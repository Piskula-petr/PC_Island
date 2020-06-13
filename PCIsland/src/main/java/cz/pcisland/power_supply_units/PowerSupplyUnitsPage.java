package cz.pcisland.power_supply_units;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import cz.pcisland.product.Product;
import cz.pcisland.product.ProductPageWithTopSelling;

/**
 *	Třída zdrojů:
 *
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */
public class PowerSupplyUnitsPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean corsair = false;
	private Boolean evga = false;
	private Boolean seasonic = false;
	
	private Boolean atx = false;
	private Boolean sfx = false;
	
	private Boolean performance650 = false;
	private Boolean performance750 = false;
	private Boolean performance850 = false;
	private Boolean performance1300 = false;
	
	private Boolean efficiencyBronze = false;
	private Boolean efficiencySilver = false;
	private Boolean efficiencyGold = false;
	private Boolean efficiencyPlatinum = false;
	private Boolean efficiencyTitanium = false;
	
	private Boolean fan92 = false;
	private Boolean fan120 = false;
	private Boolean fan130 = false;
	private Boolean fan135 = false;
	private Boolean fan140 = false;

	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private PowerSupplyUnitDAO powerSupplyUnitDAO = new PowerSupplyUnitDAOImpl();
	private List<Product> powerSupplyUnitsInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel, corsairLabel, evgaLabel, seasonicLabel, atxLabel, sfxLabel, performance650Label, performance750Label, performance850Label,
				  performance1300Label, efficiencyBronzeLabel, efficiencySilverLabel, efficiencyGoldLabel, efficiencyPlatinumLabel, efficiencyTitaniumLabel,
				  fan92Label, fan120Label, fan130Label, fan135Label, fan140Label;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public PowerSupplyUnitsPage() {
		
		// Nastaví titulek
		setTitle(new Model("Zdroje (PSU) | PC Island"));
		
		// Nejprodávanější (zdroje) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(powerSupplyUnitDAO.getTopSellingPowerSupplyUnits());
		
		// Produkty (zdroje) PageableListView (ProductPage)
		productsPageableListView.setList(powerSupplyUnitDAO.getAllPowerSupplyUnits());
		products = productsPageableListView.getList();

// Chybová hláška WebMarkupContainer ///////////////////////////////////////////////////////////////////
		
		WebMarkupContainer errorMessageMarkupContainer = new WebMarkupContainer("errorMessageMarkupContainer") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (products.isEmpty()) {
					setVisible(true);
					
				} else setVisible(false);
			}
		};
		errorMessageMarkupContainer.setOutputMarkupId(true);
		markupContainer.add(errorMessageMarkupContainer);
		
		errorMessageMarkupContainer.add(new Label("errorMessage", "Vašemu výběru neodpovídají žádné produkty."));
		errorMessageMarkupContainer.add(new Link<Object>("refreshLink") {
			
			@Override
			public void onClick() {
				
				// Obnovení stránky
				setResponsePage(PowerSupplyUnitsPage.class);
			}
		});
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny zdroje
				products = powerSupplyUnitDAO.getAllPowerSupplyUnits();
				
				// Nastavení cenového rozpětí
				powerSupplyUnitsInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Zdroje v cenovém rozpětí / Všechny zdroje
				if (powerSupplyUnitsInPriceRange.size() != 0) {
					products = new ArrayList<>(powerSupplyUnitsInPriceRange);
				
				} else products = powerSupplyUnitDAO.getAllPowerSupplyUnits();
				
				// Ochrana minimální a maximální ceny
				if (inputMinPrice < MIN_PRICE) {
					minPriceTextField.setDefaultModelObject(MIN_PRICE);
				
				} else if (inputMinPrice > MAX_PRICE) {
					minPriceTextField.setDefaultModelObject(MAX_PRICE);
				
				} else if (inputMaxPrice < inputMinPrice) {
					maxPriceTextField.setDefaultModelObject(inputMinPrice);
				
				} else if (inputMaxPrice > MAX_PRICE) {
					maxPriceTextField.setDefaultModelObject(MAX_PRICE);
				}
				
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
				
				List<Product> filteredPowerSupplyUnits = new ArrayList<>();
				
				for (Product powerSupplyUnit : products) {
					
					// Skladem
					if (onStock) {
						
						if (powerSupplyUnit.getStock() > 0) {
							filteredPowerSupplyUnits.add(powerSupplyUnit);
						}
					}
				}
				
				// Přiřazení filtrovaných zdrojů
				if (!filteredPowerSupplyUnits.isEmpty()) {
					products = filteredPowerSupplyUnits;
				}
				
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredPowerSupplyUnits = new ArrayList<>();
				
				if (corsair && evga && seasonic) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product powerSupplyUnit :products) {
						
						
						// Corsair
						if (corsair) {
							
							if (powerSupplyUnit.getParametrs().get("company").equals("Corsair")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// EVGA
						if (evga) {
							
							if (powerSupplyUnit.getParametrs().get("company").equals("EVGA")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// Seasonic
						if (seasonic) {
							
							if (powerSupplyUnit.getParametrs().get("company").equals("Seasonic")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných zdrojů
				if (!filteredPowerSupplyUnits.isEmpty()) {
					products = filteredPowerSupplyUnits;
				}
				
// Typ zdroje /////////////////////////////////////////////////////////////////////////////////////////
				
			filteredPowerSupplyUnits = new ArrayList<>();
			
			if (atx && sfx) {
				// Žádná akce v případě všech možnosti
			} else {
				
				for (Product powerSupplyUnit : products) {
					
					// ATX
					if (atx) {
						
						if (powerSupplyUnit.getParametrs().get("type").contains("ATX")) {
							filteredPowerSupplyUnits.add(powerSupplyUnit);
						}
					}
					
					// SFX
					if (sfx) {
						
						if (powerSupplyUnit.getParametrs().get("type").contains("SFX")) {
							filteredPowerSupplyUnits.add(powerSupplyUnit);
						}
					}
				}
			}
			
			// Přiřazení filtrovaných zdrojů
			if (!filteredPowerSupplyUnits.isEmpty()) {
				products = filteredPowerSupplyUnits;
			}
				
// Výkon //////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredPowerSupplyUnits = new ArrayList<>();
				
				if (performance650 && performance750 && performance850 && performance1300) {
					// Žádná akce v případě všech možnosti
				} else {

					for (Product powerSupplyUnit : products) {
						
						// 650 W
						if (performance650) {
							
							if (powerSupplyUnit.getParametrs().get("performance").equals("650")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 750 W
						if (performance750) {
							
							if (powerSupplyUnit.getParametrs().get("performance").equals("750")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 850 W
						if (performance850) {
							
							if (powerSupplyUnit.getParametrs().get("performance").equals("850")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 1300 W
						if (performance1300) {
							
							if (powerSupplyUnit.getParametrs().get("performance").equals("1300")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných zdrojů
				if (!filteredPowerSupplyUnits.isEmpty()) {
					products = filteredPowerSupplyUnits;
				}
				
// Energetická efektivita ////////////////////////////////////////////////////////////////////////////
				
				filteredPowerSupplyUnits = new ArrayList<>();
				
				if (efficiencyBronze && efficiencySilver && efficiencyGold && efficiencyPlatinum && efficiencyTitanium) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product powerSupplyUnit : products) {
						
						// 80 PLUS BRONZE
						if (efficiencyBronze) {
							
							if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS BRONZE")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 80 PLUS SILVER
						if (efficiencySilver) {
							
							if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS SILVER")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 80 PLUS GOLD
						if (efficiencyGold) {
							
							if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS GOLD")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 80 PLUS PLATINUM
						if (efficiencyPlatinum) {
							
							if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS PLATINUM")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 80 PLUS TITANIUM
						if (efficiencyTitanium) {
							
							if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS TITANIUM")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných zdrojů
				if (!filteredPowerSupplyUnits.isEmpty()) {
					products = filteredPowerSupplyUnits;
				}	
				
// Velikost ventilatoru //////////////////////////////////////////////////////////////////////////////
				
				filteredPowerSupplyUnits = new ArrayList<>();
				
				if (fan92 && fan120 && fan135 && fan130 && fan140) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product powerSupplyUnit : products) {
						
						// 92 MM
						if (fan92) {
							
							if (powerSupplyUnit.getParametrs().get("fanSize").equals("92")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 120 MM
						if (fan120) {
							
							if (powerSupplyUnit.getParametrs().get("fanSize").equals("120")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 130 MM
						if (fan130) {
							
							if (powerSupplyUnit.getParametrs().get("fanSize").equals("130")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 135 MM
						if (fan135) {
							
							if (powerSupplyUnit.getParametrs().get("fanSize").equals("135")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
						
						// 140 MM
						if (fan140) {
							
							if (powerSupplyUnit.getParametrs().get("fanSize").equals("140")) {
								filteredPowerSupplyUnits.add(powerSupplyUnit);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných zdrojů
				if (!filteredPowerSupplyUnits.isEmpty()) {
					products = filteredPowerSupplyUnits;
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);
				
				corsairLabel.setVisible(false);
				evgaLabel.setVisible(false);
				seasonicLabel.setVisible(false);

				atxLabel.setVisible(false);
				sfxLabel.setVisible(false);
				
				performance650Label.setVisible(false);
				performance750Label.setVisible(false);
				performance850Label.setVisible(false);
				performance1300Label.setVisible(false);
				
				efficiencyBronzeLabel.setVisible(false);
				efficiencySilverLabel.setVisible(false);
				efficiencyGoldLabel.setVisible(false);
				efficiencyPlatinumLabel.setVisible(false);
				efficiencyTitaniumLabel.setVisible(false);
				
				fan92Label.setVisible(false);
				fan120Label.setVisible(false);
				fan130Label.setVisible(false);
				fan135Label.setVisible(false);
				fan140Label.setVisible(false);
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product powerSupplyUnit : products) {
				
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!corsair && !evga && !seasonic && !atx && !sfx && !performance650 && !performance750 
						&& !performance850 && !performance1300 && !efficiencyBronze && !efficiencyGold && !efficiencySilver 
						&& !efficiencyPlatinum && !efficiencyTitanium && !fan92 && !fan120 && !fan130 && !fan135 && !fan140)) {
						
						onStockLabel.setVisible(true);
					}
					
// Typ zdroje ////////////////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getParametrs().get("type").contains("ATX")) {
						atxLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("type").contains("SFX")) {
						sfxLabel.setVisible(true);
					}
					
					if ((atx || sfx) && (!corsair && !evga && !seasonic && !performance650 && !performance750 
						&& !performance850 && !performance1300 && !efficiencyBronze && !efficiencyGold && !efficiencySilver 
						&& !efficiencyPlatinum && !efficiencyTitanium && !fan92 && !fan120 && !fan130 && !fan135 && !fan140)) {
						
						atxLabel.setVisible(true);
						sfxLabel.setVisible(true);
					}
					
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getParametrs().get("company").equals("Corsair")) {
						corsairLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("company").equals("EVGA")) {
						evgaLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("company").equals("Seasonic")) {
						seasonicLabel.setVisible(true);
					}
					
					if ((corsair || evga || seasonic) && (!atx && !sfx && !performance650 && !performance750 && !performance850 
						&& !performance1300 && !efficiencyBronze && !efficiencyGold && !efficiencySilver && !efficiencyPlatinum 
						&& !efficiencyTitanium && !fan92 && !fan120 && !fan130 && !fan135 && !fan140)) {
						
						corsairLabel.setVisible(true);
						evgaLabel.setVisible(true);
						seasonicLabel.setVisible(true);
					}	
			
// Výkon /////////////////////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getParametrs().get("performance").equals("650")) {
						performance650Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("performance").equals("750")) {
						performance750Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("performance").equals("850")) {
						performance850Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("performance").equals("1300")) {
						performance1300Label.setVisible(true);
					}
					
					if ((performance650 || performance750 || performance850 || performance1300) && (!corsair && !evga && !seasonic 
						&& !atx && !sfx && !efficiencyBronze && !efficiencySilver && !efficiencyGold && !efficiencyPlatinum && !efficiencyTitanium 
						&& !fan92 && !fan120 && !fan130 && !fan135 && !fan140)) {
						
						performance650Label.setVisible(true);
						performance750Label.setVisible(true);
						performance850Label.setVisible(true);
						performance1300Label.setVisible(true);
					}	
					
// Energetická efektivita ////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS BRONZE")) {
						efficiencyBronzeLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS SILVER")) {
						efficiencySilverLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS GOLD")) {
						efficiencyGoldLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS PLATINUM")) {
						efficiencyPlatinumLabel.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("efficiency").equals("80 PLUS TITANIUM")) {
						efficiencyTitaniumLabel.setVisible(true);
					}
					
					if ((efficiencyBronze || efficiencySilver || efficiencyGold || efficiencyPlatinum || efficiencyPlatinum) && (!corsair && !evga 
						&& !seasonic && !atx && !sfx && !performance650 && !performance750 && !performance850 && !performance1300 
						&& !fan92 && !fan120 && !fan130 && !fan135 && !fan140)) {
						
						efficiencyBronzeLabel.setVisible(true);
						efficiencySilverLabel.setVisible(true);
						efficiencyGoldLabel.setVisible(true);
						efficiencyPlatinumLabel.setVisible(true);
						efficiencyTitaniumLabel.setVisible(true);
					}
					
// Velikost ventilatoru //////////////////////////////////////////////////////////////////////////////
					
					if (powerSupplyUnit.getParametrs().get("fanSize").equals("92")) {
						fan92Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("fanSize").equals("120")) {
						fan120Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("fanSize").equals("130")) {
						fan130Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("fanSize").equals("135")) {
						fan135Label.setVisible(true);
					
					} else if (powerSupplyUnit.getParametrs().get("fanSize").equals("140")) {
						fan140Label.setVisible(true);
					}
					
					if ((fan92 || fan120 || fan130 || fan135 || fan140) && (!corsair && !evga && !seasonic && !atx && !sfx && !performance650 
						&& !performance750 && !performance850 && !performance1300 && !efficiencyBronze && !efficiencySilver && !efficiencyGold 
						&& !efficiencyPlatinum && !efficiencyTitanium)) {
						
						fan92Label.setVisible(true);
						fan120Label.setVisible(true);
						fan130Label.setVisible(true);
						fan135Label.setVisible(true);
						fan140Label.setVisible(true);
					}
				}
			}
		};
		
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product powerSupplyUnit : products) {
			
			if (MAX_PRICE < powerSupplyUnit.getPrice()) {
				MAX_PRICE = powerSupplyUnit.getPrice();
			}
			
			if (MIN_PRICE > powerSupplyUnit.getPrice()) {
				MIN_PRICE = powerSupplyUnit.getPrice();
			}
		}
						
// Minimální cena TextField //////////////////////////////////////////////////////////////////////////

		minPriceTextField = new TextField<Integer>("minPriceTextField", new PropertyModel<Integer>(this, "inputMinPrice"));
		minPriceTextField.setDefaultModelObject(MIN_PRICE);
		filterForm.add(minPriceTextField);
		
// Maximální cena TextField //////////////////////////////////////////////////////////////////////////
		
		maxPriceTextField = new TextField<Integer>("maxPriceTextField", new PropertyModel<Integer>(this, "inputMaxPrice"));
		maxPriceTextField.setDefaultModelObject(MAX_PRICE);
		filterForm.add(maxPriceTextField);


// Skladem AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox onStockCheckBox = new AjaxCheckBox("onStockCheckBox", new PropertyModel<Boolean>(this, "onStock")) {	
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(onStockCheckBox);
	
		// Skladem Label
		onStockLabel = new Label("onStockLabel", "     Skladem") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!onStockLabel.isVisible()) {
					onStock = false;
				}
			}
		};
		filterForm.add(onStockLabel);
		
// Corsair AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox corsairCheckBox = new AjaxCheckBox("corsairCheckBox", new PropertyModel<Boolean>(this, "corsair")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(corsairCheckBox);
		
		// Corsair Label
		corsairLabel = new Label("corsairLabel", "     Corsair") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!corsairLabel.isVisible()) {
					corsair = false;
				}
			}
		};
		filterForm.add(corsairLabel);
		
// EVGA AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox evgaCheckBox = new AjaxCheckBox("evgaCheckBox", new PropertyModel<Boolean>(this, "evga")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(evgaCheckBox);
		
		// EVGA Label
		evgaLabel = new Label("evgaLabel", "     EVGA") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!evgaLabel.isVisible()) {
					evga = false;
				}
			}
		};
		filterForm.add(evgaLabel);
		
// Seasonic AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox seasonicCheckBox = new AjaxCheckBox("seasonicCheckBox", new PropertyModel<Boolean>(this, "seasonic")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(seasonicCheckBox);
		
		// Seasonic Label
		seasonicLabel = new Label("seasonicLabel", "     Seasonic") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!seasonicLabel.isVisible()) {
					seasonic = false;
				}
			}
		};
		filterForm.add(seasonicLabel);
		
// ATX AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox atxCheckBox = new AjaxCheckBox("atxCheckBox", new PropertyModel<Boolean>(this, "atx")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(atxCheckBox);
		
		// ATX Label
		atxLabel = new Label("atxLabel", "     ATX") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!atxLabel.isVisible()) {
					atx = false;
				}
			}
		};
		filterForm.add(atxLabel);
		
// SFX AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox sfxCheckBox = new AjaxCheckBox("sfxCheckBox", new PropertyModel<Boolean>(this, "sfx")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(sfxCheckBox);
		
		// SFX Label
		sfxLabel = new Label("sfxLabel", "     SFX") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!sfxLabel.isVisible()) {
					sfx = false;
				}
			}
		};
		filterForm.add(sfxLabel);
		
// 650 W AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox performance650CheckBox = new AjaxCheckBox("performance650CheckBox", new PropertyModel<>(this, "performance650")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(performance650CheckBox);
		
		// 650 W Label
		performance650Label = new Label("performance650Label", "     650 W") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!performance650Label.isVisible()) {
					performance650 = false;
				}
			}
		};
		filterForm.add(performance650Label);
		
// 750 W AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox performance750CheckBox = new AjaxCheckBox("performance750CheckBox", new PropertyModel<Boolean>(this, "performance750")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(performance750CheckBox);
		
		// 750 W Label
		performance750Label = new Label("performance750Label", "     750 W") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!performance750Label.isVisible()) {
					performance750 = false;
				}
			}
		};
		filterForm.add(performance750Label);
		
