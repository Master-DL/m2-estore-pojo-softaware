package core;

import estorePojo.exceptions.InsufficientBalanceException;

public class Account {

    private double amount;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public void credit(double amount) {
        this.amount += amount;        
    }

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
