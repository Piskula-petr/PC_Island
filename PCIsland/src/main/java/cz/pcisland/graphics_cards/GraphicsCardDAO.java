package cz.pcisland.graphics_cards;

import java.util.List;

import cz.pcisland.product.Product;

/**
 *	Rozhraní přistupu dat ke grafickým kartám
 */
public interface GraphicsCardDAO {

	/**
	 * 	Načtení grafické karty z databáze podle zadaného ID
	 * 
	 * 	@param id - ID grafické karty
	 * 	@return grafickou kartu
	 */
	public Product getGraphicsCard(int id);
	
	/**
	 * 	Načtení všech grafických karet z databáze
	 * 
	 * 	@return List všech grafických karet
	 */
	public List<Product> getAllGraphicsCards();
	
	
	/**	
	 * 	Načtení 3 nejprodávanějších grafických karet
	 * 
	 * 	@return List nejprodávanějších grafických karet
	 */
	public List<Product> getTopSellingGraphicsCards();
	
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param graphicsCardId - ID grafické karty
	 */
	public void incrementNumberOfPreview(int graphicsCardId);
	
	/**
	 * 	Přičtení hodnocení grafické karty podle zadaného ID
	 * 
	 * 	@param graphicsCardId - ID grafické karty
	 * 	@param rating - hodnocení produktu
	 */
	public void addRating(int graphicsCardId, int rating);
	
}
