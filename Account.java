package estore.services.implem;

import estore.services.interfaces.IAdminAccount;
import estore.services.interfaces.IBusinessAccount;
import estore.services.interfaces.IConsultAccount;
import estorePojo.exceptions.InsufficientBalanceException;

public class Account implements IConsultAccount, IAdminAccount, IBusinessAccount {

    private double amount;
    private String owner;

    @Override
	public String getOwner() {
        return owner;
    }

    @Override
	public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
	public double getAmount() {
        return amount;
    }

    @Override
	public void setAmount(double amount) {
        this.amount = amount;
    }
    
    @Override
	public void credit(double amount) {
        this.amount += amount;        
    }

    @Override
	public void withdraw(double amount) throws InsufficientBalanceException {
        if ( this.amount < amount )
            throw new InsufficientBalanceException(owner);
        this.amount -= amount;
    }
    
    /**
     * Two AccountImpl instances are considered equals
     * if they share the same owner.
     * Of course, in a more realistic implementation,
     * we should have a account number.
     */
    public boolean equals( Object other ) {
        if( ! (other instanceof Account) )
            return false;
        Account otherAccount = (Account) other;
        return ( otherAccount.owner == owner);
    }
    
}
