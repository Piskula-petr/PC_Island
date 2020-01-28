package cz.pcisland.base_page;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.account_setting.AccountSettingsPage;
import cz.pcisland.cart.CartPage;
import cz.pcisland.graphics_cards.GraphicsCardsPage;
import cz.pcisland.graphics_cards.detail.GraphicsCardDetailPage;
import cz.pcisland.hard_disks.HardDisksPage;
import cz.pcisland.hard_disks.detail.HarddiskDetailPage;
import cz.pcisland.home_page.HomePage;
import cz.pcisland.login_and_registration.LoginAndRegistrationPage;
import cz.pcisland.memory.MemoryPage;
import cz.pcisland.memory.detail.MemoryDetailPage;
import cz.pcisland.motherboards.MotherboardsPage;
import cz.pcisland.motherboards.detail.MotherBoardDetailPage;
import cz.pcisland.order.overview.OrdersOverview;
import cz.pcisland.power_supply_units.PowerSupplyUnitsPage;
import cz.pcisland.power_supply_units.detail.PowerSupplyUnitsDetailPage;
import cz.pcisland.processors.ProcessorsPage;
import cz.pcisland.processors.detail.ProcessorDetailPage;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.product.price_change.ProductPriceChange;
import cz.pcisland.product.stock_status.ProductStockStatus;
import cz.pcisland.review.overview.ReviewOverview;
import cz.pcisland.searching_page.SearchingPage;
import cz.pcisland.user.User;

/**
 * 	Hlavní abstraktní třída:
 * 		
 * 		titulek stránky,
 * 		záhlaví (vyhledávací lišta, přihlášení, košík),
 * 		navigační menu (procesory, grafické karty ...),
 * 		zápatí (Copyright)
 * 
 * 	Metody:
 * 
 * 		konfigurace třídy,
 * 		přesměrování na detail produktu,
 * 		titulek stránky
 */

public abstract class BasePage extends WebPage {

	private static final long serialVersionUID = 1L;
	
	// Přihlášení
	private User user;
	private String currentUser = "Přihlásit se";
	
	// Košík
	private int cartSize;
	private String cartPrice = "Košík";
	private int totalPrice;
	
	// Vyhledávání
	private String search = "";
	
	// Komponenty
	private Link loginPage;
	private ListView<Product> cartDropdownListView;
	private TextField<String> searchBarTextField;
	private WebMarkupContainer searchedProductsContainer;
	private ListView<Product> searchedProductsDropdownListView;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public BasePage() {
		
		// Titulek stránky
		add(new Label("titleLabel", new Model(this)));
		
// Hlavní stránka Link ////////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("homePageLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na hlavní stránku
				setResponsePage(HomePage.class);
			}
		});
		
// Vyhledávací lišta Form /////////////////////////////////////////////////////////////////////////////

		Form searchBarForm = new Form("searchBarForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Přesměrování na stránku vyhledávání
				setResponsePage(new SearchingPage(searchedProductsDropdownListView.getList(), search));
			}
		};
		searchBarForm.setOutputMarkupId(true);
		add(searchBarForm);
		
// Vyhledávací lišta TextField ////////////////////////////////////////////////////////////////////////
		
		searchBarTextField = new TextField<String>("searchBarTextField", new PropertyModel<String>(this, "search"));
		searchBarTextField.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				
				// Přidaní hledaných produktů do Listu
				if ((search != null) && (search.length() > 1)) {
					
					List<Product> searchedProducts = new ArrayList<>();
					
					for (Product product : new ProductDAOImpl().getAllProducts()) {
						
						if (product.getName().toLowerCase().contains(search.toLowerCase())) {
							searchedProducts.add(product);
						}
					}
					searchedProductsDropdownListView.setList(searchedProducts);
					
				} else searchedProductsDropdownListView.getList().clear();
				
				target.add(searchedProductsContainer);
			}
		});
		searchBarForm.add(searchBarTextField);	
		
