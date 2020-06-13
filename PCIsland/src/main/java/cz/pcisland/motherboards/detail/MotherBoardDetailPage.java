package cz.pcisland.motherboards.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/**
 *	Třída detailu základní desky:
 *
 *		nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí
 */

public class MotherBoardDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;

// Bezparametrový konstruktor //////////////////////////////////////////////////////////////////////////
	
	public MotherBoardDetailPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public MotherBoardDetailPage(Product motherboard) {
		
		super(motherboard);
		
		// Nastaví titulek
		setTitle(new Model(motherboard.getName() + " | Základní desky (Motherboards)"));
		
// Parametry ///////////////////////////////////////////////////////////////////////////////////////////

		markupContainer.add(new Label("company", motherboard.getParametrs().get("company")));
		markupContainer.add(new Label("socket", motherboard.getParametrs().get("socket")));
		
		// korekce html tagu (odřádkuje pokud je řetězec příliš dlouhý)
		String processorGenerationHtmlTag = "";
		if (motherboard.getParametrs().get("processorGeneration").length() > 40) {
			processorGenerationHtmlTag = "<br><br>";
		}

		markupContainer.add(new Label("processorGenerationHtmlTag", processorGenerationHtmlTag).setEscapeModelStrings(false));
		markupContainer.add(new Label("processorGeneration", motherboard.getParametrs().get("processorGeneration")));
		markupContainer.add(new Label("chipset", motherboard.getParametrs().get("chipset")));
		markupContainer.add(new Label("memoryType", motherboard.getParametrs().get("memoryType")));
		markupContainer.add(new Label("maxMemoryFrequency", getDecimalFormat().format(Integer.parseInt(motherboard.getParametrs().get("maxMemoryFrequency"))) + " MHz"));
		markupContainer.add(new Label("maxMemory", motherboard.getParametrs().get("maxMmemory") + " GB"));
		markupContainer.add(new Label("format", motherboard.getParametrs().get("format")));
		
		// korekce html tagu (odřádkuje pokud je řetězec příliš dlouhý)
		int addRow = motherboard.getParametrs().get("connectors").length() / 40;
		String connectorsHtmlTag = "";
		for (int i = 0; i <= addRow; i++) {
			connectorsHtmlTag = connectorsHtmlTag + "<br>";
		}

		markupContainer.add(new Label("connectorsHtmlTag", connectorsHtmlTag).setEscapeModelStrings(false));
		markupContainer.add(new Label("connectors", motherboard.getParametrs().get("connectors")));
		
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(motherboard.getName()));		
	}
	
}
