package cz.pcisland.order;

import java.util.List;

/**
 * 	Rozhraní přístupu dat k objednávkám
 */
public interface OrderDAO {

	/**
	 * 	Uložení novou objednávku do databáze
	 * 
	 * 	@param order - Objednávka
	 */
	public void saveOrder(Order order);
	
	/**
	 * 	Načte všechny objednávky z databáze
	 * 
	 * 	@return List všech objednávek
	 */
	public List<Order> getAllOrders();
	
	/**
	 * 	Načte objednávky podle přihlášeného uživatele
	 * 
	 * 	@param idUser - ID uživatele
	 * 	@return List objednávek
	 */
	public List<Order> getUsersOrders(int idUser);
	
	/**
	 * 	Načtení ID objednávky podle uživatele z databáze
	 * 
	 * 	@param userFullName - Jméno + příjmení uživatele
	 * 	@return ID objednávky
	 */
	public int getIdOrder(String userFullName);
	
	/**
	 * 	Zvýšení počtu prodaných kusů podle zadaného názvu produktu
	 * 
	 * 	@param productName - Název produktu
	 * 	@param amount - množství
	 */
	public void incrementProductSales(String productName, int amount);
	
	/**
	 * 	Snížení počtu kusů skladem podle zadaného názvu produktu
	 * 
	 * 	@param productName - Název produktu
	 * 	@param amount - množství
	 */
	public void decrementProductStock(String productName, int amount);
	
	/**
	 * 	Změna stavu objednávky podle zadaného ID
	 * 
	 * 	@param idOrder - ID objednávky
	 * 	@param status - stav objednávky
	 */
	public void changeOrderStatus(int idOrder, String status);
	
}
