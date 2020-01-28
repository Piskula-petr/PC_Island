package cz.pcisland.product;

import java.util.List;

/**
 * 	Rozraní přístupu dat k produktům
 */

public interface ProductDAO {

	/**
	 * 	Načtení produktu podle zadaného názvu
	 * 
	 * 	@param name - název produktu
	 * 	@return produkt
	 */
	public Product getProductByName(String name);
	
	/**
	 * 	Načtení všech produktů z databáze
	 * 
	 * 	@return List všech produktů
	 */
	public List<Product> getAllProducts();
	
	/**
	 * 	Načtení všech produktů z databáze seřazených podle počtu kusů skladem
	 * 
	 * 	@return List produktů 
	 */
	public List<Product> getAllProductSortByStock();
	
	/**
	 * 	Načtení produktů podle zadané společnosti
	 * 
	 * 	@param companyName - název společnosti
	 * 	@return List produktů
	 */
	public List<Product> getProductsFromCompany(String companyName);
	
	/**
	 * 	Změna počtu kusů skladem
	 * 
	 * 	@param idProduct - ID produktu
	 * 	@param newStock - nové množství produktu
	 */
	public void changeStock(int idProduct, int newStock);
	
	/**
	 * 	Změna ceny produktu
	 * 
	 * 	@param idProduct - ID produktu
	 * 	@param newPrice - nová cena produktu
	 */
	public void changePrice(int idProduct, int newPrice);
	
}
