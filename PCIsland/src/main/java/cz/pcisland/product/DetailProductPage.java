package cz.pcisland.product;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RangeTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.cart.CartPage;
import cz.pcisland.graphics_cards.GraphicsCardDAO;
import cz.pcisland.graphics_cards.GraphicsCardDAOImpl;
import cz.pcisland.hard_disks.HardDiskDAO;
import cz.pcisland.hard_disks.HardDiskDAOImpl;
import cz.pcisland.login_and_registration.LoginAndRegistrationPage;
import cz.pcisland.memory.MemoryDAO;
import cz.pcisland.memory.MemoryDAOImpl;
import cz.pcisland.motherboards.MotherboardDAO;
import cz.pcisland.motherboards.MotherboardDAOImpl;
import cz.pcisland.power_supply_units.PowerSupplyUnitDAO;
import cz.pcisland.power_supply_units.PowerSupplyUnitDAOImpl;
import cz.pcisland.processors.ProcessorDAO;
import cz.pcisland.processors.ProcessorDAOImpl;
import cz.pcisland.review.Review;
import cz.pcisland.review.ReviewDAO;
import cz.pcisland.review.ReviewDAOImpl;
import cz.pcisland.review.overview.ReviewOverview;
import cz.pcisland.review.overview.change.ReviewOverviewChange;
import cz.pcisland.user.User;

/**
 * 	Mateřská třída detailu produktu:
 * 
 * 		informace o produktu (název, cena, počet kusů ...),
 * 		galerie produktu,
 * 		seznam uživatelských recenzí (informace o recenzi, klady, zápory),
 * 		vytvoření nové recenze (jméno klady, zápory hodnocení)
 * 
 * 	Metody:
 * 
 *  	zvýšení počtu recenzí + přičtení počtu recenzí
 */

public abstract class DetailProductPage extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Atributy a komponenty pro potomka třídy
	protected WebMarkupContainer markupContainer;
	protected ListView<Review> reviewsListView;
	
	// Parametry recenze
	private Integer idUser;
	private String userFullNameReview;
	private String prosReview;
	private String consReview;
	private Integer ratingReview;
	
	private List<Product> cartItems = new ArrayList<>();
	
	// Komponenty
	private Label noReview;
	private TextField<String> userFullNameReviewTextField;
	private TextArea<String> prosReviewTextArea;
	private TextArea<String> consReviewTextArea;
	private AjaxLink<Object> newReviewAjaxLink;
	private Button submitReviewButton;
	private RangeTextField<Integer> ratingReviewRangeTextField;
	
