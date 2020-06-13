package cz.pcisland.power_supply_units.detail;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.power_supply_units.PowerSupplyUnitsPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/**
 *	Třída detailu zdrojů:
 *
 *		nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí
 */
public class PowerSupplyUnitsDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;
	
// Bezparametrový konstruktor /////////////////////////////////////////////////////////////////////////
	
	public PowerSupplyUnitsDetailPage() {
		setResponsePage(PowerSupplyUnitsPage.class);
	}
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public PowerSupplyUnitsDetailPage(Product powerSupplyUnit) {
		
		super(powerSupplyUnit);
		
		// Nastaví titulek
		setTitle(new Model(powerSupplyUnit.getName() + " | Zdroje (PSU)"));
		
// Parametry //////////////////////////////////////////////////////////////////////////////////////////
		
		// korekce html tagu (odřádkuje pokud je řetězec příliš dlouhý)
		int addRow = powerSupplyUnit.getParametrs().get("connectors").length() / 37;
		String htmlTag = "";
		
		for (int i = 0; i <= addRow; i++) {
			htmlTag = htmlTag + "<br>";
		}
		markupContainer.add(new Label("htmlTag", htmlTag).setEscapeModelStrings(false));
		
		markupContainer.add(new Label("company", powerSupplyUnit.getParametrs().get("company")));
		markupContainer.add(new Label("type", powerSupplyUnit.getParametrs().get("type")));
		markupContainer.add(new Label("performance", getDecimalFormat().format(Integer.parseInt(powerSupplyUnit.getParametrs().get("performance"))) + " W"));
		markupContainer.add(new Label("efficiency", powerSupplyUnit.getParametrs().get("efficiency")));
		markupContainer.add(new Label("fanSize", powerSupplyUnit.getParametrs().get("fanSize") + " mm"));
		markupContainer.add(new Label("connectors", powerSupplyUnit.getParametrs().get("connectors")));
		
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(powerSupplyUnit.getName()));
	}
	
}
