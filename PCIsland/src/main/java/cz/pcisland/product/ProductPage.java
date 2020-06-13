package cz.pcisland.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.base_page.CustomPagingNavigator;

/**
 * 	Mateřská třída produktu:
 * 
 * 		list produktů,
 * 		třídící filtr (nejprodávanější, nejdražší ...)
 * 
 * 	Metody:
 *  	
 *  	setřízení Listu podle stisknutého Linku,
 *  	nastavené cenového rozpětí
 */
public abstract class ProductPage extends BasePage {

	private static final long serialVersionUID = 1L;

	// Atributy a komponenty pro potomka třídy
	protected WebMarkupContainer markupContainer;
	protected PageableListView<Product> productsPageableListView;
	protected List<Product> products = new ArrayList<>();
	
	// Třídící filtr
	private Boolean topSelling = true;
	private Boolean mostExpensive = false;
	private Boolean cheapest = false;
	private Boolean bestRated = false;
	
	private List<Product> cartItems = new ArrayList<>();
	private String itemsAmount;
	
	// Komponenty
	private AjaxLink<Object> topSellingLink, mostExpensiveLink, cheapestLink, bestRatedLink;
	
// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public ProductPage() {
		
// WebMarkupContainer ////////////////////////////////////////////////////////////////////////////////
		
		markupContainer = new WebMarkupContainer("markupContainer");
		markupContainer.setOutputMarkupId(true);
		add(markupContainer);
		
// Produkt PageableListView //////////////////////////////////////////////////////////////////////////
		
		productsPageableListView = new PageableListView<Product>("productsPageableListView", new ProductDAOImpl().getAllProducts(), 10) {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Nejprodávanější - změna třídy
				if (topSelling) {
					topSellingLink.add(AttributeModifier.append("class", "active"));
					
					// Setřízení a nastavení Listu
					setList(sortList(products));
				
				} else if (!topSelling) {
					
					topSellingLink.add(AttributeModifier.remove("class"));
				}
				
				// Nejdražší - změna třídy
				if (mostExpensive) {
					
					mostExpensiveLink.add(AttributeModifier.append("class", "active"));
					topSellingLink.add(AttributeModifier.append("class", "nextToActive"));
					
					// Setřízení a nastavení Listu
					setList(sortList(products));
				
				} else if (!mostExpensive) {
					
					mostExpensiveLink.add(AttributeModifier.remove("class"));
				}
				
				// Nejlevnější - změna třídy
				if (cheapest) {
					
					cheapestLink.add(AttributeModifier.append("class", "active"));
					mostExpensiveLink.add(AttributeModifier.append("class", "nextToActive"));
					
					// Setřízení a nastavení Listu
					setList(sortList(products));
				
				} else if (!cheapest) {
					
					cheapestLink.add(AttributeModifier.remove("class"));
				}
				
				// nejlépe hodnocené - změna třídy
				if (bestRated) {
					
					bestRatedLink.add(AttributeModifier.append("class", "active"));
					cheapestLink.add(AttributeModifier.append("class", "nextToActive"));
					
					// Setřízení a nastavení Listu
					setList(sortList(products));
				
				} else if (!bestRated) {
					
					bestRatedLink.add(AttributeModifier.remove("class"));
				}
				
				// Nastavení množství položek
				itemsAmount = "";
				itemsAmount = String.valueOf(productsPageableListView.getList().size());
				
				if (itemsAmount.equals("1")) {
					itemsAmount = itemsAmount + " položka";
				
				} else if (itemsAmount.equals("2") || itemsAmount.equals("3") || itemsAmount.equals("4")) {
					itemsAmount = itemsAmount + " položky";
				
				} else itemsAmount = itemsAmount + " položek";
			}
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				String rating = "";
				if (product.getNumberOfPreview() > 0) {
					rating = "Hodnocení " + product.getOverallRating() / product.getNumberOfPreview() + "%";
				}
				
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
				
				item.add(new Link<Object>("imageLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Image("image", new ContextRelativeResource(imagePath))));
				
				item.add(new Label("rating", rating));
				item.add(new Label("price", getDecimalFormat().format(product.getPrice()) + ",-"));
				item.add(new Label("priceWithoutDPH", getDecimalFormat().format(priceWithoutDPH) + ",- bez DPH"));
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
				
				Link buyLink = new Link<Object>("buyLink") {
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						
						// Změna stylu Linku, po přidání produktu do košíku
						WebSession webSession = WebSession.get();
						if (webSession.getAttribute("cartList") != null) {
							
							for (Product productFromCart : (List<Product>) webSession.getAttribute("cartList")) {
								
								if (productFromCart.getName().equals(product.getName())) {
									tag.put("style", "background: green; padding: 10px 27px 14px 27px;");
								}
							}
						}
					}
					
					@Override
					public void onClick() {
						
						// Přidání produktu do košíku
						WebSession webSession = WebSession.get();
						product.setAmount(1);
						
						// Uložení produktu do Listu
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
				
				item.add(buyLink);
				
				buyLink.add(new Label("buyLabel", "Koupit") {
					
					@Override
					protected void onConfigure() {
						super.onConfigure();
						
						// Změna popisku + znemožnění opětovného vložení produktu do košíku
						WebSession webSession = WebSession.get();
						if (webSession.getAttribute("cartList") != null) {
							
							for (Product productFromCart : (List<Product>) webSession.getAttribute("cartList")) {
								
								if (productFromCart.getName().equals(product.getName())) {
									
									setDefaultModelObject("V košíku");
									buyLink.setEnabled(false);
								}
							}
						}
					}
				});
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("name", product.getName())));
				
				item.add(new Label("description", product.getDescription(product.getType(), product.getParametrs())));
			}
		};
		
		productsPageableListView.setOutputMarkupId(true);
		markupContainer.add(productsPageableListView);
		markupContainer.add(new CustomPagingNavigator("pagingNavigator", productsPageableListView));
		
		// Počet položek
		markupContainer.add(new Label("itemsAmount", new PropertyModel<String>(this, "itemsAmount")));
		