// Hledané produkty WebMarkupContainer ////////////////////////////////////////////////////////////////
		
		searchedProductsContainer = new WebMarkupContainer("searchedProductsContainer");
		searchedProductsContainer.setOutputMarkupId(true);
		add(searchedProductsContainer);
		
		// Hledané produkty ListView		
		searchedProductsDropdownListView = new ListView<Product>("searchedProductsDropdownListView") {
			
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
				}.add(new Image("image", new ContextRelativeResource(imagePath))));
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("name", product.getName())));
			}
		};
		searchedProductsContainer.add(searchedProductsDropdownListView);
		
// Přihlašovací stránka Link //////////////////////////////////////////////////////////////////////////
		
		loginPage = new Link<Object>("loginPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování pokud uživatel není přihlášen
				if (AuthenticatedWebSession.get().getAttribute("currentUser") == null) {
					setResponsePage(LoginAndRegistrationPage.class);
				}
			}
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				// Změna na defaultní kurzor pokud je uživatel přihlášen
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					tag.put("style", "cursor: default");
				}
			}
		};
		add(loginPage);
		
// Nastavení účtu Link ////////////////////////////////////////////////////////////////////////////////
		
		loginPage.add(new Link<Object>("settingLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na nastavení účtu
				setResponsePage(new AccountSettingsPage(user));
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost linku
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					setVisible(true);
				} else setVisible(false);
			}
		});

// Objednávky Link ////////////////////////////////////////////////////////////////////////////////////
		
		loginPage.add(new Link<Object>("ordersLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na přehled objednávek
				setResponsePage(OrdersOverview.class);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost linku
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					setVisible(true);
				} else setVisible(false);
			}
		});
		
// Přehled recenzí Link ///////////////////////////////////////////////////////////////////////////////

		loginPage.add(new Link<Object>("reviewOverviewLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na přehled recenzí
				setResponsePage(ReviewOverview.class);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost linku
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					setVisible(true);
				} else setVisible(false);
			}
		});
		
// Stav zboží Link (Admin only) ///////////////////////////////////////////////////////////////////////
		
		loginPage.add(new Link<Object>("productStatusLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stav zboží
				setResponsePage(ProductStockStatus.class);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelný pouze, pokud je přihlášen Admin
				if ((AuthenticatedWebSession.get().getAttribute("currentUser") != null) 
					&& (user.getEmail().equals("admin@pcisland.cz"))) {
					setVisible(true);
				} else setVisible(false);
			}
		});
		
// Změna ceny Link (Admin only) //////////////////////////////////////////////////////////////////////
		
		loginPage.add(new Link<Object>("productPriceChangeLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na změnu ceny
				setResponsePage(ProductPriceChange.class);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelný pouze, pokud je přihlášen Admin
				if ((AuthenticatedWebSession.get().getAttribute("currentUser") != null) 
					&& (user.getEmail().equals("admin@pcisland.cz"))) {
					setVisible(true);
				} else setVisible(false);
			}
		});
		
// Odhlášení Link /////////////////////////////////////////////////////////////////////////////////////
		
		loginPage.add(new Link<Object>("logoutLink") {
			
			@Override
			public void onClick() {
				
				// Odhlášení uživatele a přesměrování na domovskou stránku
				AuthenticatedWebSession.get().setAttribute("currentUser", null);
				setResponsePage(HomePage.class);
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost linku
				if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
					setVisible(true);
				} else setVisible(false);
			}
		});
		
// Košík //////////////////////////////////////////////////////////////////////////////////////////////
		
		// Košík Link
		add(new Link<Object>("cartPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování do košíku
				setResponsePage(CartPage.class);
			}
			
		// Velikost košíku	
		}.add(new Label("cartSize", new PropertyModel<Integer>(this, "cartSize")) {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost velikosti košíku
				if (cartSize != 0) {
					setVisible(true);
				} else setVisible(false);
			}
		}));
		
// Košík ListView /////////////////////////////////////////////////////////////////////////////////////
		
		cartDropdownListView = new ListView<Product>("cartDropdownListView") {

			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String price = decimalFormat.format(product.getPrice()) + ",-";
				
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
				
				item.add(new Label("amount", product.getAmount() + "ks"));
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
						
						setResponsePage(getPageClass());
					}
				});
			}
			
		};
		add(cartDropdownListView);
		
		// Aktuální uživatel
		add(new Label("userLabel", new PropertyModel<String>(this, "currentUser")));
		
		// Cena košíku
		add(new Label("cartLabel", new PropertyModel<String>(this, "cartPrice")));
		
