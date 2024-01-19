package estore.services.implem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import estore.data.Cart;
import estore.data.ItemInStock;
import estore.data.Order;
import estore.services.interfaces.IBankDesk;
import estore.services.interfaces.ICartLane;
import estore.services.interfaces.IConsultProvider;
import estore.services.interfaces.IFastLane;
import estore.services.interfaces.IJustHaveALook;
import estore.services.interfaces.IOrderProvider;
import estorePojo.exceptions.InsufficientBalanceException;
import estorePojo.exceptions.InvalidCartException;
import estorePojo.exceptions.UnknownAccountException;
import estorePojo.exceptions.UnknownItemException;

public class Store implements IJustHaveALook, IFastLane, ICartLane {

	private IConsultProvider providerPriceManager;
	private IOrderProvider providerStockManager;
	private IBankDesk bank;

	/**
	 * Constructs a new StoreImpl
	 */
	public Store(IConsultProvider priceManager, IOrderProvider stockManager, IBankDesk bk) {
		this.providerPriceManager = priceManager;
		this.providerStockManager = stockManager;
		bank = bk;
	}

	/**
	 * @param item a given item
	 * @return the price of a given item
	 * @throws UnknownItemException
	 */
	@Override
	public double getPrice(Object item) throws UnknownItemException {
		return providerPriceManager.getPrice(item);
	}

	/**
	 * @param item a given item
	 * @param qty  a given quantity
	 * @return true if the given quantity of the given item is available directly
	 *         from the store i.e. without having to re-order it from the provider
	 */
	@Override
	public boolean isAvailable(Object item, int qty) throws UnknownItemException {

		if (!itemsInStock.containsKey(item))
			throw new UnknownItemException("Item " + item + " does not correspond to any known reference");

		ItemInStock iis = itemsInStock.get(item);
		return (iis.getQuantity() >= qty);
	}

	/**
	 * Add an item to a cart. If the cart does not exist yet, create a new one. This
	 * method is called for each item one wants to add to the cart.
	 * 
	 * @param cart   a previously created cart or null
	 * @param client
	 * @param item
	 * @param qty
	 * @return Implementation dependant. Either a new cart at each call or the same
	 *         cart updated.
	 * 
	 * @throws UnknownItemException
	 * @throws MismatchClientCartException if the given client does not own the
	 *                                     given cart
	 */
	@Override
	public Cart addItemToCart(Cart cart, Client client, Object item, int qty)
			throws UnknownItemException, InvalidCartException {

		if (cart == null) {
			// If no cart is provided, create a new one
			cart = new Cart(client);
		} else {
			if (client != cart.getClient())
				throw new InvalidCartException("Cart " + cart + " does not belong to " + client);
		}

		cart.addItem(item, qty);

		return cart;
	}

	/**
	 * Once all the items have been added to the cart, this method finish make the
	 * payment
	 * 
	 * @param cart
	 * @param address
	 * @param bankAccountRef
	 * @return the order
	 * 
	 * @throws UnknownItemException
	 */
	@Override
	public Order pay(Cart cart, String address, String bankAccountRef)
			throws InvalidCartException, UnknownItemException, InsufficientBalanceException, UnknownAccountException {

		if (cart == null)
			throw new InvalidCartException("Cart shouldn't be null");

		// Create a new order
		Order order = new Order(cart.getClient(), address, bankAccountRef);
		orders.put(order.getKey(), order);

		// Order all the items of the cart
		Set entries = cart.getItems().entrySet();
		for (Iterator iter = entries.iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object item = entry.getKey();
			int qty = ((Integer) entry.getValue()).intValue();

			treatOrder(order, item, qty);
		}
		double amount = order.computeAmount();

		// Make the payment
		// Throws InsuffisiantBalanceException if the client account is
		// not sufficiently balanced
		bank.transfert(bankAccountRef, toString(), amount);

		return order;
	}

	/**
	 * A map of emitted orders. keys = order keys as Integers values = Order
	 * instances
	 */
	private Map<Integer, Order> orders = new HashMap<>();

	/**
	 * A map of items available in the stock of the store. keys = the references of
	 * the items as Objects values = ItemInStock instances
	 */
	private Map<Object, ItemInStock> itemsInStock = new HashMap<>();

	/**
	 * Used by a client to order an item. The whole process of ordering is
	 * encapsulated by this method. If several items need to be ordered, this method
	 * needs to be called several times, but the items will appear in separate
	 * orders.
	 * 
	 * @param client
	 * @param item
	 * @param qty
	 * @param address
	 * @param bankAccountRef
	 * @return the order
	 * 
	 * @throws UnknownItemException
	 * @throws InsufficientBalanceException
	 * @throws UnknownAccountException
	 */
	@Override
	public Order oneShotOrder(Client client, Object item, int qty, String address, String bankAccountRef)
			throws UnknownItemException, InsufficientBalanceException, UnknownAccountException {

		// Create a new order
		Order order = new Order(client, address, bankAccountRef);
		orders.put(order.getKey(), order);

		// Treat the item ordered
		treatOrder(order, item, qty);
		double amount = order.computeAmount();

		// Make the payment
		// Throws InsuffisiantBalanceException if the client account is
		// not sufficiently balanced
		bank.transfert(bankAccountRef, toString(), amount);

		return order;
	}

	/**
	 * Treat an item ordered by a client and update the corresponding order.
	 * 
	 * @param order
	 * @param item
	 * @param qty
	 * @return
	 * 
	 * @throws UnknownItemException
	 * @throws InsufficientBalanceException
	 * @throws UnknownAccountException
	 */
	private void treatOrder(Order order, Object item, int qty) throws UnknownItemException {

		// The number of additional item to order
		// in case we need to place an order to the provider
		final int more = 10;

		// The price of the ordered item
		// Throws UnknownItemException if the item does not exist
		final double price = providerPriceManager.getPrice(item);

		final double totalAmount = price * qty;

		// The delay (in hours) for delivering the order
		// By default, it takes 2 hours to ship items from the stock
		// This delay increases if an order is to be placed to the provider
		int delay = 2;

		// Check whether the item is available in the stock
		// If not, place an order for it to the provider
		ItemInStock iis = (ItemInStock) itemsInStock.get(item);
		if (iis == null) {
			int quantity = qty + more;
			delay += providerStockManager.order(this, item, quantity);
			ItemInStock newItem = new ItemInStock(item, more, price, providerStockManager);
			itemsInStock.put(item, newItem);
		} else {
			// The item is in the stock
			// Check whether there is a sufficient number of them
			// to match the order
			if (iis.getQuantity() >= qty) {
				iis.changeQuantity(qty);
			} else {
				// An order to the provider needs to be issued
				int quantity = qty + more;
				delay += providerStockManager.order(this, item, quantity);
				iis.changeQuantity(more);
			}
		}

		// Update the order
		order.addItem(item, qty, price);
		order.setDelay(delay);
	}

	// -----------------------------------------------------
	// Other methods
	// -----------------------------------------------------

	public String toString() {
		return "E-Store";
	}
}
