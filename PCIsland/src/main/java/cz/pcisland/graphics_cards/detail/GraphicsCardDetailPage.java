package cz.pcisland.graphics_cards.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/**
 *	Třída detailu grafické karty obsahuje:
 *
 *		nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí
 */
public class GraphicsCardDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////////////////
	
	public GraphicsCardDetailPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public GraphicsCardDetailPage(Product graphicsCard) {
		
		super(graphicsCard);
		
		// Nastavení titulku
		setTitle(new Model(graphicsCard.getName() + " | Grafické karty (GPU)"));
		
// Parametry //////////////////////////////////////////////////////////////////////////////////////////
		
		markupContainer.add(new Label("graphicsChip", graphicsCard.getParametrs().get("graphicsChip")));
		markupContainer.add(new Label("series", graphicsCard.getParametrs().get("series")));
		markupContainer.add(new Label("coreFrequency", getDecimalFormat().format(Integer.parseInt(graphicsCard.getParametrs().get("coreFrequency"))) + " MHz"));
		markupContainer.add(new Label("streamProcess", getDecimalFormat().format(Integer.parseInt(graphicsCard.getParametrs().get("streamProcess"))) + " x"));
		markupContainer.add(new Label("memoryType", graphicsCard.getParametrs().get("memoryType")));
		markupContainer.add(new Label("memorySize", getDecimalFormat().format(Integer.parseInt(graphicsCard.getParametrs().get("memorySize"))) + " MB"));
		markupContainer.add(new Label("memoryFrequency", graphicsCard.getParametrs().get("memoryFrequency") + " GHz"));
		markupContainer.add(new Label("memoryWidth", getDecimalFormat().format(Integer.parseInt(graphicsCard.getParametrs().get("memoryWidth"))) + " bit"));
		markupContainer.add(new Label("connector", graphicsCard.getParametrs().get("connector")));
		markupContainer.add(new Label("thermalDesignPower", graphicsCard.getParametrs().get("thermalDesignPower") + " W"));
		markupContainer.add(new Label("outputs", graphicsCard.getParametrs().get("outputs")));
	
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(graphicsCard.getName()));	
	}
	
}
