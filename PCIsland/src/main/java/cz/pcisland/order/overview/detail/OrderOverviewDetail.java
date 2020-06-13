package cz.pcisland.order.overview.detail;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.order.Order;
import cz.pcisland.order.OrderDAO;
import cz.pcisland.order.OrderDAOImpl;
import cz.pcisland.order.overview.OrdersOverview;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductDAO;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.user.User;

/*
 *	Třída detailu objednávky:
 *
 *		nastavení titulku,
 *		změna stavu objednávky (Admin only),
 *		kontaktní údaje,
 *		fakturační údaje,
 *		zakoupené produkty,
 *		navigační odkaz (přehled objednávek)
 */
public class OrderOverviewDetail extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Způsob doručení
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
	
	private Product product;
	private List<Product> products = new ArrayList<>();
	
	// Komponenty
	private Button orderStatusButton;
	private WebMarkupContainer dropdownOptionsContainer;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public OrderOverviewDetail(Order order) {
		
		// Nastaví titulek
		setTitle(new Model("Objednávka číslo: " + order.getIdOrder()));
	
		add(new Label("idOrder", "Objednávka číslo: " + order.getIdOrder()));
		orderStatusButton = new Button("orderStatusButton", new Model<String>(order.getStatus()));
		add(orderStatusButton);
		
// Stavy objednávky WebMarkupContainer (Admin only) ///////////////////////////////////////////////////
		
		dropdownOptionsContainer = new WebMarkupContainer("dropdownOptionsContainer") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Zobrazí možnost změny stavu objednávky, pokud je přihlášen Admin
				User user = (User) AuthenticatedWebSession.get().getAttribute("currentUser");
				
				if (user.getEmail().equals("admin@pcisland.cz")) {
					dropdownOptionsContainer.setVisible(true);
					
				} else dropdownOptionsContainer.setVisible(false);
				
				// Posun možností stavu objednávky + uložení nového stavu
				OrderDAO orderDAO = new OrderDAOImpl();
				
				switch (order.getStatus()) {
				
					case "Nová" :
						dropdownOptionsContainer.add(new AttributeModifier("style", "margin: -17px 0px 0px 883px"));
						orderDAO.changeOrderStatus(order.getIdOrder(), "Nová");
						break;
						
					case "Zpracovaná" :
						dropdownOptionsContainer.add(new AttributeModifier("style", "margin: -17px 0px 0px 800px"));
						orderDAO.changeOrderStatus(order.getIdOrder(), "Zpracovaná");
						break;
						
					case "Odeslaná" :
						dropdownOptionsContainer.add(new AttributeModifier("style", "margin: -17px 0px 0px 828px"));
						orderDAO.changeOrderStatus(order.getIdOrder(), "Odeslaná");
						break;
						
					case "Vyřízená" :
						dropdownOptionsContainer.add(new AttributeModifier("style", "margin: -17px 0px 0px 839px"));
						orderDAO.changeOrderStatus(order.getIdOrder(), "Vyřízená");
						break;
				}
			}
		};
		
		add(dropdownOptionsContainer);
		
// Nová objednávka Link (Admin only) //////////////////////////////////////////////////////////////////
		
		dropdownOptionsContainer.add(new Link<Object>("newOrderLink") {
			
			@Override
			public void onClick() {
				orderStatusButton.setDefaultModelObject("Nová");
				order.setStatus("Nová");
			}
		});
		
// Zpracovaná objednávka Link (Admin only) ////////////////////////////////////////////////////////////
		
		dropdownOptionsContainer.add(new Link<Object>("processedOrderLink") {
			
			@Override
			public void onClick() {
				orderStatusButton.setDefaultModelObject("Zpracovaná");
				order.setStatus("Zpracovaná");
			}
		});
		
// Odeslaná objednávka Link (Admin only) //////////////////////////////////////////////////////////////
		
		dropdownOptionsContainer.add(new Link<Object>("sentOrderLink") {
			
			@Override
			public void onClick() {
				orderStatusButton.setDefaultModelObject("Odeslaná");
				order.setStatus("Odeslaná");
			}
		});
		
// Vyřízená objednávka Link (Admin only) //////////////////////////////////////////////////////////////
		
		dropdownOptionsContainer.add(new Link<Object>("completedOrderLink") {
			
			@Override
			public void onClick() {
				orderStatusButton.setDefaultModelObject("Vyřízená");
				order.setStatus("Vyřízená");
			}
		});
		
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
		
// Produkty WebMarkupContainer ///////////////////////////////////////////////////////////////////////
		
		WebMarkupContainer productsWebContainer = new WebMarkupContainer("productsWebContainer");
		add(productsWebContainer);
		
		// Rozložení typů zboží a ID Zboží
		String[] productTypes = order.getProductTypes().split(";");		// (processors,memory,hard_disks)
		String[] productNames = order.getProductNames().split(";");		// (Intel Core i7-9700K;EVGA 850 B3;WD Blue 1TB)
		String[] productAmount = order.getProductAmount().split(";");	// (1,2,1)
		String[] productPrices = order.getProductPrices().split(";");	// (7900,4620,2130)
		
		for (int i = 0; i < productTypes.length; i++) {
			
			// Uloží informace o produktu do listu
			ProductDAO productDAO = new ProductDAOImpl();
			
			switch (productTypes[i]) {
					
				case "processor" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
						
					case "graphics_card" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
						
					case "memory" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
						
					case "motherboard" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
						
					case "hard_disk" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
						
					case "power_supply_unit" :
						product = productDAO.getProductByName(productNames[i]);
						product.setAmount(Integer.parseInt(productAmount[i]));
						product.setPrice(Integer.parseInt(productPrices[i]));
						break;
			}
				
			products.add(product);
		}

// Obsah košíku ListView /////////////////////////////////////////////////////////////////////////////
		
		ListView<Product> productsListView = new ListView<Product>("productsListView", products) {
			
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
		productsWebContainer.add(productsListView);
		
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
		productsWebContainer.add(new Label("shippingType", new PropertyModel<String>(this, "shippingType")));
		
		// Cena za dopravu Label
		productsWebContainer.add(new Label("shippingPrice", new PropertyModel<String>(this, "shippingPriceString")));
		
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
		productsWebContainer.add(new Label("paymentType", new PropertyModel<String>(this, "paymentType")));
		
		// Cena za platbu Label
		productsWebContainer.add(new Label("paymentPrice", new PropertyModel<String>(this, "paymentPriceString")));
		
// Celková cena ///////////////////////////////////////////////////////////////////////////////////////
		
		totalPrice = order.getTotalPrice();
		
		int DPH = (totalPrice * 21) / 121;
		totalPriceWithoutDPH = totalPrice - DPH;
		
		totalPriceWithoutDPHString = getDecimalFormat().format(totalPriceWithoutDPH) + ",-";
		totalPriceString = getDecimalFormat().format(totalPrice) + ",-";
		
		// Cena bez DPH Label
		productsWebContainer.add(new Label("totalPriceWithoutDPH", new PropertyModel<String>(this, "totalPriceWithoutDPHString")));
		
		// Celková cena	Label
		productsWebContainer.add(new Label("totalPrice", new PropertyModel<String>(this, "totalPriceString")));			
	
// Přehled objednávek Link ////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("orderOverviewLink") {
			
			@Override
			public void onClick() {
				setResponsePage(OrdersOverview.class);
			}
		});
	}
	
}
