package cz.pcisland.shipping_and_payment;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
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
import cz.pcisland.product.Product;

/**
 * 	Třída dopravy a platby:
 * 
 * 		nastavení titulku,
 * 		způsob dopravy,
 * 		typ platby,
 * 		produkty v košíku,
 * 		postup objednávky (košík),
 * 		navigační odkazy (zpět do košíku, dodací údaje),
 * 		metody (konfigurace třídy)
 */

public class ShippingAndPaymentPage extends BasePage {

	private static final long serialVersionUID = 1L;

	// Způsob doručení
	private String shipping;
	private String shippingType;
	private int shippingPrice;
	private String shippingPriceString;
	
	// Typ platby
	private String payment;
	private String paymentType;
	private int paymentPrice;
	private String paymentPriceString;
	
	private boolean shippingSelected = false;
	private boolean paymentSelected = false;
	
	// Cena bez DPH
	private int totalPriceWithoutDPH;
	private String totalPriceWithoutDPHString;
	
	// Celková cena
	private int totalPrice;
	private String totalPriceString;
	
	// Komponenty
	final Link<Object> deliveryDataLink;
	final Label shippingTypeLabel;
	final Label shippingPriceLabel;
	final Label paymentTypeLabel;
	final Label paymentPriceLabel;
	final Label totalPriceWithoutDPHLabel;
	final Label totalPriceLabel;
	final RadioGroup<String> shippingRadioGroup;
	final RadioGroup<String> paymentRadioGroup;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public ShippingAndPaymentPage() {
		
		// Nastavení titulku
		setTitle(new Model("Doprava a platba"));
		
// Způsob dopravy Form ////////////////////////////////////////////////////////////////////////////////
		
		Form shippingForm = new Form("shippingForm");
		add(shippingForm);
		
		// Konfigurace komponent
		shippingRadioGroup = new RadioGroup<String>("shippingRadioGroup", new PropertyModel<String>(this, "shipping"));
			
			shippingRadioGroup.add(new Radio<String>("personalPickupRadio", new Model("Osobní odběr;0")));
			shippingRadioGroup.add(new Label("personalPickupPrice", "zdarma"));
			
			shippingRadioGroup.add(new Radio<String>("CzechPostRadio", new Model("Česka pošta;89")));
			shippingRadioGroup.add(new Label("CzechPostPrice", "89 Kč"));
			
			shippingRadioGroup.add(new Radio<String>("DHLRadio", new Model("DHL;119")));
			shippingRadioGroup.add(new Label("DHLPrice", "119 Kč"));
			
			shippingRadioGroup.add(new Radio<String>("mailboxRadio", new Model("Zásilkovna;49")));
			shippingRadioGroup.add(new Label("mailboxPrice", "49 Kč"));
			
		
		shippingRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				shippingSelected = true;
				
				// Rozdělení typu a ceny doručení
				String[] shippingArray = shipping.split(";");
				shippingType = shippingArray[0];
				shippingPrice = Integer.parseInt(shippingArray[1]);
				
				// Přičtení ceny dopravy
				WebSession webSession = WebSession.get();
				
				if (webSession.getAttribute("cartPrice") != null) {
					totalPrice = (int) webSession.getAttribute("cartPrice");
					totalPrice = totalPrice + shippingPrice + paymentPrice;
					
					int DPH = (totalPrice * 21) / 121;
					totalPriceWithoutDPH = totalPrice - DPH;
					
					String pattern = "###,###.###";
					DecimalFormat decimalFormat = new DecimalFormat(pattern);
					totalPriceWithoutDPHString = decimalFormat.format(totalPriceWithoutDPH) + ",-";
					totalPriceString = decimalFormat.format(totalPrice) + ",-";
				}
				
				// Zpřístupnění Linku, pokud je zvolen způsob dopravy a typ platby
				if (shippingSelected && paymentSelected) {
					deliveryDataLink.setEnabled(true);
				}
				
				// Finální úprava před zobrazením
				shippingType = shippingType + " - ";
				if (shippingPrice == 0) {
					shippingPriceString = "zdarma";
				
				} else shippingPriceString = shippingPrice + ",-";
				
				// Uložení způsobu dodání
				webSession.setAttribute("shipping", shipping);
				
				target.add(shippingTypeLabel);
				target.add(shippingPriceLabel);
				target.add(totalPriceWithoutDPHLabel);
				target.add(totalPriceLabel);
				target.add(deliveryDataLink);
			}
		});
		shippingForm.add(shippingRadioGroup);
			