// Nejprodávanější Link //////////////////////////////////////////////////////////////////////////////

		topSellingLink = new AjaxLink<Object>("topSellingLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				topSelling = true;
				mostExpensive = false;
				cheapest = false;
				bestRated = false;
				
				// Setřízení a nastavení Listu podle aktivního Linku
				productsPageableListView.setList(sortList(products));

				target.add(markupContainer);
			}
		};
		
		markupContainer.add(topSellingLink);
		
// Nejdražší Link ////////////////////////////////////////////////////////////////////////////////////
		
		mostExpensiveLink = new AjaxLink<Object>("mostExpensiveLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				topSelling = false;
				mostExpensive = true;
				cheapest = false;
				bestRated = false;
				
				// Setřízení a nastavení Listu podle aktivního Linku
				productsPageableListView.setList(sortList(products));

				target.add(markupContainer);
			}
		};
		
		markupContainer.add(mostExpensiveLink);
		
// Nejlevnější Link //////////////////////////////////////////////////////////////////////////////////
		
		cheapestLink = new AjaxLink<Object>("cheapestLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				topSelling = false;
				mostExpensive = false;
				cheapest = true;
				bestRated = false;

				// Setřízení a nastavení Listu podle aktivního Linku
				productsPageableListView.setList(sortList(products));
				
				target.add(markupContainer);
			}
		};
		
		markupContainer.add(cheapestLink);
		
// Nejlépe hodnocené Link ////////////////////////////////////////////////////////////////////////////
		
		bestRatedLink = new AjaxLink<Object>("bestRatedLink") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				
				topSelling = false;
				mostExpensive = false;
				cheapest = false;
				bestRated = true;
			
				// Setřízení a nastavení Listu podle aktivního Linku
				productsPageableListView.setList(sortList(products));
				
				target.add(markupContainer);
			}
		};
		
		markupContainer.add(bestRatedLink);
	}

// Metody ////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 	Setřízení Listu podle stisknutého Linku
	 * 
	 * 	@param products - List produktů
	 * 	@return - setřízený List produktů
	 */
	public List<Product> sortList(List<Product> products) {
		
		if (topSelling) {
			products.sort(Comparator.comparing(Product::getSales).reversed());
		
		} else if (mostExpensive) {
			products.sort(Comparator.comparing(Product::getPrice).reversed());
		
		} else if (cheapest) {
			products.sort(Comparator.comparing(Product::getPrice));
		
		} else if (bestRated) {
			Collections.sort(products, new Comparator<Product>() {
				
				@Override
				public int compare(Product product1, Product product2) {
					
					int rating1 = 0;
					if (product1.getNumberOfPreview() > 0) {
						rating1 = product1.getOverallRating() / product1.getNumberOfPreview();
					}
					
					int rating2 = 0;
					if (product2.getNumberOfPreview() > 0) {
						rating2 = product2.getOverallRating() / product2.getNumberOfPreview();
					}
					
					return Integer.compare(rating1, rating2);
				}
			}.reversed());
		}
		
		return products;
	}

	/**
	 * 	Nastavení cenového rozpětí
	 * 
	 * @param products - List produktů mimo cenové rozpětí
	 * @param inputMinPrice - nová minimální cena zadaná uživatelem
	 * @param inputMaxPrice - nová maximální cena zadaná uživatelem
	 * @param MIN_PRICE - minimální cena produktu
	 * @param MAX_PRICE - maximální cena produktu
	 * @return List produktů v cenovém rozpětí
	 */
	public List<Product> getPriceRange(List<Product> products, int inputMinPrice, int inputMaxPrice, int MIN_PRICE, int MAX_PRICE) {
		
		// Pomocný List pro uchovávání mezivýsledků
		List<Product> processorOutsidePriceRange = new ArrayList<>();
		
		// Ochrana minimální a maximální ceny
		if (inputMinPrice < MIN_PRICE) {
			inputMinPrice = MIN_PRICE;
		
		} else if (inputMinPrice > MAX_PRICE) {
			inputMinPrice = MAX_PRICE;
		
		} else if (inputMaxPrice < inputMinPrice) {
			inputMaxPrice = inputMinPrice;
		
		} else if (inputMaxPrice > MAX_PRICE) {
			inputMaxPrice = MAX_PRICE;
		}
		
		// Odstranění produktů mimo cenové rozpětí
		for (Product product : products) {
			
			if (product.getPrice() < inputMinPrice || product.getPrice() > inputMaxPrice) {
				processorOutsidePriceRange.add(product);
			}
		}
		products.removeAll(processorOutsidePriceRange);
		
		return products;
	}
	
}
