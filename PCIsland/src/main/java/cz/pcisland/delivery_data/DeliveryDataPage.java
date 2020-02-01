package cz.pcisland.delivery_data;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.cart.CartPage;
import cz.pcisland.order.Order;
import cz.pcisland.order.summary.OrderSummary;
import cz.pcisland.product.Product;
import cz.pcisland.shipping_and_payment.ShippingAndPaymentPage;
import cz.pcisland.user.User;
import cz.pcisland.user.UserDAOImpl;

/**
 *	Třída dodacích údajů:
 *
 *		nastavení titulku,
 *		dodací údaje uživatele,
 *		produkty v košíku,
 *		postup objednávky (košík, doprava a platba ...),
 *		navigační odkazy (zpět k dopravě a platbě ...)
 */

public class DeliveryDataPage extends BasePage {

	private static final long serialVersionUID = 1L;

	// Parametry uživatele
	private User user;
	private String name;
	private String surname;
	private String streetAddress;
	private String city;
	private Integer zipCode;
	private String country = "Česká Republika";
	private Integer phoneNumber;
	private String email;
	
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
	
	// Komponenty
	private final Label nameLabel;
	private final TextField<String> nameTextField;
	private final Label surnameLabel;
	private final TextField<String> surnameTextField;
	private final Label phoneNumberLabel;
	private final TextField<Integer> phoneNumberTextField;
	private final Label emailLabel;
	private final TextField<String> emailTextField;
	private final Label streetAddressLabel;
	private final TextField<String> streetAddressTextField;
	private final Label cityLabel;
	private final TextField<String> cityTextField;
	private final Label zipCodeLabel;
	private final TextField<Integer> zipCodeTextField;
	private final DropDownChoice<String> countryChangeDropDownChoice;
	private final Button orderSummaryButton;
	private final Form deliveryAddressForm;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public DeliveryDataPage() {
		
		//Nastavení titulku
		setTitle(new Model("Dodací údaje"));
		
		// Načtení přihlášeného uživatele
		if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
			user = (User) AuthenticatedWebSession.get().getAttribute("currentUser");
		}
		
// Dodací adresa Form /////////////////////////////////////////////////////////////////////////////////
		
