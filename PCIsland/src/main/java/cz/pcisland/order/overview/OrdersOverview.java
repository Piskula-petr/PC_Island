package cz.pcisland.order.overview;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

import cz.pcisland.base_page.BasePage;
import cz.pcisland.base_page.CustomPagingNavigator;
import cz.pcisland.graphics_cards.detail.GraphicsCardDetailPage;
import cz.pcisland.hard_disks.detail.HarddiskDetailPage;
import cz.pcisland.memory.detail.MemoryDetailPage;
import cz.pcisland.motherboards.detail.MotherBoardDetailPage;
import cz.pcisland.order.Order;
import cz.pcisland.order.OrderDAO;
import cz.pcisland.order.OrderDAOImpl;
import cz.pcisland.order.overview.detail.OrderOverviewDetail;
import cz.pcisland.power_supply_units.detail.PowerSupplyUnitsDetailPage;
import cz.pcisland.processors.detail.ProcessorDetailPage;
import cz.pcisland.product.Product;
import cz.pcisland.product.ProductDAO;
import cz.pcisland.product.ProductDAOImpl;
import cz.pcisland.user.User;

/*
 * 	Třída přehled objednávek:
 * 
 * 		nsatavení titulku,
 * 		filtr (nová, zpracovaná ...),
 * 		konfigurace komponent filtru,
 *   	nastavení objednávek podle přihlášeného uživatele,
 */

public class OrdersOverview extends BasePage {

	private static final long serialVersionUID = 1L;
	
	// Filtr
	private Boolean newOrder = false;
	private Boolean processedOrder = false;
	private Boolean sentOrder = false;
	private Boolean completedOrder = false;
	
	private OrderDAO orderDAO = new OrderDAOImpl();
	private List<String> imagePaths;
	private List<Order> orders = new ArrayList<>();
	private User user = (User) AuthenticatedWebSession.get().getAttribute("currentUser");
	
	// Komponenty
	private PageableListView<Order> ordersPageableListView;
	private Label newOrderLabel, processedOrderLabel, sentOrderLabel, completedOrderLabel;
	
// Konstruktor ///////////////////////////////////////////////////////////////////////////////////////
	
	public OrdersOverview() {
		
		// Nastaví titulek
		setTitle(new Model("Objednávky"));
		
		// Popisek v případě žádných objednávek
		Label noOrdersLabel = new Label("noOrdersLabel", "Nemáte žádné objednávky.");
		add(noOrdersLabel);
		
// WebMarkupContainer ////////////////////////////////////////////////////////////////////////////////
		
		WebMarkupContainer markupContainer = new WebMarkupContainer("markupContainer");
		markupContainer.setOutputMarkupId(true);
		add(markupContainer);
		
// Filtr Form ////////////////////////////////////////////////////////////////////////////////////////
		
		Form filterForm = new Form("filterForm") {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Všechny objednávky
				if (user.getEmail().equals("admin@pcisland.cz")) {
					orders = orderDAO.getAllOrders();
				
				} else {
					orders = orderDAO.getUsersOrders(user.getId());
				}
				
// Stav objednávky ///////////////////////////////////////////////////////////////////////////////////
				
				List<Order> filteredOrders = new ArrayList<>();
				
				if (newOrder && processedOrder && sentOrder && completedOrder) {
					// Žádná akce v případě všech možnosti
				} else {
					
					for (Order order : orders) {
						
						// Nová
						if (newOrder) {
							
							if (order.getStatus().equals("Nová")) {
								filteredOrders.add(order);
							}
						}
						
						// Zpracovaná
						if (processedOrder) {
							
							if (order.getStatus().equals("Zpracovaná")) {
								filteredOrders.add(order);
							}
						}
						
						// Odeslaná
						if (sentOrder) {
							
							if (order.getStatus().equals("Odeslaná")) {
								filteredOrders.add(order);
							}
						}
						
						// Vyřízená
						if (completedOrder) {
							
							if (order.getStatus().equals("Vyřízená")) {
								filteredOrders.add(order);
							}
						}
					}
				}
				
				// Přiřazení filtrovaných objednávek
				if (!filteredOrders.isEmpty()) {
					ordersPageableListView.setList(filteredOrders);
				
				} else if (user.getEmail().equals("admin@pcisland.cz")) {
					ordersPageableListView.setList(orderDAO.getAllOrders());
				
				} else {
					ordersPageableListView.setList(orderDAO.getUsersOrders(user.getId()));
				}
			}
		};
		filterForm.setOutputMarkupId(true);
		add(filterForm);
		
// Nová AjaxCheckBox /////////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox newOrderCheckBox = new AjaxCheckBox("newOrderCheckBox", new PropertyModel<Boolean>(this, "newOrder")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(newOrderCheckBox);
		
		// Nová Label
		newOrderLabel = new Label("newOrderLabel", "     Nová");
		filterForm.add(newOrderLabel);
		
// Zpracovaná AjaxCheckBox /////////////////////////////////////////////////////////////////////////// 
	
