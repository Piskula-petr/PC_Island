package cz.pcisland.processors;

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
 * 	Třída procesorů:
 * 
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */

public class ProcessorsPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean amd = false;
	private Boolean intel = false;
	
	private Boolean socket1151 = false;
	private Boolean socketAM4 = false;
	private Boolean socketTR4 = false;
	
	private Boolean coffeeLake = false;
	private Boolean zen = false;
	private Boolean zen2 = false;
	
	private Boolean cores4 = false;
	private Boolean cores6 = false;
	private Boolean cores8 = false;
	private Boolean cores12 = false;

	private Boolean amdA320 = false;
	private Boolean amdB350 = false;
	private Boolean amdB450 = false;
	private Boolean amdX370 = false;
	private Boolean amdX399 = false;
	private Boolean amdX470 = false;
	private Boolean amdX570 = false;
	private Boolean intelB360 = false;
	private Boolean intelB365 = false;
	private Boolean intelH310 = false;
	private Boolean intelH370 = false;
	private Boolean intelZ370 = false;
	private Boolean intelZ390 = false;
	
	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private ProcessorDAO processorDAO = new ProcessorDAOImpl();
	private List<Product> processorsInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel,  amdLabel, intelLabel, socket1151Label, socketAM4Label, socketTR4Label, coffeeLakeLabel, 
				  zenLabel, zen2Label, cores4Label, cores6Label, cores8Label, cores12Label, amdA320Label, amdB350Label,
				  amdB450Label, amdX370Label, amdX399Label, amdX470Label, amdX570Label, intelB360Label, intelB365Label,
				  intelH310Label, intelH370Label, intelZ370Label, intelZ390Label;
	
// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public ProcessorsPage() {
		
		// Nastavení titulku
		setTitle(new Model("Procesory (CPU) | PC Island"));
		
		// Nejprodávanější (procesory) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(processorDAO.getTopSellingProcessors());
				
		// Produkty (procesory) PageableListView (ProductPage)
		productsPageableListView.setList(processorDAO.getAllProcessors());
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
				setResponsePage(ProcessorsPage.class);
			}
		});
		
// Filtr Form /////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny procesory
				products = processorDAO.getAllProcessors();
				
				// Nastavení cenového ropětí
				processorsInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Procesory v cenovém rozpětí / Všechny procesory
				if (processorsInPriceRange.size() != 0) {
					products = new ArrayList<>(processorsInPriceRange);
				
				} else products = processorDAO.getAllProcessors();
				
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
				
// Dostupnost /////////////////////////////////////////////////////////////////////////////////////////
				
				List<Product> filteredProcessors = new ArrayList<>();
				
				for (Product processor : products) {
					
					// Skladem
					if (onStock) {
						
						if (processor.getStock() > 0) {
							filteredProcessors.add(processor);
						}
					}
				}
				
				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					products = filteredProcessors;
				}
				
