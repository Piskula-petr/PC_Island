package cz.pcisland.order.summary;

import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.cart.CartPage;
import cz.pcisland.delivery_data.DeliveryDataPage;
import cz.pcisland.order.Order;
import cz.pcisland.order.OrderDAO;
import cz.pcisland.order.OrderDAOImpl;
import cz.pcisland.order.report.OrderReport;
import cz.pcisland.product.Product;
import cz.pcisland.shipping_and_payment.ShippingAndPaymentPage;

/*
 * 	Třída souhn objednávky:
 * 
 * 		nastavení titulku,
 *		postup objednávky (košík, doprava a platba ...),
 *		kontaktní údaje,
 *		fakturační údaje,
 *		produkty v košíku,
 *		navigační odkazy (dodací údaje, odeslat objednávku)
 */
public class OrderSummary extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Způsob dopravy
	private String shippingType;
	private int shippingPrice;
	private String shippingPriceString;
	
	// Typ platby
	private String paymentType;
	private int paymentPrice;
	private String paymentPriceString;
	
	// Cena bez DPH
	private int totalPriceWithoutDPH;
	private String totalPriceWithoutDPHString;
	
	// Celková cena
	private int totalPrice;
	private String totalPriceString;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////////////////
	
	public OrderSummary() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public OrderSummary(Order order) {

		// Nastavení titulku
		setTitle(new Model("Souhrn objednávky"));
		
// Kontaktní údaje WebMarkupContainer /////////////////////////////////////////////////////////////////
		
		WebMarkupContainer contactInformationWebContainer = new WebMarkupContainer("contactInformationWebContainer");
		add(contactInformationWebContainer);
		
		contactInformationWebContainer.add(new Label("customerFullName", order.getCustomerFullName()));
		
		contactInformationWebContainer.add(new Label("customerPhoneNumber", getDecimalFormat().format(order.getCustomerPhoneNumber())));
		
		contactInformationWebContainer.add(new Label("customerEmail", order.getCustomerEmail()));
		
// Fakturační adresa WebMarkupContainer ///////////////////////////////////////////////////////////////
		
		WebMarkupContainer billingAddressWebContainer = new WebMarkupContainer("billingAddressWebContainer");
		add(billingAddressWebContainer);
		
		billingAddressWebContainer.add(new Label("customerStreetAddress", order.getStreetAddress()));
		
		String zipCodeWithSpaceAndCity = order.getZipCodeAndCity().substring(0, 3) + " " + order.getZipCodeAndCity().substring(3);
		billingAddressWebContainer.add(new Label("customerZipCodeAndCity", zipCodeWithSpaceAndCity));
		
		billingAddressWebContainer.add(new Label("customerCountry", order.getCountry()));
		
// Obsah košíku WebMarkupContainer ////////////////////////////////////////////////////////////////////
		
		WebMarkupContainer cartContantWebContainer = new WebMarkupContainer("cartContantWebContainer");
		add(cartContantWebContainer);
		
// Obsah košíku ListView //////////////////////////////////////////////////////////////////////////////
		
		ListView<Product> cartListView = new ListView<Product>("cartListView", (List<Product>) WebSession.get().getAttribute("cartList")) {
			
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
				
				item.add(new Label("amount", product.getAmount() + "ks"));
				item.add(new Label("price", String.valueOf(price)));
			}
		};
		
		cartContantWebContainer.add(cartListView);
		
// Způsob dopravy /////////////////////////////////////////////////////////////////////////////////////
		
		String[] shippingArray = order.getDeliveryTypeAndPrice().split(";");
		shippingType = shippingArray[0];
		shippingPrice = Integer.parseInt(shippingArray[1]);
		
		// Finální úprava před zobrazením
		shippingType = shippingType + " - ";
		if (shippingPrice == 0) {
			shippingPriceString = "zdarma";
		} else shippingPriceString = shippingPrice + ",-";
		
		// Způsob dopravy Label
		cartContantWebContainer.add(new Label("shippingType", new PropertyModel<String>(this, "shippingType")));
		
		// Cena za dopravu Label
		cartContantWebContainer.add(new Label("shippingPrice", new PropertyModel<String>(this, "shippingPriceString")));
		
// Typ platby /////////////////////////////////////////////////////////////////////////////////////////
		
		String[] paymentArray = order.getPaymentTypeAndPrice().split(";");
		paymentType = paymentArray[0];
		paymentPrice = Integer.parseInt(paymentArray[1]);
		
		// Finální úprava před zobrazením
		paymentType = paymentType + " - ";
		if (paymentPrice == 0) {
			paymentPriceString = "zdarma";
		} else paymentPriceString = paymentPrice + ",-";
		
		// Typ platby Label
		cartContantWebContainer.add(new Label("paymentType", new PropertyModel<String>(this, "paymentType")));
		
		// Cena za platbu Label
		cartContantWebContainer.add(new Label("paymentPrice", new PropertyModel<String>(this, "paymentPriceString")));
		
// Celková cena ///////////////////////////////////////////////////////////////////////////////////////
		
		totalPrice = order.getTotalPrice();
		
		int DPH = (totalPrice * 21) / 121;
		totalPriceWithoutDPH = totalPrice - DPH;
		
		totalPriceWithoutDPHString = getDecimalFormat().format(totalPriceWithoutDPH) + ",-";
		totalPriceString = getDecimalFormat().format(totalPrice) + ",-";
		
		//Cena bez DPH Label
		cartContantWebContainer.add(new Label("totalPriceWithoutDPH", new PropertyModel<String>(this, "totalPriceWithoutDPHString")));
		
		// Celková cena Label
		cartContantWebContainer.add(new Label("totalPrice", new PropertyModel<String>(this, "totalPriceString")));	
				
// Navigační menu /////////////////////////////////////////////////////////////////////////////////////
		
		// Košík Link
		add(new Link<Object>("menuCartLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování do košíku
				setResponsePage(CartPage.class);
			}
		});
		
		// Doprava a platba Link
		add(new Link<Object>("menuShippingAndPaymentLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku dopravy a platby
				setResponsePage(ShippingAndPaymentPage.class);
			}
		});
		
		// Dodací údaje Link
		add(new Link<Object>("menuDeliveryDataLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na dodací údaje
				setResponsePage(DeliveryDataPage.class);
			}
		});
		
// Dodací údaje Link /////////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("deliveryDataLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na dodací údaje
				setResponsePage(DeliveryDataPage.class);
			}
		});

// Odeslání objednávky ///////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("sendOrderLink") {
			
			@Override
			public void onClick() {
				
				// Uložení objednávky do databáze
				OrderDAO orderDAO = new OrderDAOImpl();
				orderDAO.saveOrder(order);
				
				// Přesměrování na zprávu o objednávce
				setResponsePage(new OrderReport(order));	
			}
		});
	}
	
}
