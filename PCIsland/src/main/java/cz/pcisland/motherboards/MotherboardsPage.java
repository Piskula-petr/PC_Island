package cz.pcisland.motherboards;

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
 *	Třída základních desek:
 *
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */

public class MotherboardsPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean asus = false;
	private Boolean gigabyte = false;
	private Boolean msi = false;
	
	private Boolean socket1151 = false;
	private Boolean socketAM4 = false;
	private Boolean socketTR4 = false;
	
	private Boolean amdB450 = false;
	private Boolean amdX370 = false;
	private Boolean amdX399 = false;
	private Boolean amdX470 = false;
	private Boolean intelZ390 = false;
	
	private Boolean memorySlot4 = false;
	private Boolean memorySlot8 = false;
	
	private Boolean atx = false;
	private Boolean eAtx = false;

	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private MotherboardDAO motherboardDAO = new MotherboardDAOImpl();
	private List<Product> motherboardsInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel, asusLabel, gigabyteLabel, msiLabel, socket1151Label, socketAM4Label, socketTR4Label,
				  amdB450Label, amdX370Label, amdX399Label, amdX470Label, intelZ390Label, memorySlot4Label,
				  memorySlot8Label, atxLabel, eAtxLabel;	
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public MotherboardsPage() {
		
		// Nastaví titulek
		setTitle(new Model("Základní desky (Motherboards) | PC Island"));
		
		// Nejprodávanější (základní desky) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(motherboardDAO.getTopSellingMotherboards());
		
		// Produkty (základní desky) PageableListView (ProductPage)
		productsPageableListView.setList(motherboardDAO.getAllMotherboards());
		products = productsPageableListView.getList();

// Chybová hláška WebMarkupContainer ///////////////////////////////////////////////////////////////////
		
		WebMarkupContainer errorMessageMarkupContainer = new WebMarkupContainer("errorMessageMarkupContainer") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost chybové hlášky
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
				setResponsePage(MotherboardsPage.class);
			}
		});
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny grafické karty
				products = motherboardDAO.getAllMotherboards();
				
				// Nastavení cenového ropětí
				motherboardsInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Základní desky v cenovém rozpětí / Všechny základní desky
				if (motherboardsInPriceRange.size() != 0) {
					products = new ArrayList<>(motherboardsInPriceRange);
				
				} else products = motherboardDAO.getAllMotherboards();
				
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
				
				List<Product> filteredMotherboards = new ArrayList<>();
				
				for (Product motherboard : products) {
					
					// Skladem
					if (onStock) {
						
						if (motherboard.getStock() > 0) {
							filteredMotherboards.add(motherboard);
						}
					}
				}
				
				// Přiřazení filtrovaných základních desek
				if (!filteredMotherboards.isEmpty()) {
					products = filteredMotherboards;
				}
				
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMotherboards = new ArrayList<>();
					
				if (asus && gigabyte && msi) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product motherboard : products) {
						
						// ASUS
						if (asus) {
							
							if (motherboard.getParametrs().get("company").equals("ASUS")) {
								filteredMotherboards.add(motherboard);
							}
						} 
						
						// GIGABYTE
						if (gigabyte) {
							
							if (motherboard.getParametrs().get("company").equals("GIGABYTE")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// MSI
						if (msi) {
							
							if (motherboard.getParametrs().get("company").equals("MSI")) {
								filteredMotherboards.add(motherboard);
							}
						}
					}
					
					// Přiřazení filtrovaných základních desek
					if (!filteredMotherboards.isEmpty()) {
						products = filteredMotherboards;
					}
				}
				
// Patice ////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMotherboards = new ArrayList<>();
					
				if (socket1151 && socketAM4 && socketTR4) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product motherboard : products) {
						
						// Socket 1151
						if (socket1151) {
							
							if (motherboard.getParametrs().get("socket").equals("1151")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// Socket AM4
						if (socketAM4) {
							
							if (motherboard.getParametrs().get("socket").equals("AM4")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// Socket TR4
						if (socketTR4) {
							
							if (motherboard.getParametrs().get("socket").equals("TR4")) {
								filteredMotherboards.add(motherboard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných základních desek
				if (!filteredMotherboards.isEmpty()) {
					products = filteredMotherboards;
				}
			
// Čipová sada ///////////////////////////////////////////////////////////////////////////////////////
				
				filteredMotherboards = new ArrayList<>();
				
				if (amdB450 && amdX370 && amdX399 && amdX470 && intelZ390) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product motherboard : products) {
						
						// AMD B450
						if (amdB450) {
							
							if (motherboard.getParametrs().get("chipset").equals("AMD B450")) {
								filteredMotherboards.add(motherboard);
							}
						} 
						
						// AMD X370
						if (amdX370) {
							
							if (motherboard.getParametrs().get("chipset").equals("AMD X370")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// AMD X399
						if (amdX399) {
							
							if (motherboard.getParametrs().get("chipset").equals("AMD X399")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// AMD X470
						if (amdX470) {
							
							if (motherboard.getParametrs().get("chipset").equals("AMD X470")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// Intel Z390
						if (intelZ390) {
							
							if (motherboard.getParametrs().get("chipset").equals("Intel Z390")) {
								filteredMotherboards.add(motherboard);
							}
						} 
					}
				}
				
				// Přiřazení filtrovaných základních desek
				if (!filteredMotherboards.isEmpty()) {
					products = filteredMotherboards;
				}
				
// Počet paměťových slotů /////////////////////////////////////////////////////////////////////////////
				
				filteredMotherboards = new ArrayList<>();
				
				if (memorySlot4 && memorySlot8) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product motherboard : products) {
						
						// 4x DDR4
						if (memorySlot4) {
							
							if (motherboard.getParametrs().get("memoryType").equals("4x DDR4")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// 8x DDR4
						if (memorySlot8) {
							
							if (motherboard.getParametrs().get("memoryType").equals("8x DDR4")) {
								filteredMotherboards.add(motherboard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných základních desek
				if (!filteredMotherboards.isEmpty()) {
					products = filteredMotherboards;
				}
				
// Formát /////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMotherboards = new ArrayList<>();
				
				if (atx && eAtx) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product motherboard : products) {
						
						// ATX
						if (atx) {
							
							if (motherboard.getParametrs().get("format").equals("ATX")) {
								filteredMotherboards.add(motherboard);
							}
						}
						
						// E-ATX
						if (eAtx) {
							
							if (motherboard.getParametrs().get("format").equals("E-ATX")) {
								filteredMotherboards.add(motherboard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných základních desek
				if (!filteredMotherboards.isEmpty()) {
					products = filteredMotherboards;
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);

				asusLabel.setVisible(false);
				gigabyteLabel.setVisible(false);
				msiLabel.setVisible(false);
				
				socket1151Label.setVisible(false);
				socketAM4Label.setVisible(false);
				socketTR4Label.setVisible(false);
				
				amdB450Label.setVisible(false);
				amdX370Label.setVisible(false);
				amdX399Label.setVisible(false);
				amdX470Label.setVisible(false);
				intelZ390Label.setVisible(false);
				
				memorySlot4Label.setVisible(false);
				memorySlot8Label.setVisible(false);
				
				atxLabel.setVisible(false);
				eAtxLabel.setVisible(false);
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product motherboard : products) {
					
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!asus && !gigabyte && !msi && !socket1151 && !socketAM4 && !socketTR4 && !amdB450
						&& !amdX370 && !amdX399 && !amdX470 && !intelZ390 && !memorySlot4 && !memorySlot8 && !atx && !eAtx)) {
						
						onStockLabel.setVisible(true);
					}
					
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getParametrs().get("company").equals("MSI")) {
						msiLabel.setVisible(true);
					
					} else if (motherboard.getParametrs().get("company").equals("ASUS")) {
						asusLabel.setVisible(true);
					
					} else if (motherboard.getParametrs().get("company").equals("GIGABYTE")) {
						gigabyteLabel.setVisible(true);
					}
					
					if ((asus || gigabyte || msi) && (!socket1151 && !socketAM4 && !socketTR4 && !amdB450
						&& !amdX370 && !amdX399 && !amdX470 && !intelZ390 && !memorySlot4 && !memorySlot8 && !atx && !eAtx)) {
						
						msiLabel.setVisible(true);
						asusLabel.setVisible(true);
						gigabyteLabel.setVisible(true);
					}
					
// Patice ////////////////////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getParametrs().get("socket").equals("1151")) {
						socket1151Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("socket").equals("AM4")) {
						socketAM4Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("socket").equals("TR4")) {
						socketTR4Label.setVisible(true);
					}
					
					if ((socket1151 || socketAM4 || socketTR4) && (!asus && !gigabyte && !msi && !amdB450 && !amdX370 
						&& !amdX399 && !amdX470 && !intelZ390 && !memorySlot4 && !memorySlot8 && !atx && !eAtx)) {
						
						socket1151Label.setVisible(true);
						socketAM4Label.setVisible(true);
						socketTR4Label.setVisible(true);
					}
					
// Čipová sada ///////////////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getParametrs().get("chipset").equals("AMD B450")) {
						amdB450Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("chipset").equals("AMD X370")) {
						amdX370Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("chipset").equals("AMD X399")) {
						amdX399Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("chipset").equals("AMD X470")) {
						amdX470Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("chipset").equals("Intel Z390")) {
						intelZ390Label.setVisible(true);
					}
					
					if ((amdB450 || amdX370 || amdX399 || amdX470 || intelZ390) && (!asus && !gigabyte && !msi && !socket1151 
						&& !socketAM4 && !socketTR4 && !memorySlot4 && !memorySlot8 && !atx && !eAtx)) {
						
						amdB450Label.setVisible(true);
						amdX370Label.setVisible(true);
						amdX399Label.setVisible(true);
						amdX470Label.setVisible(true);
						intelZ390Label.setVisible(true);
					}
					
// Počet paměťových slotů ////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getParametrs().get("memoryType").equals("4x DDR4")) {
						memorySlot4Label.setVisible(true);
					
					} else if (motherboard.getParametrs().get("memoryType").equals("8x DDR4")) {
						memorySlot8Label.setVisible(true);
					}
					
					if ((memorySlot4 || memorySlot8) && (!asus && !gigabyte && !msi && !socket1151 && !socketAM4 && !socketTR4 
						&& !amdB450 && !amdX370 && !amdX399 && !amdX470 && !intelZ390 && !atx && !eAtx)) {
						
						memorySlot4Label.setVisible(true);
						memorySlot8Label.setVisible(true);
					}
					
// Formát ////////////////////////////////////////////////////////////////////////////////////////////
					
					if (motherboard.getParametrs().get("format").equals("ATX")) {
						atxLabel.setVisible(true);
					
					} else if (motherboard.getParametrs().get("format").equals("E-ATX")) {
						eAtxLabel.setVisible(true);
					}
					
					if ((atx || eAtx) && (!asus && !gigabyte && !msi && !socket1151 && !socketAM4 && !socketTR4 
						&& !amdB450 && !amdX370 && !amdX399 && !amdX470 && !intelZ390 && !memorySlot4 && !memorySlot8)) {
						
						atxLabel.setVisible(true);
						eAtxLabel.setVisible(true);
					}
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product motherboard : products) {
			
			if (MAX_PRICE < motherboard.getPrice()) {
				MAX_PRICE = motherboard.getPrice();
			}
			
			if (MIN_PRICE > motherboard.getPrice()) {
				MIN_PRICE = motherboard.getPrice();
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
		
// ASUS AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox asusCheckBox = new AjaxCheckBox("asusCheckBox", new PropertyModel<Boolean>(this, "asus")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(asusCheckBox);
		
		// ASUS Label
		asusLabel = new Label("asusLabel", "     ASUS") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!asusLabel.isVisible()) {
					asus = false;
				}
			}
		};
		filterForm.add(asusLabel);
		
// GIGABYTE AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox gigabyteCheckBox = new AjaxCheckBox("gigabyteCheckBox", new PropertyModel<Boolean>(this, "gigabyte")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(gigabyteCheckBox);
		
		// GIGABYTE Label
		gigabyteLabel = new Label("gigabyteLabel", "     GIGABYTE") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!gigabyteLabel.isVisible()) {
					gigabyte = false;
				}
			}
		};
		filterForm.add(gigabyteLabel);
		
// MSI AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox msiCheckBox = new AjaxCheckBox("msiCheckBox", new PropertyModel<Boolean>(this, "msi")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(msiCheckBox);
		
		// MSI Label
		msiLabel = new Label("msiLabel", "     MSI") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!msiLabel.isVisible()) {
					msi = false;
				}
			}
		};
		filterForm.add(msiLabel);
		
// Socket 1151 AjaxCheckBox //////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox socket1151CheckBox = new AjaxCheckBox("socket1151CheckBox", new PropertyModel<Boolean>(this, "socket1151")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(socket1151CheckBox);
		
		// Socket 1151 Label
		socket1151Label = new Label("socket1151Label", "     1151") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!socket1151Label.isVisible()) {
					socket1151 = false;
				}
			}
		};
		filterForm.add(socket1151Label);
		
// Socket AM4 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox socketAM4CheckBox = new AjaxCheckBox("socketAM4CheckBox", new PropertyModel<Boolean>(this, "socketAM4")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(socketAM4CheckBox);
		
		// Socket AM4 Label
		socketAM4Label = new Label("socketAM4Label", "     AM4") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!socketAM4Label.isVisible()) {
					socketAM4 = false;
				}
			}
		};
		filterForm.add(socketAM4Label);
		
