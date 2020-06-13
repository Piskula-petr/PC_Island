package cz.pcisland.memory.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/*
 *	Třída detailu operační paměti:
 *
 *		nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí
 */
public class MemoryDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////////////////
	
	public MemoryDetailPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public MemoryDetailPage(Product memory) {
		
		super(memory);
		
		// Nastavení titulku
		setTitle(new Model(memory.getName() + " | Operační paměti (RAM)"));
		
// Parametry //////////////////////////////////////////////////////////////////////////////////////////
		
		markupContainer.add(new Label("company", memory.getParametrs().get("company")));
		markupContainer.add(new Label("series", memory.getParametrs().get("series")));
		markupContainer.add(new Label("memorySize", memory.getParametrs().get("memorySize") + " GB"));
		markupContainer.add(new Label("memoryType", memory.getParametrs().get("memoryType")));
		markupContainer.add(new Label("memoryFrequency", getDecimalFormat().format(Integer.parseInt(memory.getParametrs().get("memoryFrequency"))) + " MHz"));
		markupContainer.add(new Label("latency", memory.getParametrs().get("latency")));
		markupContainer.add(new Label("voltage", memory.getParametrs().get("voltage") + " V"));
		markupContainer.add(new Label("XMP", memory.getParametrs().get("XMP")));
		
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(memory.getName()));	
	}
	
}
