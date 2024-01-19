package core;

public class ItemInStock {

	/** The total number of ItemInStock instances created. */
	private static int numItems;

	/** The index of this item. */
	private int num;

	private Object item;
	private int quantity;
	private double price;
	private Provider provider;

	private ItemInStock() {
		num = numItems++;
	}

	public ItemInStock(Object item, int quantity, double price, Provider provider) {
        this();
		this.item = item;
		this.quantity = quantity;
		this.price = price;
		this.provider = provider;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void changeQuantity(int qtyToAddOrRemove) {
		if ((qtyToAddOrRemove >= 0 ) || (quantity >= -qtyToAddOrRemove)) {
			quantity += qtyToAddOrRemove ;
		}
	}

	@Override
	public String toString() {
		return "ItemInStock [num=" + num + ", item=" + item + ", quantity=" + quantity + ", price=" + price
				+ ", provider=" + provider + "]";
	}


}
