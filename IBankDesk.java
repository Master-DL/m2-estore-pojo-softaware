package estore.services.interfaces;

import estorePojo.exceptions.InsufficientBalanceException;
import estorePojo.exceptions.UnknownAccountException;

public interface IBankDesk {

	void transfert(String from, String to, double amount) throws InsufficientBalanceException, UnknownAccountException;

}