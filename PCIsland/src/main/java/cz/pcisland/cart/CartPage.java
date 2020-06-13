package cz.pcisland.cart;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.home_page.HomePage;
import cz.pcisland.product.Product;
import cz.pcisland.shipping_and_payment.ShippingAndPaymentPage;

/**
 *	Třída košíku:
 *
 *		nastavení titulku,
 *		produkty v košíku,
 *		navigační odkazy (zpět do obchodu ...)
 *
 *	Metody:
 *
 *		konfigurace třídy
 */
public class CartPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Cena bez DPH
	private int totalPriceWithoutDPH;
	private String totalPriceWithoutDPHString = "0 ,-";
	
	// Celková cena
	private int totalPrice;
	private String totalPriceString = "0 ,-";
	
	private Link deliveryAndPaymentLink;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public CartPage() {
		
		// Nastavení titulku
		setTitle(new Model("Košík"));
		
// Košík WebMarkupContainer ///////////////////////////////////////////////////////////////////////////
	
		WebMarkupContainer cartWebContainer = new WebMarkupContainer("cartWebContainer");
		add(cartWebContainer);
		
		// Popisek v případě prázdného košíku
		Label emptyCartLabel = new Label("emptyCartLabel", "V košíku není žádné zboží.");
		cartWebContainer.add(emptyCartLabel);
		
		// Oddělení obsahu a ceny košíku
		Label emptyCartUnderline = new Label("emptyCartUnderline");
		cartWebContainer.add(emptyCartUnderline);

// Košík ListView /////////////////////////////////////////////////////////////////////////////////////
		
		ListView<Product> cartListView = new ListView<Product>("cartListView", (List<Product>) WebSession.get().getAttribute("cartList")) {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost náhledu košíku
				if (getList().size() == 0) {
					
					emptyCartLabel.setVisible(true);
					emptyCartUnderline.setVisible(true);
					deliveryAndPaymentLink.setEnabled(false);
				
				} else {
					
					emptyCartLabel.setVisible(false);
					emptyCartUnderline.setVisible(false);
					deliveryAndPaymentLink.setEnabled(true);
				}
			}
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";

				String price = getDecimalFormat().format(product.getPrice()) + ",-";
				
				String stock = "";
				if (product.getStock() > 4) {
					stock = "Skladem " + String.valueOf(product.getStock() + " kusů");
				
				} else if (product.getStock() == 1) {
					stock = "Poslední kus";
				
				} else if (product.getStock() > 1 && product.getStock() < 5) {
					stock = "Skladem " + String.valueOf(product.getStock() + " kusy");
				
				} else if (product.getStock() <= 0) {
					stock = "Není Skladem";
				} 
				
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
				
				item.add(new Label("stock", stock) {
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						
						// Změna třídy podle počtu produktů
						if (product.getStock() <= 0) {
							tag.put("class", "outStock");
							
						} else tag.put("class", "onStock");
					}
				});
				
				NumberTextField numberTextField = new NumberTextField<Integer>("amount", Model.of(product.getAmount()));
				numberTextField.setMinimum(1);
				numberTextField.add(new OnChangeAjaxBehavior() {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						
						// Upravení množství položek v košíku
						if (numberTextField.getModelObject() != null) {
							product.setAmount((int) numberTextField.getModelObject());
							
							// Výpočet nové ceny
							int newPrice = 0;
							for (Product product : getList()) {
								newPrice = newPrice + (product.getAmount() * product.getPrice());
							}
							
							WebSession.get().setAttribute("cartPrice", newPrice);
							setResponsePage(CartPage.class);
						}
					}
				});
				
				item.add(numberTextField);
				item.add(new Label("price", String.valueOf(price)));
				item.add(new Link<Object>("removeLink") {
				
					@Override
					public void onClick() {
						
						// Odstranění položky z košíku + uložení upraveného listu
						getList().remove(product);
						WebSession webSession = WebSession.get();
						webSession.setAttribute("cartList", (Serializable) getList());
						
						// Úprava velikosti košíku
						webSession.setAttribute("cartSize", getList().size());
						
						// Úprava ceny košíku
						totalPrice = totalPrice - (product.getAmount() * product.getPrice());
						webSession.setAttribute("cartPrice", totalPrice);
						
						setResponsePage(CartPage.class);
					}
				});
			}
		};
		cartWebContainer.add(cartListView);

		// Celková cena bez DPH
		cartWebContainer.add(new Label("totalPriceWithoutDPH", new PropertyModel<String>(this, "totalPriceWithoutDPHString")));
		
		// Celková cena
		cartWebContainer.add(new Label("totalPrice", new PropertyModel<String>(this, "totalPriceString")));
		
// Zpět na hlavní stránku Link ////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("backToHomePageLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na domovskou stránku
				setResponsePage(HomePage.class);
			}
		});

// Doprava a platba Link //////////////////////////////////////////////////////////////////////////////
		
		deliveryAndPaymentLink = new Link<Object>("deliveryAndPaymentLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku dopravy a platby
				setResponsePage(ShippingAndPaymentPage.class);
			}
		};
		add(deliveryAndPaymentLink);
	}
	
// Metody /////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 	Konfigura třídy
	 */
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		// Výpočet ceny košíku
		if (WebSession.get().getAttribute("cartPrice") != null) {
			totalPrice = (int) WebSession.get().getAttribute("cartPrice");
			
			int DPH = (totalPrice * 21) / 121;
			totalPriceWithoutDPH = totalPrice - DPH;

			totalPriceWithoutDPHString = getDecimalFormat().format(totalPriceWithoutDPH) + ",-";
			totalPriceString = getDecimalFormat().format(totalPrice) + ",-";
		}
	}
	
}