// Bezparametrový kontruktor //////////////////////////////////////////////////////////////////////////
	
	public DetailProductPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public DetailProductPage(Product product) {
		
// WebMarkupContainer /////////////////////////////////////////////////////////////////////////////////
		
		markupContainer = new WebMarkupContainer("markupContainer");
		markupContainer.setOutputMarkupId(true);
		add(markupContainer);
		
		String rating = "";
		if (product.getNumberOfPreview() > 0) {
			rating = "Hodnocení " + product.getOverallRating() / product.getNumberOfPreview() 
			+ "% ze " + product.getNumberOfPreview() + " recenzí";
			
			if (product.getNumberOfPreview() == 1) {
				rating = "Hodnocení " + product.getOverallRating() / product.getNumberOfPreview() 
				+ "% z " + product.getNumberOfPreview() + " recenze";
			}
		}
		
		String imagePath1 = "detail images//" + product.getName() + "//" + product.getName() + "_1.jpg";
		
		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		
		int DPH = (product.getPrice() * 21) / 121; 
		int priceWithoutDPH = product.getPrice() - DPH;
		
		String stock = "";
		if (product.getStock() > 4) {
			stock = "Skladem " + product.getStock() + " kusů";
		} else if (product.getStock() == 1) {
			stock = "Poslední kus";
		} else if (product.getStock() > 1 && product.getStock() < 5) {
			stock = "Skladem " + product.getStock() + " kusy";
		} else if (product.getStock() <= 0) {
			stock = "Není Skladem";
		} 
		
		markupContainer.add(new Label("name", product.getName()));
		markupContainer.add(new Label("rating", rating));
		markupContainer.add(new Image("mainImage", new ContextRelativeResource(imagePath1)));
		markupContainer.add(new Label("price", decimalFormat.format(product.getPrice()) + ",-"));
		markupContainer.add(new Label("priceWithoutDPH", decimalFormat.format(priceWithoutDPH) + ",- bez DPH"));
		markupContainer.add(new Label("stock", stock) {
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				// Změna třídy podle počtu produktů
				if (product.getStock() <= 0) {
					tag.put("class", "outStock");
				} else tag.put("class", "onStock");
			}
		});
		
		Link buyLink = new Link<Object>("buyLink") {
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				
				// Změna stylu Linku, po přidání produktu do košíku
				WebSession session = WebSession.get();
				if (session.getAttribute("cartList") != null) {
					
					for (Product productFromCart : (List<Product>) session.getAttribute("cartList")) {
						
						if (productFromCart.getName().equals(product.getName())) {
							tag.put("style", "background: green; padding: 10px 55px 14px 55px;");
						}
					}
				}
			}
			
			@Override
			public void onClick() {
				
				// Přidání produktu do košíku
				WebSession webSession = WebSession.get();
				product.setAmount(1);
				
				if (webSession.getAttribute("cartList") != null) {
					cartItems = (List<Product>) webSession.getAttribute("cartList");
				}
				cartItems.add(product);
				webSession.setAttribute("cartList", (Serializable) cartItems);
				
				// Uložení ceny produktu
				if (webSession.getAttribute("cartPrice") != null) {
					int currentPrice = (int)webSession.getAttribute("cartPrice");
					currentPrice = currentPrice + product.getPrice();
					webSession.setAttribute("cartPrice", currentPrice);
				
				} else webSession.setAttribute("cartPrice", product.getPrice());
				
				// Uložení velikosti košíku
				webSession.setAttribute("cartSize", cartItems.size());
			}
		};
		markupContainer.add(buyLink);
		
		buyLink.add(new Label("buyLabel", "Koupit") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Ověří jestli je položka v košíku
				WebSession webSession = WebSession.get();
				if (webSession.getAttribute("cartList") != null) {
					
					boolean itemInCart = false;
					for (Product productFromCart : (List<Product>) webSession.getAttribute("cartList")) {
						
						if (productFromCart.getName().equals(product.getName())) {
							itemInCart = true;
						}
					} 
					
					// Změna popisku + znemožnění opětovného vložení produktu do košíku
					if (itemInCart) {
						setDefaultModelObject("V košíku");
						buyLink.setEnabled(false);
					
					} else {
						setDefaultModelObject("Koupit");
						buyLink.setEnabled(true);
					}
				}
			}
		});
		
		markupContainer.add(new Label("description", product.getDescription(product.getType(), product.getParametrs())));
		
// Galerie produktu ///////////////////////////////////////////////////////////////////////////////////
		
		markupContainer.add(new Image("image1", new ContextRelativeResource(imagePath1)));
		
		String imagePath2 = "detail images//" + product.getName() + "//" + product.getName() + "_2.jpg";
		markupContainer.add(new Image("image2", new ContextRelativeResource(imagePath2)));
		
		String imagePath3 = "detail images//" + product.getName() + "//" + product.getName() + "_3.jpg";
		markupContainer.add(new Image("image3", new ContextRelativeResource(imagePath3)));
		
