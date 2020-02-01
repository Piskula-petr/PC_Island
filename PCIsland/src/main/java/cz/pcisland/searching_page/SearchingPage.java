package cz.pcisland.searching_page;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductPage;

/**
 * 	Třída vyhledávané fráze:
 * 
 * 		nastavení titulku,
 * 		nastavení vyhledávaných produktů
 */

public class SearchingPage extends ProductPage {

	private static final long serialVersionUID = 1L;

// Bezparametrový konstruktor /////////////////////////////////////////////////////////////////////////
	
	public SearchingPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public SearchingPage(List<Product> searchedProducts, String search) {
		
		// Nastavení titulku
		setTitle(new Model("Vyhledáno: " + search + " | PC Island"));
		
		add(new Label("searchLabel", search));
		
		// Vyhledávané produkty PageableListView (ProductPage)
		productsPageableListView.setList(searchedProducts);
		products = productsPageableListView.getList();
	}
	
}
