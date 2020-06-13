package cz.pcisland.memory;

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
 *	Třída operačních pamětí:
 *
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */

public class MemoryPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean corsair = false;
	private Boolean crucial = false;
	private Boolean gSkill = false;
	private Boolean hyperX = false;
	
	private Boolean memory16 = false;
	private Boolean memory32 = false;
	
	private Boolean frequency3000 = false;
	private Boolean frequency3200 = false;
	private Boolean frequency3600 = false;
	private Boolean frequency4000 = false;
	
	private Boolean cl15 = false;
	private Boolean cl16 = false;
	private Boolean cl19 = false;
	
	private Boolean intelXMP = false;
	private Boolean intelXMP2 = false;
	
	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private MemoryDAO memoryDAO = new MemoryDAOImpl();
	private List<Product> memoryInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel, corsairLabel, crucialLabel, gSkillLabel, hyperXLabel, memory16Label, memory32Label,
			frequency3000Label, frequency3200Label, frequency3600Label, frequency4000Label, cl15Label, cl16Label, cl19Label, intelXMPLabel, intelXMP2Label;
	
// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public MemoryPage() {
		
		// Nastavení titulku
		setTitle(new Model("Operační paměti (RAM) | PC Island"));
		
		// Nejprodávanější (operační paměti) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(memoryDAO.getTopSellingMemory());
				
		// Produkty (operační paměti) PageableListView (ProductPage)
		productsPageableListView.setList(memoryDAO.getAllMemory());
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
				setResponsePage(MemoryPage.class);
			}
		});
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny oprační paměti
				products = memoryDAO.getAllMemory();
				
				// Nastavení cenového rozpětí
				memoryInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Operační paměti v cenovém rozpětí / Všechny procesory
				if (memoryInPriceRange.size() != 0) {
					products = new ArrayList<>(memoryInPriceRange);
				
				} else products = memoryDAO.getAllMemory();
				
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
				
				List<Product> filteredMemory = new ArrayList<>();
				
				for (Product memory : products) {
					
					// Skladem
					if (onStock) {
						
						if (memory.getStock() > 0) {
							filteredMemory.add(memory);
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMemory = new ArrayList<>();
				
				if (corsair && crucial && gSkill && hyperX) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product memory : products) {
						
						// Corsair
						if (corsair) {
							
							if (memory.getParametrs().get("company").equals("Corsair")) {
								filteredMemory.add(memory);
							}
						}
						
						// Crucial
						if (crucial) {
							
							if (memory.getParametrs().get("company").equals("Crucial")) {
								filteredMemory.add(memory);
							}
						}
						
						// G.SKILL
						if (gSkill) {
							
							if (memory.getParametrs().get("company").equals("G.SKILL")) {
								filteredMemory.add(memory);
							}
						}
						
						// HyperX
						if (hyperX) {
							
							if (memory.getParametrs().get("company").equals("HyperX")) {
								filteredMemory.add(memory);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
// Konfigurace paměti ////////////////////////////////////////////////////////////////////////////////
				
				filteredMemory = new ArrayList<>();
				
				if (memory16 && memory32) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product memory : products) {
						
						// 2 x 8 GB
						if (memory16) {
							
							if (memory.getParametrs().get("memorySize").equals("2x 8")) {
								filteredMemory.add(memory);
							}
						}
						
						// 2 x 16 GB
						if (memory32) {
							
							if (memory.getParametrs().get("memorySize").equals("2x 16")) {
								filteredMemory.add(memory);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
// Pracovní frekvence ////////////////////////////////////////////////////////////////////////////////
				
				filteredMemory = new ArrayList<>();
				
				if (frequency3000 && frequency3200 && frequency3600 && frequency4000) {
					// Žádná akce v případě všech možnosti
				} else {

					for (Product memory : products) {
						
						// 3000 MHz
						if (frequency3000) {
							
							if (memory.getParametrs().get("memoryFrequency").equals("3000")) {
								filteredMemory.add(memory);
							}
						}
						
						// 3200 MHz
						if (frequency3200) {
							
							if (memory.getParametrs().get("memoryFrequency").equals("3200")) {
								filteredMemory.add(memory);
							}
						}
						
						// 3600 MHz
						if (frequency3600) {
							
							if (memory.getParametrs().get("memoryFrequency").equals("3600")) {
								filteredMemory.add(memory);
							}
						}
						
						// 4000 MHz
						if (frequency4000) {
							
							if (memory.getParametrs().get("memoryFrequency").equals("4000")) {
								filteredMemory.add(memory);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
// Latence ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMemory = new ArrayList<>();
				
				if (cl15 && cl16 && cl19) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product memory : products) {
						
						// CL15
						if (cl15) {
						
							if (memory.getParametrs().get("latency").equals("CL15")) {
								filteredMemory.add(memory);
							}
						}
						
						// CL16
						if (cl16) {
							
							if (memory.getParametrs().get("latency").equals("CL16")) {
								filteredMemory.add(memory);
							}
						}
						
						// CL19
						if (cl19) {
							
							if (memory.getParametrs().get("latency").equals("CL19")) {
								filteredMemory.add(memory);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
// Verze XMP /////////////////////////////////////////////////////////////////////////////////////////
				
				filteredMemory = new ArrayList<>();
				
				if (intelXMP && intelXMP2) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product memory : products) {
						
						// Intel XMP
						if (intelXMP) {
						
							if (memory.getParametrs().get("XMP").equals("Intel XMP")) {
								filteredMemory.add(memory);
							}
						}
						
						// Intel XMP 2.0
						if (intelXMP2) {
							
							if (memory.getParametrs().get("XMP").equals("Intel XMP 2.0")) {
								filteredMemory.add(memory);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných operačních pamětí
				if (!filteredMemory.isEmpty()) {
					products = filteredMemory;
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);
				
				corsairLabel.setVisible(false);
				crucialLabel.setVisible(false);
				gSkillLabel.setVisible(false);
				hyperXLabel.setVisible(false);
				
				memory16Label.setVisible(false);
				memory32Label.setVisible(false);
				
				frequency3000Label.setVisible(false);
				frequency3200Label.setVisible(false);
				frequency3600Label.setVisible(false);
				frequency4000Label.setVisible(false);
				
				cl15Label.setVisible(false);
				cl16Label.setVisible(false);
				cl19Label.setVisible(false);
				
				intelXMPLabel.setVisible(false);
				intelXMP2Label.setVisible(false);
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product memory : products) {
					
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!corsair && !crucial && !gSkill && !hyperX && !memory16 && !memory32 && !frequency3000 
						&& !frequency3200 && !frequency3600 && !frequency4000 && !cl15 && !cl16 && !cl19 && !intelXMP && !intelXMP2)) {
						
						onStockLabel.setVisible(true);
					}
					
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getParametrs().get("company").equals("G.SKILL")) {
						gSkillLabel.setVisible(true);
					
					} else if (memory.getParametrs().get("company").equals("Corsair")) {
						corsairLabel.setVisible(true);
					
					} else if (memory.getParametrs().get("company").equals("Crucial")) {
						crucialLabel.setVisible(true);
					
					} else if (memory.getParametrs().get("company").equals("HyperX")) {
						hyperXLabel.setVisible(true);
					}
					
					if ((corsair || crucial || gSkill || hyperX) && (!memory16 && !memory32 && !frequency3000 
						&& !frequency3200 && !frequency3600 && !frequency4000 && !cl15 && !cl16 && !cl19 && !intelXMP && !intelXMP2)) {
						
						corsairLabel.setVisible(true);
						crucialLabel.setVisible(true);
						gSkillLabel.setVisible(true);
						hyperXLabel.setVisible(true);
					}
					
// Konfigurace paměti ////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getParametrs().get("memorySize").equals("2x 8")) {
						memory16Label.setVisible(true);
					
					} else if (memory.getParametrs().get("memorySize").equals("2x 16")) {
						memory32Label.setVisible(true);
					}
					
					if ((memory16 || memory32) && (!corsair && !crucial && !gSkill && !hyperX && !frequency3000 
						&& !frequency3200 && !frequency3600 && !frequency4000 && !cl15 && !cl16 && !cl19 && !intelXMP && !intelXMP2)) {
						
						memory16Label.setVisible(true);
						memory32Label.setVisible(true);
					}
					
// Pracovní frekvence ////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getParametrs().get("memoryFrequency").equals("3000")) {
						frequency3000Label.setVisible(true);
					
					} else if (memory.getParametrs().get("memoryFrequency").equals("3200")) {
						frequency3200Label.setVisible(true);
					
					} else if (memory.getParametrs().get("memoryFrequency").equals("3600")) {
						frequency3600Label.setVisible(true);
					
					} else if (memory.getParametrs().get("memoryFrequency").equals("4000")) {
						frequency4000Label.setVisible(true);
					}
					
					if ((frequency3000 || frequency3200 || frequency3600 || frequency4000) && (!corsair && !crucial 
						&& !gSkill && !hyperX && !memory16 && !memory32 && !cl15 && !cl16 && !cl19 && !intelXMP && !intelXMP2)) {
						
						frequency3000Label.setVisible(true);
						frequency3200Label.setVisible(true);
						frequency3600Label.setVisible(true);
						frequency4000Label.setVisible(true);
					}
					
// Latence ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getParametrs().get("latency").equals("CL15")) {
						cl15Label.setVisible(true);
					
					} else if (memory.getParametrs().get("latency").equals("CL16")) {
						cl16Label.setVisible(true);
					
					} else if (memory.getParametrs().get("latency").equals("CL19")) {
						cl19Label.setVisible(true);
					}
					
					if ((cl15 || cl16 || cl19) && (!hyperX && !corsair && !crucial && !gSkill && !memory16 && !memory32
						&& !frequency3000 && !frequency3200 && !frequency3600 && !intelXMP && !intelXMP2)) {
						
						cl15Label.setVisible(true);
						cl16Label.setVisible(true);
						cl19Label.setVisible(true);
					}	
					
// Verze XMP /////////////////////////////////////////////////////////////////////////////////////////
					
					if (memory.getParametrs().get("XMP").equals("Intel XMP")) {
						intelXMPLabel.setVisible(true);
					
					} else if (memory.getParametrs().get("XMP").equals("Intel XMP 2.0")) {
						intelXMP2Label.setVisible(true);
					}
					
					if ((intelXMP || intelXMP2) && (!corsair && !crucial && !hyperX && !gSkill && !memory16 && !memory32
						&& !frequency3000 && !frequency3200 && !frequency3600 && !frequency4000 && !cl15 && !cl16 && !cl19)) {
						
						intelXMPLabel.setVisible(true);
						intelXMP2Label.setVisible(true);
					}
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product memory : products) {
			
			if (MAX_PRICE < memory.getPrice()) {
				MAX_PRICE = memory.getPrice();
			}
			
			if (MIN_PRICE > memory.getPrice()) {
				MIN_PRICE = memory.getPrice();
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
	
// Skladem Label /////////////////////////////////////////////////////////////////////////////////////		
		
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
		
// Crucial AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox crucialCheckBox = new AjaxCheckBox("crucialCheckBox", new PropertyModel<Boolean>(this, "crucial")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(crucialCheckBox);
		
		// Crucial Label
		crucialLabel = new Label("crucialLabel", "     Crucial") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!crucialLabel.isVisible()) {
					crucial = false;
				}
			}
		};
		filterForm.add(crucialLabel);
		
// G.SKILL AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox gSkillCheckBox = new AjaxCheckBox("gSkillCheckBox", new PropertyModel<Boolean>(this, "gSkill")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(gSkillCheckBox);
		
		// G.SKILL Label
		gSkillLabel = new Label("gSkillLabel", "     G.SKILL") {
			 
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!gSkillLabel.isVisible()) {
					gSkill = false;
				}
			}
		};
		filterForm.add(gSkillLabel);
		
// HyperX AjaxCheckBox /////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox hyperXCheckBox = new AjaxCheckBox("hyperXCheckBox", new PropertyModel<Boolean>(this, "hyperX")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(hyperXCheckBox);
		
		// Kingstone Label
		hyperXLabel = new Label("hyperXLabel", "     HyperX") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!hyperXLabel.isVisible()) {
					hyperX = false;
				}
			}
		};
		filterForm.add(hyperXLabel);
		
// 2x 8 GB AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox memory16CheckBox = new AjaxCheckBox("memory16CheckBox", new PropertyModel<Boolean>(this, "memory16")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(memory16CheckBox);
		
		// 2x 8 GB Label
		memory16Label = new Label("memory16Label", "     2x 8 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory16Label.isVisible()) {
					memory16 = false;
				}
			}
		};
		filterForm.add(memory16Label);
		
