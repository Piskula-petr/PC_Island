package cz.pcisland.memory;

import java.util.List;

import cz.pcisland.product.Product;

/**
 *	Rozhraní přístupu dat k operačním pamětem
 */
public interface MemoryDAO {

	/**
	 * 	Načtení operační paměťi z databáze podle zadaného ID
	 * 
	 * 	@param id - ID operační paměti
	 * 	@return operační paměť
	 */
	public Product getMemory(int id);
	
	/**
	 * 	Načtení všech operačních pamětí z databáze
	 * 
	 * 	@return List všech operačních pamětí
	 */
	public List<Product> getAllMemory();
	
	/**
	 * 	Načtení 3 nejprodávanějších operačních pamětí
	 * 
	 * 	@return List neprodávanějších operačních pamětí
	 */
	public List<Product> getTopSellingMemory();
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param memoryId - ID operační paměti
	 */
	public void incrementNumberOfPreview(int memoryId);
	
	/**
	 * 	Přičtení hodnocení operační paměti podle zadaného ID
	 * 
	 * 	@param memoryId - ID pevného disku
	 * 	@param rating - hodnocené produktu
	 */
	public void addRating(int memoryId, int rating);
	
}
