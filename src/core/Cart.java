package core;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    
    /** The client owning the cart. */
    private final Client client;
    
    /** The items currently added to the cart. key=item, value=quantity. */
    private Map<Object,Integer> items = new HashMap<>();
    
    
    public Cart(Client client) {
        this.client = client;
    }
    
    public void addItem( Object item, int qty ) {
        int oldQty = 0;
        if ( items.containsKey(item) ) {
            // The item has already been put in the cart
            // Increase the number
            oldQty = ((Integer) items.get(item)).intValue();
        }
        items.put( item, qty+oldQty );
    }
    
    public Map getItems() {
        return items;
    }

	public Client getClient() {
		return client;
	}
}