// Navigační menu /////////////////////////////////////////////////////////////////////////////////////
		
		// Procesory Link
		add(new Link<Object>("processorsPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na procesory
				setResponsePage(ProcessorsPage.class);
			}
		});
		
		// Grafické karty Link
		add(new Link<Object>("graphicsCardsPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na grafické karty
				setResponsePage(GraphicsCardsPage.class);
			}
		});
		
		// Operační paměti Link
		add(new Link<Object>("memoryPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na operační paměti
				setResponsePage(MemoryPage.class);
			}
		});
		
		// Základní desky Link
		add(new Link<Object>("motherboardsPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na základní desky
				setResponsePage(MotherboardsPage.class);
			}
		});
		
		// Pevné disky Link
		add(new Link<Object>("hardDisksPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na pevné disky
				setResponsePage(HardDisksPage.class);
			}
		});
		
		// Zdroje Link
		add(new Link<Object>("powerSupplyUnitsPage") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na zdroje
				setResponsePage(PowerSupplyUnitsPage.class);
			}
		});
	
		// Copyright
		add(new Label("copyright", "© 2019 - " + LocalDate.now().getYear() + " PC Island"));
	}

// Metody /////////////////////////////////////////////////////////////////////////////////////////////
	
	// Konfigurace třídy
	
	@Override
	protected void onConfigure() {
		super.onConfigure();
		
		// Ověření přihlášení
	    AuthenticatedWebSession authenticatedSession = AuthenticatedWebSession.get();
	    
     	if (authenticatedSession.getAttribute("currentUser") != null) {
     		user = (User) Session.get().getAttribute("currentUser");
     		
     		// Zobrazení uživatele / Administrátora
     		if (user.getEmail().equals("admin@pcisland.cz")) {
     			currentUser = "Admin";
     		
     		} else currentUser = user.getName().charAt(0) + ". " + user.getSurname();
     		
     	} else currentUser = "Přihlásit se";
     	
     	// Výpočet ceny košíku
     	WebSession webSession = WebSession.get();
     	
  		if (webSession.getAttribute("cartPrice") != null) {
  			totalPrice = (int) webSession.getAttribute("cartPrice");
  			
  			String pattern = "###,###.###";
  			DecimalFormat decimalFormat = new DecimalFormat(pattern);
  			
  			// Zobrazení ceny košíku
  			if ((int) webSession.getAttribute("cartPrice") == 0) {
     			cartPrice = "Košík";
     		
  			} else cartPrice = decimalFormat.format((int) webSession.getAttribute("cartPrice")) + " Kč";
  		}
     	
     	// Zobrazení počtu položek v košíku
     	if (webSession.getAttribute("cartSize") != null) {
     		cartSize = (int) webSession.getAttribute("cartSize");
     	}
     	
     	// Zobrazení produktů v košíku
     	if (webSession.getAttribute("cartList") != null) {
     		cartDropdownListView.setList((List<Product>) webSession.getAttribute("cartList"));
     	}
	}
	
	
	/**
	 * 	Přesměrování na stránku produktu
	 * 
	 * @param product - produkt
	 */
	public void getDetailPage(Product product) {
		
		switch(product.getType()) {
			case "processor" :
				setResponsePage(new ProcessorDetailPage(product));
				break;
				
			case "graphics_card" :
				setResponsePage(new GraphicsCardDetailPage(product));
				break;
				
			case "memory" :
				setResponsePage(new MemoryDetailPage(product));
				break;
				
			case "motherboard" :
				setResponsePage(new MotherBoardDetailPage(product));
				break;
				
			case "hard_disk" :
				setResponsePage(new HarddiskDetailPage(product));
				break;
				
			case "power_supply_unit" :
				setResponsePage(new PowerSupplyUnitsDetailPage(product));
				break;
		}
	}
	
	/**
	 * 	Nastavení titulku stránky
	 * 
	 * 	@param model - název stránky 
	 */
	protected void setTitle(IModel model) {
		get("titleLabel").setDefaultModel(model);
	}
}
