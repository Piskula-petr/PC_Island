package cz.pcisland.product.price_change;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.home_page.CustomPagingNavigator;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductDAOImpl;

/**
 *	 Třída změny ceny produkty (Admin only):
 *
 *		nastavení titulku,
 *		filtr (procesory, grafické karty ...),
 *		konfigurace komponenty filtru,
 *		list produktů
 */

public class ProductPriceChange extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean processors = false;
	private Boolean graphicsCards = false;
	private Boolean memory = false;
	private Boolean motherboards = false;
	private Boolean hardDisks = false;
	private Boolean powerSupplyUnits = false;
	
	private Integer idProduct;
	private Integer newPrice;
	private List<Product> products = new ArrayList<>();
	
	// Komponenty
	private PageableListView<Product> productsPriceChangePageableListView;
	private CustomPagingNavigator pagingNavigator;
	private NumberTextField<Integer> priceChangeNumberTextField;
	private Button submitButton;
	private Label processorsLabel, graphicsCardsLabel, memoryLabel, motherboardsLabel, hardDisksLabel, powerSupplyUnitsLabel;
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public ProductPriceChange() {
		
		// Nastavení titulku
		setTitle(new Model("Změna ceny"));
		
// Změna ceny zboží Form //////////////////////////////////////////////////////////////////////////////////////////
		
		Form productsPriceChangeForm = new Form("productsPriceChangeForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Změní cenu produktu
				new ProductDAOImpl().changePrice(idProduct, newPrice);
				setResponsePage(ProductPriceChange.class);
			}
		};
		productsPriceChangeForm.setOutputMarkupId(true);
		add(productsPriceChangeForm);
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Všechny produkty
				products = new ProductDAOImpl().getAllProducts();
				
// Typ produktu //////////////////////////////////////////////////////////////////////////////////////
			
				List<Product> filteredProducts = new ArrayList<>();
				
				if (processors && graphicsCards && memory && motherboards && hardDisks && powerSupplyUnits) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product product : products) {
						
						// Procesory
						if (processors) {
							
							if (product.getType().equals("processor")) {
								filteredProducts.add(product);
							}
						}
						
						// Grafické karty
						if (graphicsCards) {
							
							if (product.getType().equals("graphics_card")) {
								filteredProducts.add(product);
							}
						}
						
						// Operační paměti
						if (memory) {
							
							if (product.getType().equals("memory")) {
								filteredProducts.add(product);
							}
						}
						
						// Základní desky
						if (motherboards) {
							
							if (product.getType().equals("motherboard")) {
								filteredProducts.add(product);
							}
						}
						
						// Pevné disky
						if (hardDisks) {
							
							if (product.getType().equals("hard_disk")) {
								filteredProducts.add(product);
							}
						}
						
						// Zdroje
						if (powerSupplyUnits) {
							
							if (product.getType().equals("power_supply_unit")) {
								filteredProducts.add(product);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných objednávek
				if (!filteredProducts.isEmpty()) {
					productsPriceChangePageableListView.setList(filteredProducts);
				
				} else productsPriceChangePageableListView.setList(new ProductDAOImpl().getAllProducts());
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
// Procesory AjaxCheckBox ////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox processorsCheckBox = new AjaxCheckBox("processorsCheckBox", new PropertyModel<Boolean>(this, "processors")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(processorsCheckBox);
		
		// Procesory Label
		processorsLabel = new Label("processorsLabel", "     Procesory" );
		filterForm.add(processorsLabel);
		
// Grafické karty AjaxCheckBox ///////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox graphicsCardsCheckBox = new AjaxCheckBox("graphicsCardsCheckBox", new PropertyModel<Boolean>(this, "graphicsCards")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(graphicsCardsCheckBox);
		
		// Grafické karty Label
		graphicsCardsLabel = new Label("graphicsCardsLabel", "     Grafické karty");
		filterForm.add(graphicsCardsLabel);
		
// Operační paměti AjaxCheckBox //////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox memoryCheckBox = new AjaxCheckBox("memoryCheckBox", new PropertyModel<Boolean>(this, "memory")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(memoryCheckBox);
		
		// Operační paměti Label
		memoryLabel = new Label("memoryLabel", "     Oprační paměti");
		filterForm.add(memoryLabel);
		
// Zákldaní desky AjaxCheckBox ///////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox motherboardsCheckBox = new AjaxCheckBox("motherboardsCheckBox", new PropertyModel<Boolean>(this, "motherboards")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(motherboardsCheckBox);
		
		// Zákldaní desky Label
		motherboardsLabel = new Label("motherboardsLabel", "     Základní desky");
		filterForm.add(motherboardsLabel);
		
// Pevné disky AjaxCheckBox //////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox hardDisksCheckBox = new AjaxCheckBox("hardDisksCheckBox", new PropertyModel<Boolean>(this, "hardDisks")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(hardDisksCheckBox);
		
		// Pevné disky Label
		hardDisksLabel = new Label("hardDisksLabel", "     Pevné disky");
		filterForm.add(hardDisksLabel);
		
// Zdroje AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox powerSupplyUnitsCheckBox = new AjaxCheckBox("powerSupplyUnitsCheckBox", new PropertyModel<Boolean>(this, "powerSupplyUnits")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(productsPriceChangeForm);
				target.add(pagingNavigator);
			}
		};
		filterForm.add(powerSupplyUnitsCheckBox);
		
		// Zdroje Label
		powerSupplyUnitsLabel = new Label("powerSupplyUnitsLabel", "     Zdroje");
		filterForm.add(powerSupplyUnitsLabel);
		
// Stav zboží PageableListView ///////////////////////////////////////////////////////////////////////
		
		productsPriceChangePageableListView = new PageableListView<Product>("productsPriceChangePageableListView", new ProductDAOImpl().getAllProducts(), 10) {
			
			Integer priceChange;
			
			@Override
			protected void populateItem(ListItem<Product> item) {
				
				Product product = item.getModelObject();
				String imagePath = "preview images//" + product.getName() + "//" + product.getName() + ".jpg";
				
				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String price = decimalFormat.format(product.getPrice()) + ",-";
				
				item.add(new Link<Object>("imageLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Image("image", new ContextRelativeResource(imagePath))));
				
				item.add(new Link<Object>("nameLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na stránku produktu
						getDetailPage(product);
					}
				}.add(new Label("name", product.getName())));
				
				item.add(new Label("currentPrice", price) {
					
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						
						tag.put("style", "font-weight: bold; color: #B22222;");
					}
				});
				
				priceChangeNumberTextField = new NumberTextField("priceChange", new PropertyModel<Integer>(this, "priceChange"));
				priceChangeNumberTextField.add(new OnChangeAjaxBehavior() {
					
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						idProduct = product.getId();
						newPrice = priceChangeNumberTextField.getModelObject();
						
						target.add(submitButton);
					}
				});
				item.add(priceChangeNumberTextField);

				submitButton = new Button("submitButton");
				item.add(submitButton);
			}
		};
		productsPriceChangeForm.add(productsPriceChangePageableListView);
		
		pagingNavigator = new CustomPagingNavigator("pagingNavigator", productsPriceChangePageableListView);
		pagingNavigator.setOutputMarkupId(true);
		add(pagingNavigator);
	}
	
}