		deliveryAddressForm = new Form("deliveryAddressForm") {
			
			@Override
			protected void onError() {
				super.onError();
				
				// Validace jména
				if (!nameTextField.isValid()) {
					nameLabel.setDefaultModelObject("Nezadali jste jméno");
					nameLabel.add(new AttributeModifier("style", "color: red;"));
					nameTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					nameLabel.setDefaultModelObject("");
					nameTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace příjmení
				if (!surnameTextField.isValid()) {
					surnameLabel.setDefaultModelObject("Nezadali jste příjmení");
					surnameLabel.add(new AttributeModifier("style", "color: red;"));
					surnameTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					surnameLabel.setDefaultModelObject("");
					surnameTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
				
				// Validace telefonního čísla
				if (!phoneNumberTextField.isValid()) {
					phoneNumberLabel.setDefaultModelObject("Nezadali jste telefonní číslo");
					phoneNumberLabel.add(new AttributeModifier("style", "color: red;"));
					phoneNumberTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					phoneNumberLabel.setDefaultModelObject("");
					phoneNumberTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace emailu
				if (!emailTextField.isValid()) {
					emailLabel.setDefaultModelObject("Nezadali jste email");
					emailLabel.add(new AttributeModifier("style", "color: red;"));
					emailTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					emailLabel.setDefaultModelObject("");
					emailTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace adresy
				if (!streetAddressTextField.isValid()) {
					streetAddressLabel.setDefaultModelObject("Nezadali jste adresu");
					streetAddressLabel.add(new AttributeModifier("style", "color: red;"));
					streetAddressTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					streetAddressLabel.setDefaultModelObject("");
					streetAddressTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				} 
			
				// Validace města
				if (!cityTextField.isValid()) {
					cityLabel.setDefaultModelObject("Nezadali jste město");
					cityLabel.add(new AttributeModifier("style", "color: red;"));
					cityTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					cityLabel.setDefaultModelObject("");
					cityTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			
				// Validace směrovacího čísla
				if (!zipCodeTextField.isValid()) {
					zipCodeLabel.setDefaultModelObject("Nezadali jste směrovací číslo");
					zipCodeLabel.add(new AttributeModifier("style", "color: red;"));
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid red"));
				
				} else {
					zipCodeLabel.setDefaultModelObject("");
					zipCodeTextField.add(new AttributeModifier("style", "border: 1px solid green"));
				}
			}
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Nastavení paramatrů objednávky
				Order order = new Order();
				if (user != null) {
					order.setIdUser(user.getId());
				}
				
				order.setCustomerFullName(name + " " + surname);
				order.setCustomerEmail(email);
				order.setCustomerPhoneNumber(phoneNumber);
				order.setStreetAddress(streetAddress);
				order.setZipCodeAndCity(String.valueOf(zipCode) + " " + city);
				order.setCountry(country);
				
				// Aktuální datum
				LocalDate dateNow = LocalDate.now();
				order.setCreationDate(dateNow);
				order.setStatus("Nová");

				WebSession webSession = WebSession.get();
				List<Product> cartItems = (List<Product>) webSession.getAttribute("cartList");
				
				// Uložení všech parametrů produktů, separované středníkem
				String productTypes = "";
				String productNames = "";
				String productAmount = "";
				String productsPrices = "";
				
				for (Product product : cartItems) {
					productTypes = productTypes + product.getType() + ";";
					order.setProductTypes(productTypes);
					
					productNames = productNames + product.getName() + ";";
					order.setProductNames(productNames);
					
					productAmount = productAmount + product.getAmount() + ";";
					order.setProductAmount(productAmount);
					
					productsPrices = productsPrices + product.getPrice() + ";";
					order.setProductPrices(productsPrices);
				}

				order.setDeliveryTypeAndPrice((String) webSession.getAttribute("shipping"));
				order.setPaymentTypeAndPrice((String) webSession.getAttribute("payment"));
				order.setTotalPrice((int) webSession.getAttribute("cartTotalPrice"));

				// Přesměrování na souhrn objednávky
				setResponsePage(new OrderSummary(order));
			}
		};
		add(deliveryAddressForm);
		
// Konfigurace komponent /////////////////////////////////////////////////////////////////////////////
		
		// Jméno
		nameLabel = new Label("nameLabel", "");
		deliveryAddressForm.add(nameLabel);
		
		nameTextField = new TextField<>("name", new PropertyModel<String>(this, "name"));
		nameTextField.setRequired(true);
		deliveryAddressForm.add(nameTextField);
		
		// Příjmení
		surnameLabel = new Label("surnameLabel", "");
		deliveryAddressForm.add(surnameLabel);
		
		surnameTextField = new TextField("surname", new PropertyModel<String>(this, "surname"));
		surnameTextField.setRequired(true);
		deliveryAddressForm.add(surnameTextField);
		
		// Telefonní číslo
		phoneNumberLabel = new Label("phoneNumberLabel", "");
		deliveryAddressForm.add(phoneNumberLabel);
		
		phoneNumberTextField = new TextField<Integer>("phoneNumber", new PropertyModel<Integer>(this, "phoneNumber"));
		phoneNumberTextField.setRequired(true);
		phoneNumberTextField.add(new RangeValidator<Integer>(100000000, 999999999));
		deliveryAddressForm.add(phoneNumberTextField);
		
		// Email
		emailLabel = new Label("emailLabel", "");
		deliveryAddressForm.add(emailLabel);
		
		emailTextField = new TextField("email", new PropertyModel<String>(this, "email"));
		emailTextField.setRequired(true);
		emailTextField.add(EmailAddressValidator.getInstance());
		deliveryAddressForm.add(emailTextField);
		
		// Adresa
		streetAddressLabel = new Label("streetAddressLabel", "");
		deliveryAddressForm.add(streetAddressLabel);
		
		streetAddressTextField = new TextField("streetAddress", new PropertyModel<String>(this, "streetAddress"));
		streetAddressTextField.setRequired(true);
		deliveryAddressForm.add(streetAddressTextField);
		
		// Město
		cityLabel = new Label("cityLabel", "");
		deliveryAddressForm.add(cityLabel);
		
		cityTextField = new TextField("city", new PropertyModel<String>(this, "city"));
		cityTextField.setRequired(true);
		deliveryAddressForm.add(cityTextField);
		
		// Směrovací číslo
		zipCodeLabel = new Label("zipCodeLabel", "");
		deliveryAddressForm.add(zipCodeLabel);
		
		zipCodeTextField = new TextField("zipCode", new PropertyModel<Integer>(this, "zipCode"));
		zipCodeTextField.setRequired(true);
		zipCodeTextField.add(new RangeValidator<Integer>(10000, 99999));
		deliveryAddressForm.add(zipCodeTextField);
		
		// Stát
		countryChangeDropDownChoice = new DropDownChoice("country", new PropertyModel<String>(this, "country"), new UserDAOImpl().getCountries());
		deliveryAddressForm.add(countryChangeDropDownChoice);
		
		// Přednastavení údajů podle přihlášeného uživatele
		if (user != null) {
			nameTextField.setDefaultModelObject(user.getName());
			surnameTextField.setDefaultModelObject(user.getSurname());
			phoneNumberTextField.setDefaultModelObject(user.getPhoneNumber());
			emailTextField.setDefaultModelObject(user.getEmail());
			streetAddressTextField.setDefaultModelObject(user.getStreetAddress());
			cityTextField.setDefaultModelObject(user.getCity());
			zipCodeTextField.setDefaultModelObject(user.getZipCode());
			countryChangeDropDownChoice.setDefaultModelObject(user.getCountry());
		}
		
// Obsah košíku WebMarkupContainer ///////////////////////////////////////////////////////////////////
		
		WebMarkupContainer cartContantWebContainer = new WebMarkupContainer("cartContantWebContainer");
		add(cartContantWebContainer);
		
		// Obsah košíku ListView
		ListView<Product> cartListView = new ListView<Product>("cartListView", (List<Product>) WebSession.get().getAttribute("cartList")) {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Přesměrování v případě prázdného košíku
				if (getList().isEmpty()) {
					setResponsePage(CartPage.class);
				}
			}
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String price = decimalFormat.format(product.getPrice()) + ",-";
				
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
		
		WebSession webSession = WebSession.get();
		
		if (webSession.getAttribute("shipping") != null) {
			String shipping = (String) webSession.getAttribute("shipping");
			String[] shippingArray = shipping.split(";");
			shippingType = shippingArray[0];
			shippingPrice = Integer.parseInt(shippingArray[1]);
			
			// Finální úprava před zobrazením
			shippingType = shippingType + " - ";
			if (shippingPrice == 0) {
				shippingPriceString = "zdarma";
			
			} else shippingPriceString = shippingPrice + ",-";
		}
		
		// Způsob dopravy Label
		Label shippingTypeLabel = new Label("shippingType", new PropertyModel<String>(this, "shippingType"));
		cartContantWebContainer.add(shippingTypeLabel);
		
		// Cena za dopravu Label
		Label shippingPriceLabel = new Label("shippingPrice", new PropertyModel<String>(this, "shippingPriceString"));
		cartContantWebContainer.add(shippingPriceLabel);
		
// Typ platby /////////////////////////////////////////////////////////////////////////////////////////
		
		if (webSession.getAttribute("payment") != null) {
			String payment = (String) webSession.getAttribute("payment");
			String[] paymentArray = payment.split(";");
			paymentType = paymentArray[0];
			paymentPrice = Integer.parseInt(paymentArray[1]);
			
			// Finální úprava před zobrazením
			paymentType = paymentType + " - ";
			if (paymentPrice == 0) {
				paymentPriceString = "zdarma";
			
			} else paymentPriceString = paymentPrice + ",-";
		}
		
		// Typ platby Label
		cartContantWebContainer.add(new Label("paymentType", new PropertyModel<String>(this, "paymentType")));
		
		// Cena za platbu Label
		cartContantWebContainer.add(new Label("paymentPrice", new PropertyModel<String>(this, "paymentPriceString")));
		
// Celková cena ///////////////////////////////////////////////////////////////////////////////////////
		
		if (webSession.getAttribute("cartPrice") != null) {
			totalPrice = (int) webSession.getAttribute("cartPrice");
			totalPrice = totalPrice + shippingPrice + paymentPrice;
			webSession.setAttribute("cartTotalPrice", totalPrice);
			
			int DPH = (totalPrice * 21) / 121;
			totalPriceWithoutDPH = totalPrice - DPH;
			
			String pattern = "###,###.###";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			
			totalPriceWithoutDPHString = decimalFormat.format(totalPriceWithoutDPH) + ",-";
			totalPriceString = decimalFormat.format(totalPrice) + ",-";
		}
		
		// Cena bez DPH Label
		cartContantWebContainer.add(new Label("totalPriceWithoutDPH", new PropertyModel<String>(this, "totalPriceWithoutDPHString")));
		
		// Celková cena Label
		cartContantWebContainer.add(new Label("totalPrice", new PropertyModel<String>(this, "totalPriceString")));
		
// Navigační menu /////////////////////////////////////////////////////////////////////////////////////
		
		// Košík Link
		add(new Link<Object>("menuCartLink") {
			
			@Override
			public void onClick() {
				
				// Přesměruje do košíku
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
		
// Doprava a platba Link //////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("shippingAndPaymentLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku dopravy a platby
				setResponsePage(ShippingAndPaymentPage.class);
			}
		});
		
// Souhrn objednávky Button ///////////////////////////////////////////////////////////////////////////
		
		orderSummaryButton = new Button("orderSummary");
		add(orderSummaryButton);
	}
	
}
