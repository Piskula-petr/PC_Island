package cz.pcisland.review.overview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.base_page.CustomPagingNavigator;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.review.Review;
import cz.pcisland.review.ReviewDAOImpl;
import cz.pcisland.review.overview.change.ReviewOverviewChange;
import cz.pcisland.user.User;

/**
 * 	Třída přehledu uživatelských recenzí:
 * 
 * 		nastavení titulku,
 * 		list produktů s nenulovými recenzemi,
 * 		list recenzí k produktu
 */

public class ReviewOverview extends BasePage {

	private static final long serialVersionUID = 1L;

	private User user = (User) AuthenticatedWebSession.get().getAttribute("currentUser");
	
	// Komponenty
	private PageableListView<Review> reviewedProductsPageableListView;
	private ListView<Review> productReviewListView;
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public ReviewOverview() {
		
		// Nastavení titulku
		setTitle(new Model("Přehled recenzí"));
		
// Recenze WebMarkupContainer ////////////////////////////////////////////////////////////////////////
		
		WebMarkupContainer reviewContainer = new WebMarkupContainer("reviewContainer");
		reviewContainer.setOutputMarkupId(true);
		add(reviewContainer);
		
// Recenzované produkty PageableListView /////////////////////////////////////////////////////////////////////////////
		
		reviewedProductsPageableListView = new PageableListView<Review>("reviewedProductsPageableListView", new ReviewDAOImpl().getUsersNotNullReviews(user.getId()), 10) {
			
			@Override
			protected void populateItem(ListItem<Review> item) {
				
				Review review = item.getModelObject();
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(new ProductDAOImpl().getProductByName(review.getProductName()));
					}
				}.add(new Label("name", review.getProductName())));
				
// Recenze produktu ListView //////////////////////////////////////////////////////////////////////////
				
				productReviewListView = new ListView<Review>("productReviewListView", new ReviewDAOImpl().getProductNotNullReviewsFromUser(review.getProductName(), user.getId())) {
					
					@Override
					protected void populateItem(ListItem<Review> item) {
						
						Review review = item.getModelObject();
						item.add(new Label("underline") {
							
							@Override
							protected void onComponentTag(ComponentTag tag) {
								super.onComponentTag(tag);
								
								// Podtrhnutí každé recenze
								tag.put("style", "border-top: 1px solid #c2c2c2");
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
						
						item.add(new Link<Object>("editLink") {
							
							@Override
							public void onClick() {
								
								// Přesměrování na změnu recenze
								setResponsePage(new ReviewOverviewChange(review));
							}
						});
					}
				};
				item.add(productReviewListView);
				
				// Zobrazení všech nenulových recenzí produktu pokud je přihlášený Admin
				if (user.getEmail().equals("admin@pcisland.cz")) {
					productReviewListView.setList(new ReviewDAOImpl().getProductNotNullReviews(review.getProductName()));
				}
			}
		};
		reviewContainer.add(reviewedProductsPageableListView);
		add(new CustomPagingNavigator("pagingNavigator", reviewedProductsPageableListView));

		// Zobrazení všech nenulových recenzí pokud je příhlášený Admin
		if (user.getEmail().equals("admin@pcisland.cz")) {
			reviewedProductsPageableListView.setList(new ReviewDAOImpl().getAllNotNullReviews());
		}
	}
	
}