// Výrobce ////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredProcessors = new ArrayList<>();
				
				if (amd && intel) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product processor : products) {
						
						// AMD 
						if (amd) {
							
							if (processor.getParametrs().get("company").equals("AMD")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel
						if (intel) {
							
							if (processor.getParametrs().get("company").equals("Intel")) {
								filteredProcessors.add(processor);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					products = filteredProcessors;
				}
				
// Patice /////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredProcessors = new ArrayList<>();
					
				if (socket1151 && socketAM4 && socketTR4) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product processor : products) {
						
						// Socket 1151
						if (socket1151) {
							
							if (processor.getParametrs().get("socket").equals("1151")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Socket AM4
						if (socketAM4) {
							
							if (processor.getParametrs().get("socket").equals("AM4")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Socket TR4
						if (socketTR4) {
							
							if (processor.getParametrs().get("socket").equals("TR4")) {
								filteredProcessors.add(processor);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					products = filteredProcessors;
				}
				
// Kódové označení ////////////////////////////////////////////////////////////////////////////////////
				
				filteredProcessors = new ArrayList<>();
				
 				if (coffeeLake && zen && zen2) {
 					// Žádná akce v případě všech možnosti
 				} else {
 					
 					for (Product processor : products) {
 	 					
 	 					// Coffee Lake
 	 					if (coffeeLake) {
 	 						
 	 						if (processor.getParametrs().get("codeName").equals("Coffee Lake")) {
 	 							filteredProcessors.add(processor);
 		 					}	
 	 					} 
 	 					
 	 					// Zen
 	 					if (zen) {
 	 						
 	 						if (processor.getParametrs().get("codeName").equals("Zen")) {
 	 							filteredProcessors.add(processor);
 			 				}
 	 					}
 	 					
 	 					// Zen 2
 	 					if (zen2) {
 	 						
 	 						if (processor.getParametrs().get("codeName").equals("Zen 2")) {
 	 							filteredProcessors.add(processor);
 			 				}
 	 					}
 	 				}
 				}
 				
 				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					products = filteredProcessors;
				}

// Počet jader procesoru //////////////////////////////////////////////////////////////////////////////
				
 				filteredProcessors = new ArrayList<>();

				if (cores4 && cores6 && cores8 && cores12) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product processor : products) {
						
						// 4 Cores
						if (cores4) {
							
							if (processor.getParametrs().get("numberOfCores").equals("4")) {
								filteredProcessors.add(processor);
							}
						}
						
						// 6 Cores
						if (cores6) {
							
							if (processor.getParametrs().get("numberOfCores").equals("6")) {
								filteredProcessors.add(processor);
							}
						}
						
						// 8 Cores
						if (cores8) {
							
							if (processor.getParametrs().get("numberOfCores").equals("8")) {
								filteredProcessors.add(processor);
							}
						}
						
						// 12 Cores
						if (cores12) {
							
							if (processor.getParametrs().get("numberOfCores").equals("12")) {
								filteredProcessors.add(processor);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					products = filteredProcessors;
				}
				
// Podpora chipsetu ///////////////////////////////////////////////////////////////////////////////////
				
				filteredProcessors = new ArrayList<>();

				if (amdX399 && (amdA320 || amdB350 || amdB450 || amdX370 || amdX470 || amdX570)
					&& (intelB360 || intelB365 || intelH310 || intelH370 || intelZ370 || intelZ390)) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product processor : products) {
						
						// AMD A320
						if (amdA320) {
							
							if (processor.getParametrs().get("chipset").contains("A320")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD B350
						if (amdB350) {
							
							if (processor.getParametrs().get("chipset").contains("B350")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD B450
						if (amdB450) {
							
							if (processor.getParametrs().get("chipset").contains("B450")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD X370
						if (amdX370) {
							
							if (processor.getParametrs().get("chipset").contains("X370")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD X399
						if (amdX399) {
						
							if (processor.getParametrs().get("chipset").equals("X399")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD X470
						if (amdX470) {
							
							if (processor.getParametrs().get("chipset").contains("X470")) {
								filteredProcessors.add(processor);
							}
						}
						
						// AMD X570
						if (amdX570) {
							
							if (processor.getParametrs().get("chipset").contains("X570")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel B360
						if (intelB360) {
						
							if (processor.getParametrs().get("chipset").contains("B360")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel B365
						if (intelB365) {
						
							if (processor.getParametrs().get("chipset").contains("B365")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel H310
						if (intelH310) {
						
							if (processor.getParametrs().get("chipset").contains("H310")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel H370
						if (intelH370) {
						
							if (processor.getParametrs().get("chipset").contains("H370")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel Z370
						if (intelZ370) {
						
							if (processor.getParametrs().get("chipset").contains("Z370")) {
								filteredProcessors.add(processor);
							}
						}
						
						// Intel Z390
						if (intelZ390) {
						
							if (processor.getParametrs().get("chipset").contains("Z390")) {
								filteredProcessors.add(processor);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných procesorů
				if (!filteredProcessors.isEmpty()) {
					
					// Odstranění duplicit
					products = filteredProcessors.stream().distinct().collect(Collectors.toList());
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);
				
				amdLabel.setVisible(false);
				intelLabel.setVisible(false);
				
				socket1151Label.setVisible(false);
				socketAM4Label.setVisible(false);
				socketTR4Label.setVisible(false);
				
				coffeeLakeLabel.setVisible(false);
				zenLabel.setVisible(false);
				zen2Label.setVisible(false);
				
				cores4Label.setVisible(false);
				cores6Label.setVisible(false);
				cores8Label.setVisible(false);
				cores12Label.setVisible(false);
				
				amdA320Label.setVisible(false);
				amdB350Label.setVisible(false);
				amdB450Label.setVisible(false);
				amdX370Label.setVisible(false);
				amdX399Label.setVisible(false);
				amdX470Label.setVisible(false);
				amdX570Label.setVisible(false);
				intelB360Label.setVisible(false);
				intelB365Label.setVisible(false);
				intelH310Label.setVisible(false);
				intelH370Label.setVisible(false);
				intelZ370Label.setVisible(false);
				intelZ390Label.setVisible(false);	
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product processor : products) {
						
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
						
					if (processor.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!amd && !intel && !socket1151 && !socketAM4 && !socketTR4 && !coffeeLake 
						&& !zen && !zen2 && !cores4 && !cores6 && !cores8 && !cores12  && !amdA320 && !amdB350 && !amdB450 
						&& !amdX370 && !amdX399 && !amdX470 && !amdX570 && !intelB360 && !intelB365 && !intelH310 && !intelH370 
						&& !intelZ370 && !intelZ390)) {
						
						onStockLabel.setVisible(true);
					}
						
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (processor.getParametrs().get("company").equals("AMD")) {
						amdLabel.setVisible(true);
					
					} else if (processor.getParametrs().get("company").equals("Intel")) {
						intelLabel.setVisible(true);
					}
					
					if ((amd || intel) && (!socket1151 && !socketAM4 && !socketTR4 && !coffeeLake && !zen && !zen2
						&& !cores4 && !cores6 && !cores8 && !cores12 && !amdA320 && !amdB350 && !amdB450 
						&& !amdX370 && !amdX399 && !amdX470 && !amdX570 && !intelB360 && !intelB365 && !intelH310 && !intelH370 
						&& !intelZ370 && !intelZ390)) {
						
						amdLabel.setVisible(true);
						intelLabel.setVisible(true);
					}
						
// Patice ////////////////////////////////////////////////////////////////////////////////////////////
						
					if (processor.getParametrs().get("socket").equals("1151")) {
						socket1151Label.setVisible(true);
					
					} else if (processor.getParametrs().get("socket").equals("AM4")) {
						socketAM4Label.setVisible(true);
					
					} else if (processor.getParametrs().get("socket").equals("TR4")) {
						socketTR4Label.setVisible(true);
					}
					
					if ((socket1151 || socketAM4 || socketTR4) && (!amd && !intel && !coffeeLake && !zen && !zen2
						&& !cores4 && !cores6 && !cores8 && !cores12 && !amdA320 && !amdB350 && !amdB450 
						&& !amdX370 && !amdX399 && !amdX470 && !amdX570 && !intelB360 && !intelB365 && !intelH310 && !intelH370 
						&& !intelZ370 && !intelZ390)) {
						
						socket1151Label.setVisible(true);
						socketAM4Label.setVisible(true);
						socketTR4Label.setVisible(true);
					}
						
// Kódové označení ///////////////////////////////////////////////////////////////////////////////////
						
					if (processor.getParametrs().get("codeName").equals("Coffee Lake")) {
						coffeeLakeLabel.setVisible(true);
					
					} else if (processor.getParametrs().get("codeName").equals("Zen")) {
						zenLabel.setVisible(true);
					
					} else if (processor.getParametrs().get("codeName").equals("Zen 2")) {
						zen2Label.setVisible(true);
					}
					
					if ((coffeeLake || zen || zen2) && (!amd && !intel && !socket1151 && !socketAM4 && !socketTR4 
						&& !cores4 && !cores6 && !cores8 && !cores12 && !amdA320 && !amdB350 && !amdB450 
						&& !amdX370 && !amdX399 && !amdX470 && !amdX570 && !intelB360 && !intelB365 && !intelH310 
						&& !intelH370 && !intelZ370 && !intelZ390)) {
						
						coffeeLakeLabel.setVisible(true);
						zenLabel.setVisible(true);
						zen2Label.setVisible(true);
					}
						
// Počet jader procesoru /////////////////////////////////////////////////////////////////////////////
						
					if (processor.getParametrs().get("numberOfCores").equals("4")) {
						cores4Label.setVisible(true);
					
					} else if (processor.getParametrs().get("numberOfCores").equals("6")) {
						cores6Label.setVisible(true);
					
					} else if (processor.getParametrs().get("numberOfCores").equals("8")) {
						cores8Label.setVisible(true);
					
					} else if (processor.getParametrs().get("numberOfCores").equals("12")) {
						cores12Label.setVisible(true);
					}  
					
					if ((cores4 || cores6 || cores8 || cores12) && (!amd && !intel && !socket1151 && !socketAM4 
						&& !socketTR4 && !coffeeLake && !zen && !zen2 && !amdA320 && !amdB350 && !amdB450 
						&& !amdX370 && !amdX399 && !amdX470 && !amdX570 && !intelB360 && !intelB365 && !intelH310 && !intelH370 
						&& !intelZ370 && !intelZ390)) {
						
						cores4Label.setVisible(true);
						cores6Label.setVisible(true);
						cores8Label.setVisible(true);
						cores12Label.setVisible(true);
					}
					
// Podpora chipsetu //////////////////////////////////////////////////////////////////////////////////
					
					if (processor.getParametrs().get("chipset").contains("B360")
						|| processor.getParametrs().get("chipset").contains("B365")
						|| processor.getParametrs().get("chipset").contains("H310")
						|| processor.getParametrs().get("chipset").contains("H370")
						|| processor.getParametrs().get("chipset").contains("Z370")
						|| processor.getParametrs().get("chipset").contains("Z390")) {
						
						intelB360Label.setVisible(true);
						intelB365Label.setVisible(true);
						intelH310Label.setVisible(true);
						intelH370Label.setVisible(true);
						intelZ370Label.setVisible(true);
						intelZ390Label.setVisible(true);
					
					} else if (processor.getParametrs().get("chipset").contains("A320")
							   || processor.getParametrs().get("chipset").contains("B350")
							   || processor.getParametrs().get("chipset").contains("B450")
							   || processor.getParametrs().get("chipset").contains("X370")
							   || processor.getParametrs().get("chipset").contains("X470")
							   || processor.getParametrs().get("chipset").contains("X570")){
						
						   		amdA320Label.setVisible(true);
								amdB350Label.setVisible(true);
								amdB450Label.setVisible(true);
								amdX370Label.setVisible(true);
								amdX470Label.setVisible(true);
								amdX570Label.setVisible(true);
					
					} else if (processor.getParametrs().get("chipset").equals("X399")) {
						amdX399Label.setVisible(true);
					}
					
					if ((amdA320 || amdB350 || amdB450 || amdX370 || amdX399 || amdX470 || amdX570 || intelB360 
						|| intelB365 || intelH310 || intelH370 || intelZ370 || intelZ390)
						&& (!amd && !intel && !socket1151 && !socketAM4 && !socketTR4 && !coffeeLake 
						&& !zen && !zen2 && !cores4 && !cores6 && !cores8 && !cores12)) {
						
						amdA320Label.setVisible(true);
						amdB350Label.setVisible(true);
						amdB450Label.setVisible(true);
						amdX370Label.setVisible(true);
						amdX399Label.setVisible(true);
						amdX470Label.setVisible(true);
						amdX570Label.setVisible(true);
						intelB360Label.setVisible(true);
						intelB365Label.setVisible(true);
						intelH310Label.setVisible(true);
						intelH370Label.setVisible(true);
						intelZ370Label.setVisible(true);
						intelZ390Label.setVisible(true);
					}
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product processor : products) {
			
			if (MAX_PRICE < processor.getPrice()) {
				MAX_PRICE = processor.getPrice();
			}
			
			if (MIN_PRICE > processor.getPrice()) {
				MIN_PRICE = processor.getPrice();
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
		
		
// AMD AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
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
		
// Intel AjaxCheckBox ////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelCheckBox = new AjaxCheckBox("intelCheckBox", new PropertyModel<Boolean>(this, "intel")) {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(intelCheckBox);
		
		// Intel Label
		intelLabel = new Label("intelLabel", "     Intel") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelLabel.isVisible()) {
					intel = false;
				}
			}
		};
		filterForm.add(intelLabel);
		
// socket 1151 AjaxCheckBox //////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox socket1151CheckBox = new AjaxCheckBox("socket1151CheckBox", new PropertyModel<Boolean>(this, "socket1151")) {
	
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(socket1151CheckBox);
		
		// socket 1151 Label
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
		
// socket AM4 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox socketAM4CheckBox = new AjaxCheckBox("socketAM4CheckBox", new PropertyModel<Boolean>(this, "socketAM4")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(socketAM4CheckBox);
		
		// socket AM4 Label
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

// socket TR4 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
	    
	    AjaxCheckBox socketTR4CheckBox = new AjaxCheckBox("socketTR4CheckBox", new PropertyModel<Boolean>(this, "socketTR4")) {
	 		
	 		@Override
	 		protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
	 			target.add(markupContainer);
	 		}
	 	};
	 	filterForm.add(socketTR4CheckBox);
	 		
	 	// socket TR4 Label
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
	    
// Coffee Lake AjaxCheckBox //////////////////////////////////////////////////////////////////////////
	 	
	 	AjaxCheckBox coffeeLakeCheckBox = new AjaxCheckBox("coffeeLakeCheckBox", new PropertyModel<Boolean>(this, "coffeeLake")) {
	 			
	 		@Override
	 		protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
	 			target.add(markupContainer);
	 		}
	 	};
	 	filterForm.add(coffeeLakeCheckBox);

	 	// Coffe Lake Label
	 	 coffeeLakeLabel = new Label("coffeeLakeLabel", "     Coffee Lake") {
	 		 
	 		 @Override
	 		protected void onConfigure() {
	 			super.onConfigure();
	 			
	 			if (!coffeeLakeLabel.isVisible()) {
					coffeeLake = false;
				}
	 		}
	 	 };
	 	 filterForm.add(coffeeLakeLabel);	 	

// Zen AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
	 	
	 	 AjaxCheckBox zenCheckBox = new AjaxCheckBox("zenCheckBox", new PropertyModel<Boolean>(this, "zen")) {
		 		
		 	@Override
		 	protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
		 	}
		 };
		 filterForm.add(zenCheckBox);	 	 	

		 // Zen Label
		 zenLabel = new Label("zenLabel", "     Zen") {
			 
			 @Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!zenLabel.isVisible()) {
					zen = false;
				}
			}
		 };
	 	 filterForm.add(zenLabel);		 
		
// Zen 2 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
 	
		 AjaxCheckBox zen2CheckBox = new AjaxCheckBox("zen2CheckBox", new PropertyModel<Boolean>(this, "zen2")) {
		 		
		 	@Override
		 	protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
		 	}
		 };
		 filterForm.add(zen2CheckBox);	 	 	
		
		 // Zen 2 Label
		 zen2Label = new Label("zen2Label", "     Zen 2") {
			 
			 @Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!zen2Label.isVisible()) {
					zen2 = false;
				}
			}
		 };
		 filterForm.add(zen2Label);
	 	 
// 4 Cores AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
	 	 
	 	 AjaxCheckBox fourCoresCheckBox = new AjaxCheckBox("fourCoresCheckBox", new PropertyModel<Boolean>(this, "cores4")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(fourCoresCheckBox);
		
		// 4 Cores Label
		cores4Label = new Label("fourCoresLabel", "     4") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cores4Label.isVisible()) {
					cores4 = false;
				}
			}
		};
		filterForm.add(cores4Label);
	 	 
// 6 Cores AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
	 	AjaxCheckBox sixCoresCheckBox = new AjaxCheckBox("sixCoresCheckBox", new PropertyModel<Boolean>(this, "cores6")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(sixCoresCheckBox);
	
		// 6 Cores Label
		cores6Label = new Label("sixCoresLabel", "     6") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cores6Label.isVisible()) {
					cores6 = false;
				}
			}
		};
		filterForm.add(cores6Label);

// 8 Cores AjaxCheckBox ///////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox eightCoresCheckBox = new AjaxCheckBox("eightCoresCheckBox", new PropertyModel<Boolean>(this, "cores8")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(eightCoresCheckBox);
	
		// 8 Cores Label
		cores8Label = new Label("eightCoresLabel", "     8") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cores8Label.isVisible()) {
					cores8 = false;
				}
			}
		};
		filterForm.add(cores8Label);
		
// 12 Cores AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox twelveCoresCheckBox = new AjaxCheckBox("twelveCoresCheckBox", new PropertyModel<Boolean>(this, "cores12")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(twelveCoresCheckBox);
	
		// 12 Cores Label
		cores12Label = new Label("twelveCoresLabel", "     12") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cores12Label.isVisible()) {
					cores12 = false;
				}
			}
		};
		filterForm.add(cores12Label);
		
// AMD A320 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetA320CheckBox = new AjaxCheckBox("amdA320CheckBox", new PropertyModel<Boolean>(this, "amdA320")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetA320CheckBox);
		
		// AMD A320 Label
		amdA320Label = new Label("amdA320Label", "     AMD A320") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelZ390Label.isVisible()) {
					amdA320 = false;
				}
			}
		};
		filterForm.add(amdA320Label);
		
// AMD B350 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetB350CheckBox = new AjaxCheckBox("amdB350CheckBox", new PropertyModel<Boolean>(this, "amdB350")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetB350CheckBox);
		
		// AMD B350 Label
		amdB350Label = new Label("amdB350Label", "     AMD B350") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdB350Label.isVisible()) {
					amdB350 = false;
				}
			}
		};
		filterForm.add(amdB350Label);
		
// AMD B450 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetB450CheckBox = new AjaxCheckBox("amdB450CheckBox", new PropertyModel<Boolean>(this, "amdB450")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetB450CheckBox);
		
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
		
// AMD X370 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetX370CheckBox = new AjaxCheckBox("amdX370CheckBox", new PropertyModel<Boolean>(this, "amdX370")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetX370CheckBox);
		
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
		
// AMD X399 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetX399CheckBox = new AjaxCheckBox("amdX399CheckBox", new PropertyModel<Boolean>(this, "amdX399")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetX399CheckBox);
		
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
		
// AMD X470 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox chipsetX470CheckBox = new AjaxCheckBox("amdX470CheckBox", new PropertyModel<Boolean>(this, "amdX470")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetX470CheckBox);
		
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
		
// AMD X570 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox chipsetX570CheckBox = new AjaxCheckBox("amdX570CheckBox", new PropertyModel<Boolean>(this, "amdX570")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetX570CheckBox);
		
		// AMD X570 Label
		amdX570Label = new Label("amdX570Label", "     AMD X570") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!amdX570Label.isVisible()) {
					amdX570 = false;
				}
			}
		};
		filterForm.add(amdX570Label);
		
// Intel B360 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelB360CheckBox = new AjaxCheckBox("intelB360CheckBox", new PropertyModel<Boolean>(this, "intelB360")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelB360CheckBox);
		
		// Intel B360 Label
		intelB360Label = new Label("intelB360Label", "     Intel B360") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelB360Label.isVisible()) {
					intelB360 = false;
				}
			}
		};
		filterForm.add(intelB360Label);
		