// 2x 16 GB AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox memory32CheckBox = new AjaxCheckBox("memory32CheckBox", new PropertyModel<Boolean>(this, "memory32")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(memory32CheckBox);
		
		// 2x 16 GB Label
		memory32Label = new Label("memory32Label", "     2x 16 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!memory32Label.isVisible()) {
					memory32 = false;
				}
			}
		};
		filterForm.add(memory32Label);
	
// 3000 MHz AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox frequency3000CheckBox = new AjaxCheckBox("frequency3000CheckBox", new PropertyModel<Boolean>(this, "frequency3000")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(frequency3000CheckBox);
		
		// 3000 MHz Label
		frequency3000Label = new Label("frequency3000Label", "     3000 MHz") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!frequency3000Label.isVisible()) {
					frequency3000 = false;
				}
			}
		};
		filterForm.add(frequency3000Label);
		
// 3200 MHz AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox frequency3200CheckBox = new AjaxCheckBox("frequency3200CheckBox", new PropertyModel<Boolean>(this, "frequency3200")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(frequency3200CheckBox);
		
		// 3200 MHz Label
		frequency3200Label = new Label("frequency3200Label", "     3200 MHz") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!frequency3200Label.isVisible()) {
					frequency3200 = false;
				}
			}
		};
		filterForm.add(frequency3200Label);
		
