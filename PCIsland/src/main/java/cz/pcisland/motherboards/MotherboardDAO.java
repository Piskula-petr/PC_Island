package cz.pcisland.motherboards;

import java.util.List;

import cz.pcisland.product.Product;

/**
 *	Rozhraní přístupu dat k základním deskám
 */

public interface MotherboardDAO {

	/**
	 * 	Načtení základní desky z databáze podle zadaného ID
	 * 
	 * 	@param id - ID základní desky
	 * 	@return Základní desku
	 */
	public Product getMotherboard(int id);
	
	/**
	 * 	Načtení všech základních desek z databáze
	 * 
	 * 	@return List všech základních desek
	 */
	public List<Product> getAllMotherboards();
	
	/**
	 * 	Načtení 3 nejprodávanějších základních desek
	 * 
	 * 	@return List nejprodávanějších základních deske
	 */
	public List<Product> getTopSellingMotherboards();
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param motherboardId - ID základní desky
	 */
	public void incrementNumberOfPreview(int motherboardId);
	
	/**
	 * 	Přičtení hodnocení základní desky podle zadaného ID
	 * 
	 * 	@param motherboardId - ID základní desky
	 * 	@param rating - hodnocení produktu
	 */
	public void addRating(int motherboardId, int rating);
	
}