// Socket TR4 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox socketTR4CheckBox = new AjaxCheckBox("socketTR4CheckBox", new PropertyModel<Boolean>(this, "socketTR4")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(socketTR4CheckBox);
		
		// Socket TR4 Label
		socketTR4Label = new Label("socketTR4Label", "     TR4") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!socketTR4Label.isVisible()) {
					socketTR4 = false;
				}
			}
		};
		filterForm.add(socketTR4Label);
		
// AMD B450 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox amdB450CheckBox = new AjaxCheckBox("amdB450CheckBox", new PropertyModel<Boolean>(this, "amdB450")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(amdB450CheckBox);
		
		// AMD B450 Label
		amdB450Label = new Label("amdB450Label", "     AMD B450") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdB450Label.isVisible()) {
					amdB450 = false;
				}
			}
		};
		filterForm.add(amdB450Label);
		
// AMD X370 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox amdX370CheckBox = new AjaxCheckBox("amdX370CheckBox", new PropertyModel<Boolean>(this, "amdX370")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		}; 
		filterForm.add(amdX370CheckBox);
		
		// AMD X370 Label
		amdX370Label = new Label("amdX370Label", "     AMD X370") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdX370Label.isVisible()) {
					amdX370 = false;
				}
			}
		};
		filterForm.add(amdX370Label);
		
