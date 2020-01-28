package cz.pcisland.product;

import java.text.DecimalFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.resource.ContextRelativeResource;

/**
 * 	Třída rozšiřující mateřskou třídu:
 * 
 * 		list nejprodávanějších produktů
 */

public abstract class ProductPageWithTopSelling extends ProductPage {

	private static final long serialVersionUID = 1L;
	
	// Komponenta pro potomka třídy
	protected ListView<Product> topSellingListView;
	
// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public ProductPageWithTopSelling() {
		
		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		
// Nejprodávanější produkty ListView /////////////////////////////////////////////////////////////////
		
		topSellingListView = new ListView<Product>("topSellingListView") {
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				item.add(new Label("counter", (item.getIndex() + 1) + "."));
				item.add(new Link<Object>("imageLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Image("image", new ContextRelativeResource(imagePath))));
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("name", product.getName())));
				
				item.add(new Link<Object>("descriptionLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("description", product.getDescription(product.getType(), product.getParametrs()))));
				
				item.add(new Label("price", decimalFormat.format(product.getPrice()) + ",-"));
			}
		};
		add(topSellingListView);
	}
	
}
