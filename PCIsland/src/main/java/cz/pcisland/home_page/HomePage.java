package cz.pcisland.home_page;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import cz.pcisland.company.CompanyPage;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.product.ProductPage;

/**
 *	Třída domovské stránky:
 *
 *		nastavení titulku,
 *		nastavení všech produktů,
 *		odkazy na produkty společností (amd, asus ...)
 */
public class HomePage extends ProductPage {

	private static final long serialVersionUID = 1L;
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public HomePage() {
		
		// Nastaví titulek
		setTitle(new Model("PC Island"));
		
		// Všechny produkty PageableListView (ProductPage)
		productsPageableListView.setList(new ProductDAOImpl().getAllProducts());
		products = productsPageableListView.getList();
		
// Společnosti ///////////////////////////////////////////////////////////////////////////////////////
		
		// AMD Link
		add(new Link<Object>("amdLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti AMD
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("AMD"), "AMD", "amd"));
			}
		});
		
		// ASUS Link
		add(new Link<Object>("asusLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti ASUS
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("ASUS"), "ASUS", "asus"));
			}
		});
		
		// Corsair Link
		add(new Link<Object>("corsairLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Corsair
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Corsair"), "Corsair", "corsair"));
			}
		});
		
		// Crucial Link
		add(new Link<Object>("crucialLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Crucial
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Crucial"), "Crucial", "crucial"));
			}
		});
		
		// EVGA Link
		add(new Link<Object>("evgaLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti EVGA
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("EVGA"), "EVGA", "evga"));
			}
		});
		
		// GIGABYTE Link
		add(new Link<Object>("gigabyteLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti GIGABYTE
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("GIGABYTE"), "GIGABYTE", "gigabyte"));
			}
		});
		
		// G.SKILL Link
		add(new Link<Object>("gskillLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti G.SKILL
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("G.SKILL"), "G.SKILL", "gskill"));
			}
		});
		
		// HyperX Link
		add(new Link<Object>("hyperxLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti HyperX
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("HyperX"), "HyperX", "hyperx"));
			}
		});
		
		// Intel Link
		add(new Link<Object>("intelLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Intel
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Intel"), "Intel", "intel"));
			}
		});
		
		// MSI Link
		add(new Link<Object>("msiLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti MSI
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("MSI"), "MSI", "msi"));
			}
		});
		
		// Samsung Link
		add(new Link<Object>("samsungLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Samsung
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Samsung"), "Samsung", "samsung"));
			}
		});
		
		// Seagate Link
		add(new Link<Object>("seagateLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Seagate
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Seagate"), "Seagate", "seagate"));
			}
		});
		
		// Seasonic Link
		add(new Link<Object>("seasonicLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Seasonic
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("Seasonic"), "Seasonic", "seasonic"));
			}
		});
		
		// Western Digital Link
		add(new Link<Object>("westernDigitalLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na stránku společnosti Western Digital
				setResponsePage(new CompanyPage(new ProductDAOImpl().getProductsFromCompany("WD"), "Western Digital", "western_digital"));
			}
		});
	}	
	
}
