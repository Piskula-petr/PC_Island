package cz.pcisland.hard_disks;

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
 *	Třída pevných disků:
 *
 *		nastavení titulku,
 *		nastavení produktů (nejprodávanější, všechny), 
 *		filtr (cena, dostupnost ...),
 *		konfigurace komponent filtru
 */

public class HardDisksPage extends ProductPageWithTopSelling {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean onStock = false;
	
	private Boolean samsung = false;
	private Boolean seagate = false;
	private Boolean westernDigital = false;
	
	private Boolean magnetic = false;
	private Boolean ssd = false;
	
	private Boolean format2_5 = false;
	private Boolean format3_5 = false;
	private Boolean formatM_2 = false;
	
	private Boolean size0to500 = false;
	private Boolean size500to1000 = false;
	private Boolean size1000to2000 = false;
	private Boolean size2000Plus = false;
	
	private Boolean m_2Pci = false;
	private Boolean sata6 = false;

	private Integer MAX_PRICE = 0;
	private Integer MIN_PRICE = Integer.MAX_VALUE;
	private Integer inputMaxPrice;
	private Integer inputMinPrice;
	
	private HardDiskDAO hardDiskDAO = new HardDiskDAOImpl();
	private List<Product> hardDisksInPriceRange = new ArrayList<>();
	
	// Komponenty
	private TextField<Integer> minPriceTextField, maxPriceTextField;
	private Label onStockLabel, samsungLabel, seagateLabel, westernDigitalLabel, ssdLabel, magneticLabel, format2_5Label,
				  format3_5Label, formatM_2Label, size0to500Label, size500to1000Label, size1000to2000Label, size2000PlusLabel,
				  m_2PciLabel, sata6Label;

// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public HardDisksPage() {
		
		// Nastavení titulku
		setTitle(new Model("Pevné disky (HDD) | PC Island"));
		
		// Nejprodávanější (pevné disky) ListView (ProductPageWithTopSelling)
		topSellingListView.setList(hardDiskDAO.getTopSellingHardDisks());
		