// Uživatelské recenze ListView ///////////////////////////////////////////////////////////////////////
		
		reviewsListView = new ListView<Review>("reviewsListView") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Zobrazení zprávy pokud nejsou k produktu žádné recenze
				if (getList().size() == 0) {
					noReview.setVisible(true);
				} else noReview.setVisible(false);
			}
			
			@Override
			protected void populateItem(ListItem<Review> item) {
				
				Review review = item.getModelObject();
				item.add(new Label("underline") {
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						
						// Podtrhnutí každé recenze
						if (item.getIndex() > 0) {
							tag.put("style", "border-top: 1px solid #c2c2c2");
						}
					}
				});
				
				item.add(new Label("reviewRating", review.getRating() + "%"));
				item.add(new Label("userFullName", review.getUserFullName()));
				item.add(new Label("creationDate", review.getCreationDate()));
				
// Klady ListView /////////////////////////////////////////////////////////////////////////////////////
				
				List<String> prosList = new ArrayList<>();
				
				if (review.getPros() != null) {
					prosList = Arrays.asList(review.getPros().split(";"));
				}
				
				item.add(new ListView<String>("prosListView", prosList) {
					
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("pros", item.getDefaultModel()));
					}
				});
				
// Zápory ListView ////////////////////////////////////////////////////////////////////////////////////
				
				List<String> consList = new ArrayList<>();
				
				if (review.getCons() != null) {
					consList = Arrays.asList(review.getCons().split(";"));
				}
				
				item.add(new ListView<String>("consListView", consList) {
					
					@Override
					protected void populateItem(ListItem<String> item) {
						item.add(new Label("cons", item.getDefaultModel()));
					}
				});
			}
		};
		markupContainer.add(reviewsListView);
		
		// Žádná recenze Label
		noReview = new Label("noReview", "K tomuto produktu doposud není žádná uživatelská recenze.");
		markupContainer.add(noReview);
		
// Nová recenze Form //////////////////////////////////////////////////////////////////////////////////
		
		Form newReviewForm = new Form("newReviewForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				if (prosReview != null) {
					String[] prosPreviewArray = prosReview.split("\r\n|\r|\n");
					prosReview = "";
					
					for (int i = 0; i < prosPreviewArray.length; i++) {
						prosReview = prosReview + prosPreviewArray[i] + ";";
					}
				}
				
				if (consReview != null) {
					String[] consPreviewArray = consReview.split("\r\n|\r|\n");
					consReview = "";
					
					for (int i = 0; i < consPreviewArray.length; i++) {
						consReview = consReview + consPreviewArray[i] + ";";
					}
				}
				
				// Nastavení parametrů recenze
				Review review = new Review();
				review.setIdUser(idUser);
				review.setUserFullName(userFullNameReview);
				review.setProductName(product.getName());
				review.setPros(prosReview);
				review.setCons(consReview);
				review.setRating(ratingReview);
				LocalDate dateNow = LocalDate.now();
				review.setCreationDate(dateNow);
				
				// Uložení recenze
				ReviewDAO reviewDAO = new ReviewDAOImpl();
				reviewDAO.saveReview(review);
				
				// Zvýšení počtu recenzí + přičtení hodnocení k celkovému hodnocení
				saveRating(product);
			}
		};
		newReviewForm.setOutputMarkupId(true);
		markupContainer.add(newReviewForm);
				