// 850 W AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox performance850CheckBox = new AjaxCheckBox("performance850CheckBox", new PropertyModel<Boolean>(this, "performance850")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(performance850CheckBox);
		
		// 850 W Label
		performance850Label = new Label("performance850Label", "     850 W") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!performance850Label.isVisible()) {
					performance850 = false;
				}
			}
		};
		filterForm.add(performance850Label);
		
// 1300 W AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox performance1300CheckBox = new AjaxCheckBox("performance1300CheckBox", new PropertyModel<Boolean>(this, "performance1300")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(performance1300CheckBox);
		
		// 1300 W Label
		performance1300Label = new Label("performance1300Label", "     1300 W") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!performance1300Label.isVisible()) {
					performance1300 = false;
				}
			}
		};
		filterForm.add(performance1300Label);
		
// 80 PLUS BRONZE AjaxCheckBox ///////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox efficiencyBronzeCheckBox = new AjaxCheckBox("efficiencyBronzeCheckBox", new PropertyModel<Boolean>(this, "efficiencyBronze")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(efficiencyBronzeCheckBox);
		
		// 80 PLUS BRONZE Label
		efficiencyBronzeLabel = new Label("efficiencyBronzeLabel", "     80 PLUS BRONZE") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!efficiencyBronzeLabel.isVisible()) {
					efficiencyBronze = false;
				}
			}
		};
		filterForm.add(efficiencyBronzeLabel);
		
