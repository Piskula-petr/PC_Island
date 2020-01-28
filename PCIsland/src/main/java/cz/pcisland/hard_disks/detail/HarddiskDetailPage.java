package cz.pcisland.hard_disks.detail;

import java.text.DecimalFormat;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import cz.pcisland.cart.CartPage;
import cz.pcisland.product.DetailProductPage;
import cz.pcisland.product.Product;
import cz.pcisland.review.ReviewDAOImpl;

/**
 *	Třída detailu pevného disku:
 *
 *		nastavení titulku,
 *		parametry produktu,
 *		přiřazení uživatelských recenzí
 */

public class HarddiskDetailPage extends DetailProductPage {

	private static final long serialVersionUID = 1L;

// Bezparametrový konstruktor //////////////////////////////////////////////////////////////////////////
	
	public HarddiskDetailPage() {
		setResponsePage(CartPage.class);
	}
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////	
	
	public HarddiskDetailPage(Product hardDisk) {
		
		super(hardDisk);
		
		// Nastavení titulku
		setTitle(new Model(hardDisk.getName() + " | Pevné disky (HDD)"));

// Parametry ///////////////////////////////////////////////////////////////////////////////////////////
		
		String pattern = "###,###.###";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		
		// Úpravení formátu disku
		String format = ""; 
		if (hardDisk.getParametrs().get("format").equals("2.5") || hardDisk.getParametrs().get("format").equals("3.5")) {
			format = "\"";
		}
		
		// Úpravení velikosti disku
		String size = "";
		if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 1000) {
			size = Integer.parseInt(hardDisk.getParametrs().get("size")) / 1000 + " TB";
		
		} else size = (hardDisk.getParametrs().get("size")) + " GB";
		
		markupContainer.add(new Label("company", hardDisk.getParametrs().get("company")));
		markupContainer.add(new Label("type", hardDisk.getParametrs().get("type")));
		markupContainer.add(new Label("format", hardDisk.getParametrs().get("format") + format));
		markupContainer.add(new Label("size", size));
		
		// Editace HTML tagu (přidá parametr otáček u magnetických disků)
		if (Integer.parseInt(hardDisk.getParametrs().get("RMP")) > 0) {
			markupContainer.add(new Label("RPMName", "<p>Rychlost otáčení ploten:</p>").setEscapeModelStrings(false));
			markupContainer.add(new Label("RPMValue", "<p>" + decimalFormat.format(Integer.parseInt(hardDisk.getParametrs().get("RMP"))) + " otáček/min</p>").setEscapeModelStrings(false));
		
		} else {
			markupContainer.add(new Label("RPMName", "").setEscapeModelStrings(false));
			markupContainer.add(new Label("RPMValue", "").setEscapeModelStrings(false));
		}
		
		markupContainer.add(new Label("readingSpeed", decimalFormat.format(Integer.parseInt(hardDisk.getParametrs().get("readingSpeed"))) + " MB/s"));
		markupContainer.add(new Label("writeSpeed", decimalFormat.format(Integer.parseInt(hardDisk.getParametrs().get("writeSpeed"))) + " MB/s"));
		markupContainer.add(new Label("connector", hardDisk.getParametrs().get("connector")));
		
		// Uživatelské recenze ListView (DetailProductPage)
		reviewsListView.setList(new ReviewDAOImpl().getProductNotNullReviews(hardDisk.getName()));
	}	
	
}
