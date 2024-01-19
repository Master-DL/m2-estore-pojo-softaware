package estore.services.implem;

import estore.services.interfaces.IAdminAccount;
import estore.services.interfaces.IBankDesk;
import estore.services.interfaces.IBusinessAccount;
import estorePojo.exceptions.InsufficientBalanceException;
import estorePojo.exceptions.UnknownAccountException;

public class Bank implements IBankDesk {

	private IBusinessAccount estore, anne, bob;

	public Bank(IAdminAccount estoreAdmin, IAdminAccount anneAdmin, IAdminAccount bobAdmin,
				IBusinessAccount estore, IBusinessAccount anne, IBusinessAccount bob) {
		this.estore = estore;
		this.anne = anne;
		this.bob = bob;

		estoreAdmin.setOwner("Estore");
		estoreAdmin.setAmount(0);
		anneAdmin.setOwner("Anne");
		anneAdmin.setAmount(30);
		bobAdmin.setOwner("Bob");
		bobAdmin.setAmount(100);
	}

	@Override
	public void transfert(String from, String to, double amount)
			throws InsufficientBalanceException, UnknownAccountException {
		IBusinessAccount Afrom = null;
		IBusinessAccount Ato = null;

		if (from.equals("E-Store"))
			Afrom = estore;
		else if (from.equals("Anne"))
			Afrom = anne;
		else if (from.equals("Bob"))
			Afrom = bob;

		if (to.equals("E-Store"))
			Ato = estore;
		else if (to.equals("Anne"))
			Ato = anne;
		else if (to.equals("Bob"))
			Ato = bob;

		// Perform the transfert
		Afrom.withdraw(amount);
		Ato.credit(amount);
	}

}