		// Produkty (pevné disky) PageableListView (ProductPage)
		productsPageableListView.setList(hardDiskDAO.getAllHardDisks());
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
				setResponsePage(HardDisksPage.class);
			}
		});
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onSubmit() {
				super.onSubmit();
				
				// Všechny pevné disky
				products = hardDiskDAO.getAllHardDisks();
				
				// Nastavení cenového rozpětí
				hardDisksInPriceRange = getPriceRange(products, inputMinPrice, inputMaxPrice, MIN_PRICE, MAX_PRICE);
				onConfigure();
			}
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Základní desky v cenovém rozpětí / Všechny základní desky
				if (hardDisksInPriceRange.size() != 0) {
					products = new ArrayList<>(hardDisksInPriceRange);
				
				} else products = hardDiskDAO.getAllHardDisks();
				
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
				
				List<Product> filteredHardDisk = new ArrayList<>();
				
				for (Product hardDisk : products) {
					
					// Skladem
					if (onStock) {
						
						if (hardDisk.getStock() > 0) {
							filteredHardDisk.add(hardDisk);
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					products = filteredHardDisk;
				}
				
// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
				
				filteredHardDisk = new ArrayList<>();
				
				if (samsung && seagate && westernDigital) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product hardDisk : products) {
						
						// Samsung 
						if (samsung) {
							
							if (hardDisk.getParametrs().get("company").equals("Samsung")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// Seagate
						if (seagate) {
							
							if (hardDisk.getParametrs().get("company").equals("Seagate")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// Western Digital
						if (westernDigital) {
							
							if (hardDisk.getParametrs().get("company").equals("Western Digital")) {
								filteredHardDisk.add(hardDisk);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					products = filteredHardDisk;
				}

// Typ pevného disku /////////////////////////////////////////////////////////////////////////////////
				
				filteredHardDisk = new ArrayList<>();
				
				if (magnetic && ssd) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product hardDisk : products) {
						
						// Magnetický
						if (magnetic) {
							
							if (hardDisk.getParametrs().get("type").equals("Magnetický")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// SSD 
						if (ssd) {
							
							if (hardDisk.getParametrs().get("type").equals("SSD")) {
								filteredHardDisk.add(hardDisk);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					products = filteredHardDisk;
				}
				
// Formát ////////////////////////////////////////////////////////////////////////////////////////////
				
				filteredHardDisk = new ArrayList<>();
				
				if (format2_5 && format3_5 && formatM_2) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product hardDisk : products) {
						
						// 2.5"
						if (format2_5) {
							
							if (hardDisk.getParametrs().get("format").equals("2.5")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// 3.5"
						if (format3_5) {
							
							if (hardDisk.getParametrs().get("format").equals("3.5")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// M.2
						if (formatM_2) {
							
							if (hardDisk.getParametrs().get("format").equals("M.2")) {
								filteredHardDisk.add(hardDisk);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					products = filteredHardDisk;
				}
				
// Kapacita //////////////////////////////////////////////////////////////////////////////////////////
				
				filteredHardDisk = new ArrayList<>();
				
				if (size0to500 && size500to1000 && size1000to2000 && size2000Plus) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product hardDisk : products) {
						
						// 0 GB - 500 GB
						if (size0to500) {
							
							if (Integer.parseInt(hardDisk.getParametrs().get("size")) > 0 
								&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 500) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// 500 GB - 1000 GB
						if (size500to1000) {
							
							if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 500 
								&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 1000) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// 1000 GB - 2000 GB
						if (size1000to2000) {
							
							if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 1000 
								&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 2000) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// +2000 GB
						if (size2000Plus) {
							
							if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 2000) {
								filteredHardDisk.add(hardDisk);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					
					// Odstranění duplicit
					products = filteredHardDisk.stream().distinct().collect(Collectors.toList());
				}
				
// Rozhraní //////////////////////////////////////////////////////////////////////////////////////////
				
				filteredHardDisk = new ArrayList<>();
				
				if (m_2Pci && sata6) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Product hardDisk : products) {
						
						// M.2 PCI-Express
						if (m_2Pci) {
							
							if (hardDisk.getParametrs().get("connector").equals("M.2 PCI-Express")) {
								filteredHardDisk.add(hardDisk);
							}
						}
						
						// SATA 6 GB/s
						if (sata6) {
							
							if (hardDisk.getParametrs().get("connector").equals("SATA 6 GB/s")) {
								filteredHardDisk.add(hardDisk);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných pevných disků
				if (!filteredHardDisk.isEmpty()) {
					products = filteredHardDisk;
				}
				
				// Zneviditelnění celého filtru
				onStockLabel.setVisible(false);
				
				samsungLabel.setVisible(false);
				seagateLabel.setVisible(false);
				westernDigitalLabel.setVisible(false);
				
				magneticLabel.setVisible(false);
				ssdLabel.setVisible(false);
				
				format2_5Label.setVisible(false);
				format3_5Label.setVisible(false);
				formatM_2Label.setVisible(false);
				
				size0to500Label.setVisible(false);
				size500to1000Label.setVisible(false);
				size1000to2000Label.setVisible(false);
				size2000PlusLabel.setVisible(false);
				
				m_2PciLabel.setVisible(false);
				sata6Label.setVisible(false);
				
// Zviditelnění možností filtu podle zobrazených produktů ////////////////////////////////////////////
				
				for (Product hardDisk : products) {
					
// Dostupnost ////////////////////////////////////////////////////////////////////////////////////////
					
					if (hardDisk.getStock() > 0) {
						onStockLabel.setVisible(true);
					}
					
					if (onStock && (!samsung && !seagate && !westernDigital && !magnetic && !ssd && !format2_5 
						&& !format3_5 && !formatM_2 && !size0to500 && !size500to1000 && !size1000to2000 
						&& !size2000Plus && !m_2Pci && !sata6)) {
						
						onStockLabel.setVisible(true);
					}

// Výrobce ///////////////////////////////////////////////////////////////////////////////////////////
					
					if (hardDisk.getParametrs().get("company").equals("Samsung")) {
						samsungLabel.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("company").equals("Seagate")) {
						seagateLabel.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("company").equals("Western Digital")) {
						westernDigitalLabel.setVisible(true);
					}
					
					if ((samsung || seagate || westernDigital) && (!magnetic && !ssd && !format2_5 
						&& !format3_5 && !formatM_2 && !size0to500 && !size500to1000 && !size1000to2000 
						&& !size2000Plus && !m_2Pci && !sata6)) {
						
						samsungLabel.setVisible(true);
						seagateLabel.setVisible(true);
						westernDigitalLabel.setVisible(true);
					}	
					
// Typ disku /////////////////////////////////////////////////////////////////////////////////////////
				
					if (hardDisk.getParametrs().get("type").equals("Magnetický")) {
						magneticLabel.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("type").equals("SSD")) {
						ssdLabel.setVisible(true);
					}
					
					if ((magnetic || ssd) && (!samsung && !seagate && !westernDigital && !format2_5 
						&& !format3_5 && !formatM_2 && !size0to500 && !size500to1000 && !size1000to2000 
						&& !size2000Plus && !m_2Pci && !sata6)) {
						
						magneticLabel.setVisible(true);
						ssdLabel.setVisible(true);
					}	
					
// Formát ////////////////////////////////////////////////////////////////////////////////////////////
					
					if (hardDisk.getParametrs().get("format").equals("2.5")) {
						format2_5Label.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("format").equals("3.5")) {
						format3_5Label.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("format").equals("M.2")) {
						formatM_2Label.setVisible(true);
					}
					
					if ((format2_5 || format3_5 || formatM_2) && (!samsung && !seagate && !westernDigital 
						&& !magnetic && !ssd && !size0to500 && !size500to1000 && !size1000to2000 && !size2000Plus 
						&& !m_2Pci && !sata6)) {
						
						format2_5Label.setVisible(true);
						format3_5Label.setVisible(true);
						formatM_2Label.setVisible(true);
					}	
					
// Kapacita //////////////////////////////////////////////////////////////////////////////////////////
					
					if (Integer.parseInt(hardDisk.getParametrs().get("size")) > 0
						&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 500) {
						size0to500Label.setVisible(true);
						
					} else if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 500
						&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 1000) {
						size500to1000Label.setVisible(true);
						
					} else if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 1000
						&& Integer.parseInt(hardDisk.getParametrs().get("size")) <= 2000) {
						size1000to2000Label.setVisible(true);
						
					} else if (Integer.parseInt(hardDisk.getParametrs().get("size")) >= 2000) {
						size2000PlusLabel.setVisible(true);
					}
					
					if ((size0to500 || size500to1000 || size1000to2000 || size2000Plus) && (!samsung && !seagate 
						&& !westernDigital && !magnetic && !ssd && !format2_5 && !format3_5 && !formatM_2 
						&& !m_2Pci && !sata6)) {
						
						size0to500Label.setVisible(true);
						size500to1000Label.setVisible(true);
						size1000to2000Label.setVisible(true);
						size2000PlusLabel.setVisible(true);
					}	
					
// Rozhraní //////////////////////////////////////////////////////////////////////////////////////////
					
					if (hardDisk.getParametrs().get("connector").equals("M.2 PCI-Express")) {
						m_2PciLabel.setVisible(true);
					
					} else if (hardDisk.getParametrs().get("connector").equals("SATA 6 GB/s")) {
						sata6Label.setVisible(true);
					}
					
					if ((m_2Pci || sata6) && (!samsung && !seagate && !westernDigital && !magnetic 
						&& !ssd && !format2_5 && !format3_5 && !formatM_2 && !size0to500 && !size500to1000 
						&& !size1000to2000 && !size2000Plus)) {
						
						m_2PciLabel.setVisible(true);
						sata6Label.setVisible(true);
					}
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
		// Maximální cena, Minimální cena
		for (Product hadrDisk : products) {
			
			if (MAX_PRICE < hadrDisk.getPrice()) {
				MAX_PRICE = hadrDisk.getPrice();
			}
			
			if (MIN_PRICE > hadrDisk.getPrice()) {
				MIN_PRICE = hadrDisk.getPrice();
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
		
// Samsung AjaxCheckBox //////////////////////////////////////////////////////////////////////////////

		AjaxCheckBox samsungCheckBox = new AjaxCheckBox("samsungCheckBox", new PropertyModel<Boolean>(this, "samsung")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(samsungCheckBox);
		
		// Samsung Label
		samsungLabel = new Label("samsungLabel", "     Samsung") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!samsungLabel.isVisible()) {
					samsung = false;
				}
			}
		};
		filterForm.add(samsungLabel);
		
// Seagate AjaxCheckBox //////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox seagateCheckBox = new AjaxCheckBox("seagateCheckBox", new PropertyModel<Boolean>(this, "seagate")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(seagateCheckBox);
		
		// Seagate Label
		seagateLabel = new Label("seagateLabel", "     Seagate") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!seagateLabel.isVisible()) {
					seagate = false;
				}
			}
		};
		filterForm.add(seagateLabel);
		
// Western Digital AjaxCheckBox //////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox westernDigitalCheckBox = new AjaxCheckBox("westernDigitalCheckBox", new PropertyModel<Boolean>(this, "westernDigital")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(westernDigitalCheckBox);
		
		// Western Digital
		westernDigitalLabel = new Label("westernDigitalLabel", "     Western Digital") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!westernDigitalLabel.isVisible()) {
					westernDigital = false;
				}
			}
		};
		filterForm.add(westernDigitalLabel);
		
// Magnetický AjaxCheckBox ///////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox magneticCheckBox = new AjaxCheckBox("magneticCheckBox", new PropertyModel<Boolean>(this, "magnetic")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(magneticCheckBox);
		
		// Magnetický Label
		magneticLabel = new Label("magneticLabel", "     Magnetický") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!magneticLabel.isVisible()) {
					magnetic = false;
				}
			}
		};
		filterForm.add(magneticLabel);
		
// SSD AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox ssdCheckBox = new AjaxCheckBox("ssdCheckBox", new PropertyModel<Boolean>(this, "ssd")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(ssdCheckBox);
		
		// SSD Label
		ssdLabel = new Label("ssdLabel", "     SSD") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!ssdLabel.isVisible()) {
					ssd = false;
				}
			}
		};
		filterForm.add(ssdLabel);
		
// 2.5" AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox format2_5CheckBox = new AjaxCheckBox("format2_5CheckBox", new PropertyModel<>(this, "format2_5")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(format2_5CheckBox);
		
		// 2.5" Label
		format2_5Label = new Label("format2_5Label", "     2.5\"") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!format2_5Label.isVisible()) {
					format2_5 = false;
				}
			}
		};
		filterForm.add(format2_5Label);
		
// 3.5" AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox format3_5CheckBox = new AjaxCheckBox("format3_5CheckBox", new PropertyModel<Boolean>(this, "format3_5")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(format3_5CheckBox);
		
		// 3.5" Label
		format3_5Label = new Label("format3_5Label", "     3.5\"") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!format3_5Label.isVisible()) {
					format3_5 = false;
				}
			}
		};
		filterForm.add(format3_5Label);
		
