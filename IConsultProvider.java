package estore.services.interfaces;

import estorePojo.exceptions.UnknownItemException;

public interface IConsultProvider {

	/**
	 * Get the price of an item provided by this provider.
	 * 
	 * @param item
	 * @return
	 */
	double getPrice(Object item) throws UnknownItemException;

}