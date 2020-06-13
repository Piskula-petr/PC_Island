package cz.pcisland.graphics_cards;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
 *	Třída grafických karet:
 *
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */
public class GraphicsCardsPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean asus = false;
	private Boolean gigabyte = false;
	private Boolean msi = false;
	
	private Boolean amd = false;
	private Boolean nvidia = false;
	
	private Boolean memory6 = false;
	private Boolean memory8 = false;
	private Boolean memory11 = false;
	private Boolean memory16 = false;
	
	private Boolean gddr5x = false;
	private Boolean gddr6 = false;
	private Boolean hbm2 = false;
	
	private Boolean displayPort = false;
	private Boolean dviD = false;
	private Boolean hdmi = false;
	private Boolean usbC = false;

	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private GraphicsCardDAO graphicsCardDAO = new GraphicsCardDAOImpl();
	private List<Product> graphicsCardsInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel, asusLabel, gigabyteLabel, msiLabel, amdLabel, nvidiaLabel, memory6Label, memory8Label, memory11Label,
				  memory16Label, gddr5xLabel, gddr6Label, hbm2Label, displayPortLabel, dviDLabel, hdmiLabel, usbCLabel;

// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public GraphicsCardsPage() {
		
		// Nastavení titulku
		setTitle(new Model("Grafické karty (GPU) | PC Island"));
		
		// Nejprodávanější (grafické karty) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(graphicsCardDAO.getTopSellingGraphicsCards());
		
		// Produkty (grafické karty) PageableListView (ProductPage)
		productsPageableListView.setList(graphicsCardDAO.getAllGraphicsCards());
		products = productsPageableListView.getList();