// M.2 AjaxCheckBox //////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox formatM_2CheckBox = new AjaxCheckBox("formatM_2CheckBox", new PropertyModel<Boolean>(this, "formatM_2")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(formatM_2CheckBox);
		
		// M.2 Label
		formatM_2Label = new Label("formatM_2Label", "     M.2") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!formatM_2Label.isVisible()) {
					formatM_2 = false;
				}
			}
		};
		filterForm.add(formatM_2Label);
		
// 0 GB - 500 GB AjaxCheckBox ////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox size0to500CheckBox = new AjaxCheckBox("size0to500CheckBox", new PropertyModel<Boolean>(this, "size0to500")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(size0to500CheckBox);
		
		// 0 GB - 500 GB Label
		size0to500Label = new Label("size0to500Label", "     0 GB - 500 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!size0to500Label.isVisible()) {
					size0to500 = false;
				}
			}
		};
		filterForm.add(size0to500Label);
		
// 500 GB - 1000 GB AjaxCheckBox /////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox size500to1000CheckBox = new AjaxCheckBox("size500to1000CheckBox", new PropertyModel<Boolean>(this, "size500to1000")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(size500to1000CheckBox);
		
		// 500 GB - 1000 GB Label
		size500to1000Label = new Label("size500to1000Label", "     500 GB - 1000 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!size500to1000Label.isVisible()) {
					size500to1000 = false;
				}
			}
		};
		filterForm.add(size500to1000Label);
		
