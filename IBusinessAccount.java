package estore.services.interfaces;

import estorePojo.exceptions.InsufficientBalanceException;

public interface IBusinessAccount {

	void credit(double amount);

	void withdraw(double amount) throws InsufficientBalanceException;

}