// Intel B365 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelB365CheckBox = new AjaxCheckBox("intelB365CheckBox", new PropertyModel<Boolean>(this, "intelB365")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelB365CheckBox);
		
		// Intel B365 Label
		intelB365Label = new Label("intelB365Label", "     Intel B365") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelB365Label.isVisible()) {
					intelB365 = false;
				}
			}
		};
		filterForm.add(intelB365Label);
		
// Intel H310 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelH310CheckBox = new AjaxCheckBox("intelH310CheckBox", new PropertyModel<Boolean>(this, "intelH310")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelH310CheckBox);
		
		// Intel H310 Label
		intelH310Label = new Label("intelH310Label", "     Intel H310") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelH310Label.isVisible()) {
					intelH310 = false;
				}
			}
		};
		filterForm.add(intelH310Label);
		
// Intel H370 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelH370CheckBox = new AjaxCheckBox("intelH370CheckBox", new PropertyModel<Boolean>(this, "intelH370")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelH370CheckBox);
		
		// Intel H370 Label
		intelH370Label = new Label("intelH370Label", "     Intel H370") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelH370Label.isVisible()) {
					intelH370 = false;
				}
			}
		};
		filterForm.add(intelH370Label);
		
// Intel Z370 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelZ370CheckBox = new AjaxCheckBox("intelZ370CheckBox", new PropertyModel<Boolean>(this, "intelZ370")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelZ370CheckBox);
		
		// Intel Z370 Label
		intelZ370Label = new Label("intelZ370Label", "     Intel Z370") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelZ370Label.isVisible()) {
					intelZ370 = false;
				}
			}
		};
		filterForm.add(intelZ370Label);
		
// Intel Z390 AjaxCheckBox ///////////////////////////////////////////////////////////////////////////

		AjaxCheckBox chipsetZ390CheckBox = new AjaxCheckBox("chipsetZ390CheckBox", new PropertyModel<Boolean>(this, "intelZ390")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(chipsetZ390CheckBox);
		
		// Chipset Z390 Label
		intelZ390Label = new Label("chipsetZ390Label", "     Intel Z390") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelZ390Label.isVisible()) {
					intelZ390 = false;
				}
			}
		};
		filterForm.add(intelZ390Label);
	}
	
}