// 1000 GB - 2000 GB AjaxCheckBox ////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox size1000to2000CheckBox = new AjaxCheckBox("size1000to2000CheckBox", new PropertyModel<Boolean>(this, "size1000to2000")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(size1000to2000CheckBox);
		
		// 1000 GB - 2000 GB Label
		size1000to2000Label = new Label("size1000to2000Label", "     1000 GB - 2000 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!size1000to2000Label.isVisible()) {
					size1000to2000 = false;
				}
			}
		};
		filterForm.add(size1000to2000Label);
		
// +2000 GB AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox size2000PlusCheckBox = new AjaxCheckBox("size2000PlusCheckBox", new PropertyModel<Boolean>(this, "size2000Plus")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(size2000PlusCheckBox);
		
		// +2000 GB Label
		size2000PlusLabel = new Label("size2000PlusLabel", "     +2000 GB") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!size2000PlusLabel.isVisible()) {
					size2000Plus = false;
				}
			}
		};
		filterForm.add(size2000PlusLabel);

// M.2 PCI-Express AjaxCheckBox //////////////////////////////////////////////////////////////////////

		AjaxCheckBox m_2PciCheckBox = new AjaxCheckBox("m_2PciCheckBox", new PropertyModel<Boolean>(this, "m_2Pci")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(m_2PciCheckBox);
		
		// M.2 PCI-Express Label
		m_2PciLabel = new Label("m_2PciLabel", "     M.2 PCI-Express") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!m_2PciLabel.isVisible()) {
					m_2Pci = false;
				}
			}
		};
		filterForm.add(m_2PciLabel);
		
// SATA 6 GB/s AjaxCheckBox //////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox sata6CheckBox = new AjaxCheckBox("sata6CheckBox", new PropertyModel<Boolean>(this, "sata6")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(sata6CheckBox);
		
		// SATA 6 GB/s Label
		sata6Label = new Label("sata6Label", "     SATA 6 GB/s") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				if (!sata6Label.isVisible()) {
					sata6 = false;
				}
			}
		};
		filterForm.add(sata6Label);
	}
	
}
