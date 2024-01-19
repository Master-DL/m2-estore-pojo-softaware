package core;

import estorePojo.exceptions.InsufficientBalanceException;
import estorePojo.exceptions.UnknownAccountException;

public class Bank {

	private Account estore;
	private Account anne, bob;

	public Bank() {
		estore = new Account();
		anne = new Account();
		bob = new Account();

		estore.setOwner("Estore");
		estore.setAmount(0);
		anne.setOwner("Anne");
		anne.setAmount(30);
		bob.setOwner("Bob");
		bob.setAmount(100);
	}

	public void transfert(String from, String to, double amount)
			throws InsufficientBalanceException, UnknownAccountException {
		Account Afrom = null, Ato = null;

		if (from.equals("E-Store"))
			Afrom = estore;
		if (from.equals("Anne"))
			Afrom = anne;
		if (from.equals("Bob"))
			Afrom = bob;

		if (to.equals("E-Store"))
			Ato = estore;
		if (to.equals("Anne"))
			Ato = anne;
		if (to.equals("Bob"))
			Ato = bob;

		// Get the balance of the account to widthdraw
		double fromBalance = Afrom.getAmount();

		// Check whether the account is sufficiently balanced
		if (fromBalance < amount)
			throw new InsufficientBalanceException(from.toString());

		// Get the balance of the account to credit
		double toBalance = Ato.getAmount();

		// Perform the transfert
		Afrom.setAmount(fromBalance - amount);
		Ato.setAmount(toBalance + amount);
	}

}