// Chybová hláška WebMarkupContainer /////////////////////////////////////////////////////////////////
		
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
				setResponsePage(GraphicsCardsPage.class);
			}
		});
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny grafické karty
				products = graphicsCardDAO.getAllGraphicsCards();
				
				// Nastavení cenového ropětí
				graphicsCardsInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Grafické karty v cenovém rozpětí / Všechny grafické karty
				if (graphicsCardsInPriceRange.size() != 0) {
					products = new ArrayList<>(graphicsCardsInPriceRange);
				
				} else products = graphicsCardDAO.getAllGraphicsCards();
				
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
				
				List<Product> filteredGraphicsCards = new ArrayList<>();
				
				for (Product graphicsCard : products) {
					
					// Skladem
					if (onStock) {
						
						if (graphicsCard.getStock() > 0) {
							filteredGraphicsCards.add(graphicsCard);
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					products = filteredGraphicsCards;
				}
				
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredGraphicsCards = new ArrayList<>();
				
				if (asus && gigabyte && msi) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product graphicsCard : products) {
						
						// ASUS
						if (asus) {
							
							if (graphicsCard.getParametrs().get("company").equals("ASUS")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// GIGABYTE
						if (gigabyte) {
							
							if (graphicsCard.getParametrs().get("company").equals("GIGABYTE")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// MSI
						if (msi) {
							
							if (graphicsCard.getParametrs().get("company").equals("MSI")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					products = filteredGraphicsCards;
				}
				
// Grafický čip //////////////////////////////////////////////////////////////////////////////////////
				
				filteredGraphicsCards = new ArrayList<>();
				
				if (amd && nvidia) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product graphicsCard : products) {
						
						// AMD
						if (amd) {
							
							if (graphicsCard.getParametrs().get("graphicsChip").equals("AMD")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// NVIDIA
						if (nvidia) {
							
							if (graphicsCard.getParametrs().get("graphicsChip").equals("NVIDIA")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					products = filteredGraphicsCards;
				}
				
// Velikost grafické paměti //////////////////////////////////////////////////////////////////////////
				
				filteredGraphicsCards = new ArrayList<>();
				
				if (memory6 && memory8 && memory11 && memory16) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product graphicsCard : products) {
						
						// 6 GB
						if (memory6) {
							
							if (graphicsCard.getParametrs().get("memorySize").equals("6144")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// 8 GB
						if (memory8) {
							
							if (graphicsCard.getParametrs().get("memorySize").equals("8192")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// 11 GB
						if (memory11) {
							
							if (graphicsCard.getParametrs().get("memorySize").equals("11264")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// 16 GB
						if (memory16) {
							
							if (graphicsCard.getParametrs().get("memorySize").equals("16384")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					products = filteredGraphicsCards;
				}
				
// Typ grafické paměti ///////////////////////////////////////////////////////////////////////////////

				filteredGraphicsCards = new ArrayList<>();
				
				if (gddr5x && gddr6 && hbm2) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product graphicsCard : products) {
						
						// GDDR5X
						if (gddr5x) {
							
							if (graphicsCard.getParametrs().get("memoryType").equals("GDDR5X")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// GDDR6
						if (gddr6) {
							
							if (graphicsCard.getParametrs().get("memoryType").equals("GDDR6")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// HBM2
						if (hbm2) {
							
							if (graphicsCard.getParametrs().get("memoryType").equals("HBM2")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					products = filteredGraphicsCards;
				}
				
// Konektory /////////////////////////////////////////////////////////////////////////////////////////
				
				filteredGraphicsCards = new ArrayList<>();
				
				if (displayPort && dviD && usbC && hdmi) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product graphicsCard : products) {

						// DisplayPort
						if (displayPort) {
							
							if (graphicsCard.getParametrs().get("outputs").contains("DisplayPort")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// DVI-D
						if (dviD) {
							
							if (graphicsCard.getParametrs().get("outputs").contains("DVI-D")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// HDMI
						if (hdmi) {
							
							if (graphicsCard.getParametrs().get("outputs").contains("HDMI")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
						
						// USB-C
						if (usbC) {
							
							if (graphicsCard.getParametrs().get("outputs").contains("USB-C")) {
								filteredGraphicsCards.add(graphicsCard);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných grafických karet
				if (!filteredGraphicsCards.isEmpty()) {
					
					// Odstranění duplicit
					products = filteredGraphicsCards.stream().distinct().collect(Collectors.toList());
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);
				
				asusLabel.setVisible(false);
				gigabyteLabel.setVisible(false);
				msiLabel.setVisible(false);
				
				amdLabel.setVisible(false);
				nvidiaLabel.setVisible(false);
				
				memory6Label.setVisible(false);
				memory8Label.setVisible(false);
				memory11Label.setVisible(false);
				memory16Label.setVisible(false);
				
				gddr5xLabel.setVisible(false);
				gddr6Label.setVisible(false);
				hbm2Label.setVisible(false);
				
				displayPortLabel.setVisible(false);
				dviDLabel.setVisible(false);
				hdmiLabel.setVisible(false);
				usbCLabel.setVisible(false);
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product graphicsCard : products) {
					
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
					
					if (graphicsCard.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!asus && !gigabyte && !msi && !amd && !nvidia && !memory6 && !memory8 && !memory11
						&& !memory16 && !gddr5x && !gddr6 && !hbm2 && !displayPort && !dviD && !hdmi && !usbC)) {
						
						onStockLabel.setVisible(true);
					}
					
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (graphicsCard.getParametrs().get("company").equals("MSI")) {
						msiLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("company").equals("ASUS")) {
						asusLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("company").equals("GIGABYTE")) {
						gigabyteLabel.setVisible(true);
					}
					
					if ((asus || gigabyte || msi) && (!amd && !nvidia && !memory6 && !memory8 && !memory11
						&& !memory16 && !gddr5x && !gddr6 && !hbm2 && !displayPort && !dviD && !hdmi && !usbC)) {
						
						msiLabel.setVisible(true);
						asusLabel.setVisible(true);
						gigabyteLabel.setVisible(true);
					}
					
// Grafický čip //////////////////////////////////////////////////////////////////////////////////////
					
					if (graphicsCard.getParametrs().get("graphicsChip").equals("NVIDIA")) {
						nvidiaLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("graphicsChip").equals("AMD")) {
						amdLabel.setVisible(true);
					}
					
					if ((amd || nvidia) && (!asus && !gigabyte && !msi && !memory6 && !memory8 && !memory11
						&& !memory16 && !gddr5x && !gddr6 && !hbm2 && !displayPort && !dviD && !hdmi && !usbC)) {
						
						nvidiaLabel.setVisible(true);
						amdLabel.setVisible(true);
					}
					
// Velikost grafické paměti //////////////////////////////////////////////////////////////////////////
					
					if (graphicsCard.getParametrs().get("memorySize").equals("6144")) {
						memory6Label.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("memorySize").equals("8192")) {
						memory8Label.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("memorySize").equals("11264")) {
						memory11Label.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("memorySize").equals("16384")) {
						memory16Label.setVisible(true);
					}
					
					if ((memory6 || memory8 || memory11 || memory16) && (!asus && !gigabyte && !msi && !amd && !nvidia
						&& !gddr5x && !gddr6 && !hbm2 && !displayPort && !dviD && !hdmi && !usbC)) {
						
						memory6Label.setVisible(true);
						memory8Label.setVisible(true);
						memory11Label.setVisible(true);
						memory16Label.setVisible(true);
					}
					
// Typ grafické paměti ///////////////////////////////////////////////////////////////////////////////
					
					if (graphicsCard.getParametrs().get("memoryType").equals("GDDR5X")) {
						gddr5xLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("memoryType").equals("GDDR6")) {
						gddr6Label.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("memoryType").equals("HBM2")) {
						hbm2Label.setVisible(true);
					}
					
					if ((gddr5x || gddr6 || hbm2) && (!asus && !gigabyte && !msi && !amd && !nvidia && !memory6 && !memory8
						&& !memory11 && !memory16 && !displayPort && !dviD && !hdmi && !usbC)) {
						
						gddr5xLabel.setVisible(true);
						gddr6Label.setVisible(true);
						hbm2Label.setVisible(true);
					}
					
// Konektory /////////////////////////////////////////////////////////////////////////////////////////

					if (graphicsCard.getParametrs().get("outputs").contains("DisplayPort")) {
						displayPortLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("outputs").contains("DVI-D")) {
						dviDLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("outputs").contains("USB-C")) {
						usbCLabel.setVisible(true);
					
					} else if (graphicsCard.getParametrs().get("outputs").contains("HDMI")) {
						hdmiLabel.setVisible(true);
					}
					
					if ((displayPort || dviD || usbC || hdmi) && (!asus && !gigabyte && !msi && !amd && !nvidia
						&& !memory6 && !memory8 && !memory11 && !memory16 && !gddr5x && !gddr6 && !hbm2)) {
						
						displayPortLabel.setVisible(true);
						dviDLabel.setVisible(true);
						hdmiLabel.setVisible(true);
						usbCLabel.setVisible(true);
					}
				}
			}
		};
		
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product graphicsCard : products) {
			
			if (MAX_PRICE < graphicsCard.getPrice()) {
				MAX_PRICE = graphicsCard.getPrice();
			}
			
			if (MIN_PRICE > graphicsCard.getPrice()) {
				MIN_PRICE = graphicsCard.getPrice();
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
	
// AMD CheckBox //////////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox amdCheckBox = new AjaxCheckBox("amdCheckBox", new PropertyModel<Boolean>(this, "amd")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(amdCheckBox);
		
		// AMD Label
		amdLabel = new Label("amdLabel", "     AMD") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdLabel.isVisible()) {
					amd = false;
				}
			}
		};
		filterForm.add(amdLabel);

// NVIDIA AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox nvidiaCheckBox = new AjaxCheckBox("nvidiaCheckBox", new PropertyModel<Boolean>(this, "nvidia")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(nvidiaCheckBox);
		
		// NVIDIA Label
		nvidiaLabel = new Label("nvidiaLabel", "     NVIDIA") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!nvidiaLabel.isVisible()) {
					nvidia = false;
				}
			}
		};
		filterForm.add(nvidiaLabel);
		
// 6 GB AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox memory6CheckBox = new AjaxCheckBox("memory6CheckBox", new PropertyModel<Boolean>(this, "memory6")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memory6CheckBox);
		
		// 6 GB Label
		memory6Label = new Label("memory6Label", "     6 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory6Label.isVisible()) {
					memory6 = false;
				}
			}
		};
		filterForm.add(memory6Label);
		
// 8 GB AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox memory8CheckBox = new AjaxCheckBox("memory8CheckBox", new PropertyModel<Boolean>(this, "memory8")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memory8CheckBox);
		
		// 8 GB Label
		memory8Label = new Label("memory8Label", "     8 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory8Label.isVisible()) {
					memory8 = false;
				}
			}
		};
		filterForm.add(memory8Label);
	
// 11 GB AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox memory11CheckBox = new AjaxCheckBox("memory11CheckBox", new PropertyModel<Boolean>(this, "memory11")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memory11CheckBox);
		
		// 11 GB Label
		memory11Label = new Label("memory11Label", "     11 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory11Label.isVisible()) {
					memory11 = false;
				}
			}
		};
		filterForm.add(memory11Label);
	
// 16 GB AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox memory16CheckBox = new AjaxCheckBox("memory16CheckBox", new PropertyModel<Boolean>(this, "memory16")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(memory16CheckBox);
		
		// 16 GB Label
		memory16Label = new Label("memory16Label", "     16 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory16Label.isVisible()) {
					memory16 = false;
				}
			}
		};
		filterForm.add(memory16Label);	
	
// GDDR5X AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox gddr5xCheckBox = new AjaxCheckBox("gddr5xCheckBox", new PropertyModel<Boolean>(this, "gddr5x")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(gddr5xCheckBox);
		
		// GDDR5X Label
		gddr5xLabel = new Label("gddr5xLabel", "     GDDR5X") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!gddr5xLabel.isVisible()) {
					gddr5x = false;
				}
			}
		};
		filterForm.add(gddr5xLabel);
	
// GDDR6 AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox gddr6CheckBox = new AjaxCheckBox("gddr6CheckBox", new PropertyModel<Boolean>(this, "gddr6")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(gddr6CheckBox);
		
		// GDDR6 Label
		gddr6Label = new Label("gddr6Label", "     GDDR6") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!gddr6Label.isVisible()) {
					gddr6 = false;
				}
			}
		};
		filterForm.add(gddr6Label);
	
// HBM2 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox hbm2CheckBox = new AjaxCheckBox("hbm2CheckBox", new PropertyModel<Boolean>(this, "hbm2")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(hbm2CheckBox);
		
		// HBM2 Label
		hbm2Label = new Label("hbm2Label", "     HBM2") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!hbm2Label.isVisible()) {
					hbm2 = false;
				}
			}
		};
		filterForm.add(hbm2Label);
	
// DisplayPort AjaxCheckBox //////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox displayPortCheckBox = new AjaxCheckBox("displayPortCheckBox", new PropertyModel<Boolean>(this, "displayPort")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(displayPortCheckBox);
		
		// DisplayPort Label
		displayPortLabel = new Label("displayPortLabel", "     DisplayPort") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!displayPortLabel.isVisible()) {
					displayPort = false;
				}
			}
		};
		filterForm.add(displayPortLabel);
	
// DVI-D AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox dviDCheckBox = new AjaxCheckBox("dviDCheckBox", new PropertyModel<Boolean>(this, "dviD")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(dviDCheckBox);
		
		// DVI-D Label
		dviDLabel = new Label("dviDLabel", "     DVI-D") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!dviDLabel.isVisible()) {
					dviD = false;
				}
			}
		};
		filterForm.add(dviDLabel);
		
// HDMI AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox hdmiCheckBox = new AjaxCheckBox("hdmiCheckBox", new PropertyModel<Boolean>(this, "hdmi")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(hdmiCheckBox);
		
		// HDMI Label
		hdmiLabel = new Label("hdmiLabel", "     HDMI") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!hdmiLabel.isVisible()) {
					hdmi = false;
				}
			}
		};
		filterForm.add(hdmiLabel);
		
// USB-C AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox usbCCheckBox = new AjaxCheckBox("usbCCheckBox", new PropertyModel<Boolean>(this, "usbC")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(usbCCheckBox);
		
		// USB-C Label
		usbCLabel = new Label("usbCLabel", "     USB-C") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!usbCLabel.isVisible()) {
					usbC = false;
				}
			}
		};
		filterForm.add(usbCLabel);
	}
	
}