// AMD X399 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox amdX399CheckBox = new AjaxCheckBox("amdX399CheckBox", new PropertyModel<Boolean>(this, "amdX399")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(amdX399CheckBox);
		
		// AMD X399 Label
		amdX399Label = new Label("amdX399Label", "     AMD X399") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdX399Label.isVisible()) {
					amdX399 = false;
				}
			}
		};
		filterForm.add(amdX399Label);
		
// AMD X470 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox amdX470CheckBox = new AjaxCheckBox("amdX470CheckBox", new PropertyModel<Boolean>(this, "amdX470")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(amdX470CheckBox);
		
		// AMD X470 Label
		amdX470Label = new Label("amdX470Label", "     AMD X470") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdX470Label.isVisible()) {
					amdX470 = false;
				}
			}
		};
		filterForm.add(amdX470Label);
		
// Intel Z390 AjaxCheckBox ////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox intelZ390CheckBox = new AjaxCheckBox("intelZ390CheckBox", new PropertyModel<Boolean>(this, "intelZ390")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(intelZ390CheckBox);
		
		// Intel Z390 Label
		intelZ390Label = new Label("intelZ390Label", "     Intel Z390") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelZ390Label.isVisible()) {
					intelZ390 = false;
				}
			}
		};
		filterForm.add(intelZ390Label);
		
