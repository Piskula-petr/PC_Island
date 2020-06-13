package cz.pcisland.order.report;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.home_page.HomePage;
import cz.pcisland.order.Order;
import cz.pcisland.order.OrderDAO;
import cz.pcisland.order.OrderDAOImpl;
import cz.pcisland.order.overview.OrdersOverview;

/*
 * 	Třída zprávy o objednávce:
 * 
 * 		nastavení titulku,
 * 		zpráva o objednávce,
 * 		navigační odkazy (zpět do obchodu, přehled objednávek)
 */
public class OrderReport extends BasePage {

	private static final long serialVersionUID = 1L;
	
	private int idOrder;

// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public OrderReport(Order order) {
		
		// Nastavení titulku
		setTitle(new Model("Zpráva o objednávce"));	
		
// Zpráva o objednávce WebMarkupContainer /////////////////////////////////////////////////////////////
		
		WebMarkupContainer orderReporWebContainer = new WebMarkupContainer("orderReporWebContainer");
		add(orderReporWebContainer);
		
		OrderDAO orderDAO = new OrderDAOImpl();
		idOrder = orderDAO.getIdOrder(order.getCustomerFullName());
		
		Label idOrderLabel = new Label("idOrder", idOrder);
		orderReporWebContainer.add(idOrderLabel);
		
		// Vyprázdnění košíku
		WebSession webSession = WebSession.get();
		webSession.setAttribute("cartList", null);
		webSession.setAttribute("cartSize", null);
		webSession.setAttribute("cartPrice", null);
		
		// Rozdělení názvů a množství produktů
		String[] productNames = order.getProductNames().split(";");		// (Intel Core i7-9700K;EVGA 850 B3;WD Blue 1TB)
		String[] productAmount = order.getProductAmount().split(";");	// (2,1,1)
		
		// Zvýší počet prodaných kusů, sníží počet kusů na skladu
		for (int i = 0; i < productNames.length; i++) {

			orderDAO.incrementProductSales(productNames[i], Integer.parseInt(productAmount[i]));
			orderDAO.decrementProductStock(productNames[i], Integer.parseInt(productAmount[i]));
		}
		
// Zpět na hlavní stránku Link ////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("backToHomePageLink") {
			
			@Override
			public void onClick() {
				
				// Přesměrování na domovskou stránku
				setResponsePage(HomePage.class);
			}
		});
		
// Přehled objednávek Link ////////////////////////////////////////////////////////////////////////////
		
		add(new Link<Object>("orderOverviewLink") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost pokud je uživatel přihlášen
				if (WebSession.get().getAttribute("currentUser") != null) {
					setVisible(true);
					
				} else setVisible(false);
			}
			
			@Override
			public void onClick() {
				
				// Přesměrování na přehled objednávek
				setResponsePage(OrdersOverview.class);
			}
		});
	}
	
}
