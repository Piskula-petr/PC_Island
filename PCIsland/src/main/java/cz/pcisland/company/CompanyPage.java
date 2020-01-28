package cz.pcisland.company;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductPage;

/**
 * 	Třída produktů společnosti:
 * 
 * 		nastavení titulku,
 * 		nastavení produktů
 */

public class CompanyPage extends ProductPage {
	
	private static final long serialVersionUID = 1L;

// Bezparametrový konstruktor //////////////////////////////////////////////////////////////////////////
	
	public CompanyPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public CompanyPage(List<Product> companyProducts, String companyName, String imageName) {
		
		// nastavení titulku
		setTitle(new Model(companyName + " | PC Island"));
		
		add(new Label("company", "Produkty společnosti " + companyName));
		
		// Logo společnosti
		String imagePath = "companies logos//" + imageName + ".png";
		add(new Image("image", new ContextRelativeResource(imagePath)) {
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				// Úpravení pozice loga spoločnosti
				if (companyName.equals("GIGABYTE")) {
					tag.put("style", "margin-top: -45px;");
				
				} else if (companyName.equals("G.SKILL")) {
					tag.put("style", "margin-top: -65px;");
				
				} else if (companyName.equals("Intel")) {
					tag.put("style", "margin-top: -65px; height: 65px;");
				
				} else if (companyName.equals("MSI")) {
					tag.put("style", "margin-top: -55px;");
				
				} else if (companyName.equals("Samsung")) {
					tag.put("style", "margin-top: -55px;");
				}
			}
		});
		
		// Produkty společnosti PageableListView (ProductPage)
		productsPageableListView.setList(companyProducts);
		products = productsPageableListView.getList();
	}
	
}
