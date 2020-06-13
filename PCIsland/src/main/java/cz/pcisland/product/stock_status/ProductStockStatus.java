package cz.pcisland.product.stock_status;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.base_page.CustomPagingNavigator;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductDAOImpl;

/**
 * 	Třída změny stavu zboží (Admin only):
 * 
 * 		nastavení titulku,
 * 		filtr (procesory, grafické karty ...),
 *		konfigurace komponenty filtru,
 * 		list produktů
 */
public class ProductStockStatus extends BasePage {

	private static final long serialVersionUID = 1L;

	private Integer idProduct;
	private Integer newStock;
	
	// Komponenty
	private NumberTextField<Integer> amountNumberTextField;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public ProductStockStatus() {
		
		// Nastaví titulek
		setTitle(new Model("Přehled zboží"));
		
// Stav zboží Form ////////////////////////////////////////////////////////////////////////////////////

		Form productsStatusForm = new Form("productsStatusForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Změní množství na skladu
				new ProductDAOImpl().changeStock(idProduct, newStock);
				setResponsePage(ProductStockStatus.class);
			}
		};
		
		productsStatusForm.setOutputMarkupId(true);
		add(productsStatusForm);
		
// Stav zboží PageableListView ///////////////////////////////////////////////////////////////////////
		
		PageableListView<Product> productsStatusPageableListView = new PageableListView<Product>("productsStatusPageableListView", new ProductDAOImpl().getAllProductSortByStock(), 10) {
			
			int option = 0;
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				item.add(new Link<Object>("imageLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Image("image", new ContextRelativeResource(imagePath)))) ;
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("name", product.getName())));
				
				item.add(new Label("stock", product.getStock()) {
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						
						// Změna barvy podle počtu položek na skladu
						if (product.getStock() <= 0) {
							tag.put("style", "color: red;");
							
						} else tag.put("style", "color: green;");
					}
				});
				
				amountNumberTextField = new NumberTextField("amount", new PropertyModel<Integer>(this, "option"));
				amountNumberTextField.add(new OnChangeAjaxBehavior() {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						idProduct = product.getId();
						newStock = product.getStock() + amountNumberTextField.getModelObject();
					}
				});
				item.add(amountNumberTextField);
			}
		};
		
		productsStatusForm.add(productsStatusPageableListView);
		add(new CustomPagingNavigator("pagingNavigator", productsStatusPageableListView));
	}
	
}