// 80 PLUS SILVER AjaxCheckBox ////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox efficiencySilverCheckBox = new AjaxCheckBox("efficiencySilverCheckBox", new PropertyModel<Boolean>(this, "efficiencySilver")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(efficiencySilverCheckBox);
		
		// 80 PLUS Silver Label
		efficiencySilverLabel = new Label("efficiencySilverLabel","     80 PLUS SILVER") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!efficiencySilverLabel.isVisible()) {
					efficiencySilver = false;
				}
			}
		};
		filterForm.add(efficiencySilverLabel);
		
// 80 PLUS GOLD AjaxCheckBox //////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox efficiencyGoldCheckBox = new AjaxCheckBox("efficiencyGoldCheckBox", new PropertyModel<Boolean>(this, "efficiencyGold")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(efficiencyGoldCheckBox);
		
		// 80 PLUS GOLD Label
		efficiencyGoldLabel = new Label("efficiencyGoldLabel", "     80 PLSU GOLD") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!efficiencyGoldLabel.isVisible()) {
					efficiencyGold = false;
				}
			}
		};
		filterForm.add(efficiencyGoldLabel);
		
// 80 PLUS PLATINUM AjaxCheckBox /////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox efficiencyPlatinumCheckBox = new AjaxCheckBox("efficiencyPlatinumCheckBox", new PropertyModel<Boolean>(this, "efficiencyPlatinum")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(efficiencyPlatinumCheckBox);
		
		// 80 PLUS PLATINUM Label
		efficiencyPlatinumLabel = new Label("efficiencyPlatinumLabel", "     80 PLUS PLATINUM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!efficiencyPlatinumLabel.isVisible()) {
					efficiencyPlatinum = false;
				}
			}
		};
		filterForm.add(efficiencyPlatinumLabel);
		
