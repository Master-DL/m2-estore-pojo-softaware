package estore.services.interfaces;

import estorePojo.exceptions.UnknownItemException;

public interface IOrderProvider {

	/**
	 * Emit an order for items. The provider returns the delay for delivering the
	 * items.
	 * 
	 * @param store the store that emits the order
	 * @param item  the item ordered
	 * @param qty   the quantity ordered
	 * @return the delay (in hours)
	 */
	int order(IJustHaveALook store, Object item, int qty) throws UnknownItemException;

}