// 3600 MHz AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox frequency3600CheckBox = new AjaxCheckBox("frequency3600CheckBox", new PropertyModel<Boolean>(this, "frequency3600")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(frequency3600CheckBox);
		
		// 3600 MHz Label
		frequency3600Label = new Label("frequency3600Label", "     3600 MHz") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!frequency3600Label.isVisible()) {
					frequency3600 = false;
				}
			}
		};
		filterForm.add(frequency3600Label);
		
// 4000 MHz AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox frequency4000CheckBox = new AjaxCheckBox("frequency4000CheckBox", new PropertyModel<Boolean>(this, "frequency4000")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(frequency4000CheckBox);
		
		// 4000 MHz Label
		frequency4000Label = new Label("frequency4000Label", "     4000 HMz") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!frequency4000Label.isVisible()) {
					frequency4000 = false;
				}
			}
		};
		filterForm.add(frequency4000Label);
		
// CL15 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox cl15CheckBox = new AjaxCheckBox("cl15CheckBox", new PropertyModel<Boolean>(this, "cl15")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(cl15CheckBox);
		
		// CL15 Lable
		cl15Label = new Label("cl15Label", "     CL15") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cl15Label.isVisible()) {
					cl15 = false;
				}
			}
		}; 
		filterForm.add(cl15Label);
		