		AjaxCheckBox processedOrderCheckBox = new AjaxCheckBox("processedOrderCheckBox", new PropertyModel<Boolean>(this, "processedOrder")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(processedOrderCheckBox);
		
		// Zpracavaná Label
		processedOrderLabel = new Label("processedOrderLabel", "     Zpracovaná");
		filterForm.add(processedOrderLabel);
		
// Odeslaná AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox sentOrderCheckBox = new AjaxCheckBox("sentOrderCheckBox", new PropertyModel<Boolean>(this, "sentOrder")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(sentOrderCheckBox);
		
		// Odeslaná Label
		sentOrderLabel = new Label("sentOrderLabel", "     Odeslaná");
		filterForm.add(sentOrderLabel);
		
// Vyřízená AjaxCheckBox /////////////////////////////////////////////////////////////////////////////
		
		AjaxCheckBox completedOrderCheckBox = new AjaxCheckBox("completedOrderCheckBox", new PropertyModel<Boolean>(this, "completedOrder")) {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(filterForm);
				target.add(markupContainer);
			}
		};
		filterForm.add(completedOrderCheckBox);
		
		// Vyřízená Label
		completedOrderLabel = new Label("completedOrderLabel", "     Vyřízená");
		filterForm.add(completedOrderLabel);
		
// Objednávky PageableListView ///////////////////////////////////////////////////////////////////////
		
		ordersPageableListView = new PageableListView<Order>("ordersPageableListView", orderDAO.getUsersOrders(user.getId()), 10) {
			
			@Override
			protected void onConfigure() {
				super.onConfigure();
				
				// Viditelnost popisku
				if (getList().size() == 0) {
					noOrdersLabel.setVisible(true);
				} else noOrdersLabel.setVisible(false);
			}
			
			@Override
			protected void populateItem(ListItem<Order> item) {

				Order order = item.getModelObject();
				imagePaths = new ArrayList<>();
				
				String pattern = "###,###.###";
				DecimalFormat decimalFormat = new DecimalFormat(pattern);
				String totalPrice = decimalFormat.format(order.getTotalPrice()) + ",-";
				
				item.add(new Label("creationDate", order.getCreationDate()));
				item.add(new Link<Object>("idOrderLink") {
					
					@Override
					public void onClick() {
						
						// Přesměrování na detail objednávky
						setResponsePage(new OrderOverviewDetail(order));
					}
				}.add(new Label("idOrder", order.getIdOrder())));
				
				item.add(new Label("status", order.getStatus()));
				item.add(new Label("totalPrice", totalPrice));
				
				// Rozložení typů zboží a ID Zboží
				String[] productTypes = order.getProductTypes().split(";");		// (processors;memory;hard_disks)
				String[] productNames = order.getProductNames().split(";");		// (Intel Core i7-9700K;EVGA 850 B3;WD Blue 1TB)
				for (int i = 0; i < productTypes.length; i++) {

					// Uloží cestu k náhledovému obrázku do Listu
					String imagePath = "";
					imagePath = "preview images//" + productNames[i] + "//" + productNames[i] + ".jpg";
					imagePaths.add(imagePath);
				}
				
				item.add(new ListView<String>("productsListView", imagePaths) {
					
					@Override
					protected void populateItem(ListItem<String> item) {

						item.add(new Link<Object>("imageLink") {
							
							@Override
							public void onClick() {
								
								// Přesměrování na detail zboží
								ProductDAO productDAO = new ProductDAOImpl();

								switch (productTypes[item.getIndex()]) {
								
									case "processor":
										Product processor = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new ProcessorDetailPage(processor));
										break;
										
									case "graphics_card":
										Product graphicsCard = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new GraphicsCardDetailPage(graphicsCard));
										break;
										
									case "memory":
										Product memory = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new MemoryDetailPage(memory));
										break;
										
									case "motherboard":
										Product motherboard = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new MotherBoardDetailPage(motherboard));
										break;
										
									case "hard_disk":
										Product hardDisk = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new HarddiskDetailPage(hardDisk));
										break;
										
									case "power_supply_unit":
										Product powerSupplyUnit = productDAO.getProductByName(productNames[item.getIndex()]);
										setResponsePage(new PowerSupplyUnitsDetailPage(powerSupplyUnit));
										break;
								}
							}
						}.add(new Image("image", new ContextRelativeResource(item.getModelObject()))));
					}
					
				});
			}
		};
		markupContainer.add(ordersPageableListView);
		markupContainer.add(new CustomPagingNavigator("pagingNavigator", ordersPageableListView));
		
// Admin /////////////////////////////////////////////////////////////////////////////////////////////
		
		// Zobrazení všech objednávek pokud je příhlášený Admin
		if (user.getEmail().equals("admin@pcisland.cz")) {
			ordersPageableListView.setList(orderDAO.getAllOrders());
		}
	}
	
}
