package cz.pcisland.review.overview.change;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.review.Review;
import cz.pcisland.review.ReviewDAO;
import cz.pcisland.review.ReviewDAOImpl;
import cz.pcisland.review.overview.ReviewOverview;

/**
 * 	Třída uprava uživatelské recenze:
 * 
 * 		nastavení titulku,
 * 		upravení recenze + uložení nové recenze
 */

public class ReviewOverviewChange extends BasePage {

	private static final long serialVersionUID = 1L;
	
	private String newPros;
	private String newCons;
	
	// Komponenty
	private TextArea<String> prosTextArea;
	private TextArea<String> consTextArea;
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public ReviewOverviewChange(Review review) {

		// Nastavení titulku
		setTitle(new Model("Úprava recenze"));
		
// Úprava recenze Form ////////////////////////////////////////////////////////////////////////////////
		
		Form reviewOverviewChangeForm = new Form("reviewOverviewChangeForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				ReviewDAO reviewDAO = new ReviewDAOImpl();
				
				// Klady
				if (prosTextArea.getModelObject() != null) {
					newPros = (String) prosTextArea.getModelObject();
					String[] prosPreviewArray = newPros.split("\r\n|\r|\n");
					newPros = "";
					
					for (int i = 0; i < prosPreviewArray.length; i++) {
						newPros = newPros + prosPreviewArray[i] + ";";
					}
					
					// Uložení + Změna stylu
					if (!newPros.equals(review.getPros())) {
						reviewDAO.changeReviewPros(review.getIdReview(), newPros);
						prosTextArea.add(new AttributeModifier("style", "border: 1px solid green"));
					
					} else prosTextArea.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
				
				// Zápory
				if (consTextArea.getModelObject() != null) {
					newCons = (String) consTextArea.getModelObject();
					String[] consPreviewArray = newCons.split("\r\n|\r|\n");
					newCons = "";
					
					for (int i = 0; i < consPreviewArray.length; i++) {
						newCons = newCons + consPreviewArray[i] + ";";
					}
					
					// Uložení + Změna stylu
					if (!newCons.equals(review.getCons())) {
						reviewDAO.changeReviewCons(review.getIdReview(), newCons);
						consTextArea.add(new AttributeModifier("style", "border: 1px solid green"));
					
					} else consTextArea.add(new AttributeModifier("style", "border: 1px solid #c2c2c2"));
				}
			}
		};
		add(reviewOverviewChangeForm);
		
		reviewOverviewChangeForm.add(new Link<Object>("nameLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku produktu
				getDetailPage(new ProductDAOImpl().getProductByName(review.getProductName()));
			}
		}.add(new Label("name", review.getProductName())));

		reviewOverviewChangeForm.add(new Label("reviewRating", review.getRating() + "%"));
		reviewOverviewChangeForm.add(new Label("userFullName", review.getUserFullName()));
		reviewOverviewChangeForm.add(new Label("creationDate", review.getCreationDate()));
		
		// Roložení pozitiv na řádky
		String pros = "";
		if (review.getPros() != null) {
			pros = review.getPros().replaceAll(";", "\n");
		}
		
		prosTextArea = new TextArea<String>("prosTextArea", Model.of(pros));
		reviewOverviewChangeForm.add(prosTextArea);
		
		// Rozložení negativ na řádky
		String cons = "";
		if (review.getCons() != null) {
			cons = review.getCons().replaceAll(";", "\n");
		}
		
		consTextArea = new TextArea<String>("consTextArea", Model.of(cons));
		reviewOverviewChangeForm.add(consTextArea);
		
// Zpět na přehled recenzí Link //////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("backToReviewOverviewLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na přehled recenzí
				setResponsePage(ReviewOverview.class);
			}
		});
	}
	
}