// CL16 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox cl16CheckBox = new AjaxCheckBox("cl16CheckBox", new PropertyModel<Boolean>(this, "cl16")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(cl16CheckBox);
		
		// CL16 Label
		cl16Label = new Label("cl16Label", "     CL16") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!cl16Label.isVisible()) {
					cl16 = false;
				}
			}
		};
		filterForm.add(cl16Label);
		
// CL16 AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////

			AjaxCheckBox cl19CheckBox = new AjaxCheckBox("cl19CheckBox", new PropertyModel<Boolean>(this, "cl19")) {
				
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					target.add(filterForm);
			 		target.add(markupContainer);
				}
			};
			filterForm.add(cl19CheckBox);
			
			// CL19 Label
			cl19Label = new Label("cl19Label", "     CL19") {
				
				@Override
				protected void onConfigure() {
					super.onConfigure();
					
					if (!cl19Label.isVisible()) {
						cl19 = false;
					}
				}
			};
			filterForm.add(cl19Label);
		
// Intel XMP AjaxCheckBox ////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox intelXMPCheckBox = new AjaxCheckBox("intelXMPCheckBox", new PropertyModel<>(this, "intelXMP")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelXMPCheckBox);
		
		// Intel XMP Label
		intelXMPLabel = new Label("intelXMPLabel", "     Intel XMP") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelXMPLabel.isVisible()) {
					intelXMP = false;
				}
			}
		};
		filterForm.add(intelXMPLabel);
		
// Intel XMP 2.0 AjaxCheckBox ////////////////////////////////////////////////////////////////////////
	
		AjaxCheckBox intelXMP2CheckBox = new AjaxCheckBox("intelXMP2CheckBox", new PropertyModel<Boolean>(this, "intelXMP2")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
		 		target.add(markupContainer);
			}
		};
		filterForm.add(intelXMP2CheckBox);
		
		// Intel XMP 2.0 Label
		intelXMP2Label = new Label("intelXMP2Label", "     Intel XMP 2.0") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!intelXMP2Label.isVisible()) {
					intelXMP2 = false;
				}
			}
		};
		filterForm.add(intelXMP2Label);
	}
	
}
