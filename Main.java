package main;

import estore.services.implem.Account;
import estore.services.implem.Bank;
import estore.services.implem.Client;
import estore.services.implem.Provider;
import estore.services.implem.Store;

public class Main {

	public static void main(String[] args) {
		Provider prov = new Provider();
		Account estore = new Account();
		Account anne = new Account();
		Account bob = new Account();
		Bank bank = new Bank(estore, anne, bob, estore, anne, bob);
		Store store = new Store(prov, prov, bank);
		Client cl = new Client(store, store, store);
		
		cl.run();

	}

}
