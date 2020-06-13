package cz.pcisland.processors.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/**
 * 	Třída detailu procesoru:
 * 
 *  	nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí;
 */

public class ProcessorDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;
	
// Bezparametrový kontruktor ///////////////////////////////////////////////////////////////////////////
	
	public ProcessorDetailPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public ProcessorDetailPage(Product processor) {
		
		super(processor);
		
		// Nastavení titulku
		setTitle(new Model(processor.getName() + " | Procesory (CPU)"));
		
// Parametry ///////////////////////////////////////////////////////////////////////////////////////////
		
		markupContainer.add(new Label("company", processor.getParametrs().get("company")));
		markupContainer.add(new Label("series", processor.getParametrs().get("series")));
		markupContainer.add(new Label("codeName", processor.getParametrs().get("codeName")));
		markupContainer.add(new Label("socket", processor.getParametrs().get("socket")));
		markupContainer.add(new Label("numberOfCores", processor.getParametrs().get("numberOfCores")));
		markupContainer.add(new Label("numberOfThreads", processor.getParametrs().get("numberOfThreads")));
		markupContainer.add(new Label("workFrequency", processor.getParametrs().get("workFrequency") + " GHz"));
		markupContainer.add(new Label("turboFrequency", processor.getParametrs().get("turboFrequency") + " GHz"));
		markupContainer.add(new Label("cache", processor.getParametrs().get("cache") + " MB"));
		markupContainer.add(new Label("thermalDesignPower", processor.getParametrs().get("thermalDesignPower") + " W"));
		markupContainer.add(new Label("technology", processor.getParametrs().get("technology") + " nm"));
		markupContainer.add(new Label("chipset", processor.getParametrs().get("chipset")));
		
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(processor.getName()));
	}
	
}
