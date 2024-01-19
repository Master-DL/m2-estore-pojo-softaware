package main;

import estore.services.implem.src.core.services.Bank;
import core.Client;
import estore.services.implem.src.core.services.Provider;
import estore.services.implem.src.core.services.Store;

public class Main {

	public static void main(String[] args) {
		Provider prov = new Provider();
		Bank bank = new Bank();
		Store store = new Store(prov,bank);
		Client cl = new Client(store);
		
		cl.run();

	}

}