// 80 PLUS TITANIUM AjaxCheckBox /////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox efficiencyTitaniumCheckBox = new AjaxCheckBox("efficiencyTitaniumCheckBox", new PropertyModel<Boolean>(this, "efficiencyTitanium")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(efficiencyTitaniumCheckBox);
		
		// 80 PLUS TITANIUM Label
		efficiencyTitaniumLabel = new Label("efficiencyTitaniumLabel", "     80 PLUS TITANIUM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!efficiencyTitaniumLabel.isVisible()) {
					efficiencyTitanium = false;
				}
			}
		};
		filterForm.add(efficiencyTitaniumLabel);
	
// 92 MM AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox fan92CheckBox = new AjaxCheckBox("fan92CheckBox", new PropertyModel<Boolean>(this, "fan92")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(fan92CheckBox);
		
		// 92 MM Label
		fan92Label = new Label("fan92Label", "     92 MM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!fan92Label.isVisible()) {
					fan92 = false;
				}
			}
		};
		filterForm.add(fan92Label);
		
// 120 MM AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox fan120CheckBox = new AjaxCheckBox("fan120CheckBox", new PropertyModel<Boolean>(this, "fan120")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(fan120CheckBox);
		
		// 120 MM Label
		fan120Label = new Label("fan120Label", "     120 MM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!fan120Label.isVisible()) {
					fan120 = false;
				}
			}
		};
		filterForm.add(fan120Label);
		
// 130 MM AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox fan130CheckBox = new AjaxCheckBox("fan130CheckBox", new PropertyModel<Boolean>(this, "fan130")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(fan130CheckBox);
		
		// 130 MM Label
		fan130Label = new Label("fan130Label", "     130 MM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!fan130Label.isVisible()) {
					fan130 = false;
				}
			}
		};
		filterForm.add(fan130Label);
		
// 135 MM AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox fan135CheckBox = new AjaxCheckBox("fan135CheckBox", new PropertyModel<Boolean>(this, "fan135")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(fan135CheckBox);
		
		// 135 MM Label
		fan135Label = new Label("fan135Label", "     135 MM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!fan135Label.isVisible()) {
					fan135 = false;
				}
			}
		};
		filterForm.add(fan135Label);
		
// 140 MM AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox fan140CheckBox = new AjaxCheckBox("fan140CheckBox", new PropertyModel<Boolean>(this, "fan140")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(fan140CheckBox);
		
		// 140 MM Label
		fan140Label = new Label("fan140Label", "     140 MM") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!fan140Label.isVisible()) {
					fan140 = false;
				}
			}
		};
		filterForm.add(fan140Label);
	}
	
}