// 4x DDR4 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox memorySlot4CheckBox = new AjaxCheckBox("memorySlot4CheckBox", new PropertyModel<Boolean>(this, "memorySlot4")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memorySlot4CheckBox);
		
		// 4x DDR4 Label
		memorySlot4Label = new Label("memorySlot4Label", "     4x DDR4") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memorySlot4Label.isVisible()) {
					memorySlot4 = false;
				}
			}
		};
		filterForm.add(memorySlot4Label);
		
// 8x DDR4 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox memorySlot8CheckBox = new AjaxCheckBox("memorySlot8CheckBox", new PropertyModel<Boolean>(this, "memorySlot8")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memorySlot8CheckBox);
		
		// 8x DDR8 Label
		memorySlot8Label = new Label("memorySlot8Label", "     8x DDR4") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memorySlot8Label.isVisible()) {
					memorySlot8 = false;
				}
			}
		};
		filterForm.add(memorySlot8Label);
		
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
				
				if(!atxLabel.isVisible()) {
					atx = false;
				}
			}
		};
		filterForm.add(atxLabel);
		
// E-ATX AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox eAtxCheckBox = new AjaxCheckBox("eAtxCheckBox", new PropertyModel<Boolean>(this, "eAtx")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(eAtxCheckBox);
		
		// E-ATX Label
		eAtxLabel = new Label("eAtxLabel", "     E-ATX") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!eAtxLabel.isVisible()) {
					eAtx = false;
				}
			}
		};
		filterForm.add(eAtxLabel);
	}
	
}