// Nakonfigurování komponent //////////////////////////////////////////////////////////////////////////
		
		userFullNameReviewTextField = new TextField("userFullNameReviewTextField", new PropertyModel<String>(this, "userFullNameReview"));
		
		// Přednastaví jméno uživatele
		if (AuthenticatedWebSession.get().getAttribute("currentUser") != null) {
			User user = (User) AuthenticatedWebSession.get().getAttribute("currentUser");
			userFullNameReviewTextField.setDefaultModelObject(user.getName() + " " + user.getSurname());
			idUser = user.getId();
		}
		
		userFullNameReviewTextField.add(new OnChangeAjaxBehavior() {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(submitReviewButton);
			}
		});
		newReviewForm.add(userFullNameReviewTextField);
		
		prosReviewTextArea = new TextArea("prosReviewTextArea", new PropertyModel<String>(this, "prosReview"));
		newReviewForm.add(prosReviewTextArea);
		
		consReviewTextArea = new TextArea("consReviewTextArea", new PropertyModel<String>(this, "consReview"));
		newReviewForm.add(consReviewTextArea);
		
		ratingReviewRangeTextField = new RangeTextField<Integer>("ratingReviewRangeTextField", new PropertyModel<Integer>(this, "ratingReview"));
		ratingReviewRangeTextField.setStep(10);
		ratingReviewRangeTextField.setMinimum(0);
		ratingReviewRangeTextField.setMaximum(100);
		newReviewForm.add(ratingReviewRangeTextField);
		
		newReviewAjaxLink = new AjaxLink<Object>("newReviewLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				// Znemožnění napsat více recenzí jednomu produktu
				for (Review review : new ReviewDAOImpl().getProductNotNullReviews(product.getName())) {
					
					// Přesměrování na úpravu recenze
					if (review.getIdUser() == idUser) {
						setResponsePage(new ReviewOverviewChange(review));
					}
					
					// Přesměrování na přehled recenzí pokud je přihlášen Admin
					if (idUser == 1) {
						setResponsePage(ReviewOverview.class);
					}
				}
				
				// Přesměrování pokud není uživatel přihlášen, jinak spustí script s oknem recenze
				if (AuthenticatedWebSession.get().getAttribute("currentUser") == null) {
					setResponsePage(LoginAndRegistrationPage.class);
				
				} else target.appendJavaScript("document.getElementById('reviewFrame').style.display='block'");;
			}
		};
		markupContainer.add(newReviewAjaxLink);
		
		submitReviewButton = new Button("submitReviewButton") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Zneviditelní tlačítko "Odeslat" při nezadaném jménu
				if (userFullNameReview != null) {
					setVisible(true);
				} else {
					setVisible(false);
				}
			}
		};
		submitReviewButton.setOutputMarkupPlaceholderTag(true);
		submitReviewButton.setOutputMarkupId(true);
		newReviewForm.add(submitReviewButton);
	}
	
// Metody /////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 	Zvýšení počtu recenzí + přičtení hodnocení k celkovému hodnocení
	 * 
	 * @param product - product
	 */
	private void saveRating(Product product) {
		
		switch(product.getType()) {
		case "processor" :
			ProcessorDAO processorDAO = new ProcessorDAOImpl();
			processorDAO.incrementNumberOfPreview(product.getId());
			processorDAO.addRating(product.getId(), ratingReview);
			break;
			
		case "graphics_card" :
			GraphicsCardDAO graphicsCardDAO = new GraphicsCardDAOImpl();
			graphicsCardDAO.incrementNumberOfPreview(product.getId());
			graphicsCardDAO.addRating(product.getId(), ratingReview);
			break;
			
		case "memory" :
			MemoryDAO memoryDAO = new MemoryDAOImpl();
			memoryDAO.incrementNumberOfPreview(product.getId());
			memoryDAO.addRating(product.getId(), ratingReview);
			break;
			
		case "motherboard" :
			MotherboardDAO motherboardDAO = new MotherboardDAOImpl();
			motherboardDAO.incrementNumberOfPreview(product.getId());
			motherboardDAO.addRating(product.getId(), ratingReview);
			break;
			
		case "hard_disk" :
			HardDiskDAO hardDiskDAO = new HardDiskDAOImpl();
			hardDiskDAO.incrementNumberOfPreview(product.getId());
			hardDiskDAO.addRating(product.getId(), ratingReview);
			break;
			
		case "power_supply_unit" :
			PowerSupplyUnitDAO powerSupplyUnitDAO = new PowerSupplyUnitDAOImpl();
			powerSupplyUnitDAO.incrementNumberOfPreview(product.getId());
			powerSupplyUnitDAO.addRating(product.getId(), ratingReview);
			break;
		}
	}
	
}