// Typ platby Form ////////////////////////////////////////////////////////////////////////////////////
		
		Form paymentForm = new Form("paymentForm");
		add(paymentForm);
		
		// Konfigurace komponent
		paymentRadioGroup = new RadioGroup<String>("paymentRadioGroup", new PropertyModel<String>(this, "payment"));
		
			paymentRadioGroup.add(new Radio<String>("cashOnDeliveryRadio", new Model("Dobírka;45")));
			paymentRadioGroup.add(new Label("cashOnDeliveryLabel", "45 Kč"));
			
			paymentRadioGroup.add(new Radio<String>("bankTransferRadio", new Model("Bankovní převod;0")));
			paymentRadioGroup.add(new Label("bankTransferLabel", "zdarma"));
			
			paymentRadioGroup.add(new Radio<String>("creditCardPaymentRadio", new Model("Platba kartou;0")));
			paymentRadioGroup.add(new Label("creditCardPaymentLabel", "zdarma"));
			
			paymentRadioGroup.add(new Radio<String>("paypalRadio", new Model("PayPal;0")));
			paymentRadioGroup.add(new Label("paypalLabel", "zdarma"));
			
		paymentRadioGroup.add(new AjaxFormChoiceComponentUpdatingBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				paymentSelected = true;
				
				// Rozdělení typu a ceny platby
				String[] paymentArray = payment.split(";");
				paymentType = paymentArray[0];
				paymentPrice = Integer.parseInt(paymentArray[1]);
				
				// Přičtení ceny platby
				WebSession webSession = WebSession.get();
				
				if (webSession.getAttribute("cartPrice") != null) {
					totalPrice = (int) webSession.getAttribute("cartPrice");
					totalPrice = totalPrice + paymentPrice + shippingPrice;
					
					int DPH = (totalPrice * 21) / 121;
					totalPriceWithoutDPH = totalPrice - DPH;
					
					String pattern = "###,###.###";
					DecimalFormat decimalFormat = new DecimalFormat(pattern);
					totalPriceWithoutDPHString = decimalFormat.format(totalPriceWithoutDPH) + ",-";
					totalPriceString = decimalFormat.format(totalPrice) + ",-";
				}
				
				// Zpřístupnění Linku, pokud je zvolen způsob dopravy a typ platby
				if (shippingSelected && paymentSelected) {
					deliveryDataLink.setEnabled(true);
				}
				
				// Finální úprava před zobrazením
				paymentType = paymentType + " - ";
				if (paymentPrice == 0) {
					paymentPriceString = "zdarma";
				
				} else paymentPriceString = paymentPrice + ",-";
				
				// Uložení typu platby
				webSession.setAttribute("payment", payment);
				
				target.add(paymentTypeLabel);
				target.add(paymentPriceLabel);
				target.add(totalPriceWithoutDPHLabel);
				target.add(totalPriceLabel);
				target.add(deliveryDataLink);
			}
		});
		paymentForm.add(paymentRadioGroup);
		
// Obsah košíku WebMarkupContainer ///////////////////////////////////////////////////////////////////
		
		WebMarkupContainer cartContantWebContainer = new WebMarkupContainer("cartContantWebContainer");
		add(cartContantWebContainer);
		
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
		
		// Způsob dopravy
		shippingTypeLabel = new Label("shippingType", new PropertyModel<String>(this, "shippingType"));
		shippingTypeLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(shippingTypeLabel);
		
		// Cena za dopravu
		shippingPriceLabel = new Label("shippingPrice", new PropertyModel<String>(this, "shippingPriceString"));
		shippingPriceLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(shippingPriceLabel);
		
		// Typ platby
		paymentTypeLabel = new Label("paymentType", new PropertyModel<String>(this, "paymentType"));
		paymentTypeLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(paymentTypeLabel);
		
		// Cena za platbu
		paymentPriceLabel = new Label("paymentPrice", new PropertyModel<String>(this, "paymentPriceString"));
		paymentPriceLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(paymentPriceLabel);
		
		// Celková cena bez DPH
		totalPriceWithoutDPHLabel = new Label("totalPriceWithoutDPH", new PropertyModel<String>(this, "totalPriceWithoutDPHString"));
		totalPriceWithoutDPHLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(totalPriceWithoutDPHLabel);
		
		// Celková cena
		totalPriceLabel = new Label("totalPrice", new PropertyModel<String>(this, "totalPriceString"));
		totalPriceLabel.setOutputMarkupId(true);
		cartContantWebContainer.add(totalPriceLabel);
		
// Navigační menu ////////////////////////////////////////////////////////////////////////////////////
		
		// Košík Link
		add(new Link<Object>("menuCartLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování do košíku
				setResponsePage(CartPage.class);
			}
		});
		
// Košík Link ////////////////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("cartLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování do košíku
				setResponsePage(CartPage.class);
			}
		});
		
		
		
// Dodací údaje Link //////////////////////////////////////////////////////////////////////////////////
		
		deliveryDataLink = new Link<Object>("deliveryDataLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na dodací údaje
				setResponsePage(DeliveryDataPage.class);
			}
		};
		
		deliveryDataLink.setEnabled(false);
		deliveryDataLink.setOutputMarkupId(true);
		add(deliveryDataLink);
	}
	
// Metody ////////////////////////////////////////////////////////////////////////////////////////////
	
	// Konfigurace třídy
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		// Výpočet ceny košíku
		if (WebSession.get().getAttribute("cartPrice") != null) {
			totalPrice = (int) WebSession.get().getAttribute("cartPrice");
			
			int DPH = (totalPrice * 21) / 121;
			totalPriceWithoutDPH = totalPrice - DPH;
			
			String pattern = "###,###.###";
			DecimalFormat decimalFormat = new DecimalFormat(pattern);
			totalPriceWithoutDPHString = decimalFormat.format(totalPriceWithoutDPH) + ",-";
			totalPriceString = decimalFormat.format(totalPrice) + ",-";
		}
	}
	
}
