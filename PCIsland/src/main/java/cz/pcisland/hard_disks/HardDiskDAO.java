package cz.pcisland.hard_disks;

import java.util.List;

import cz.pcisland.product.Product;

/**
 * 	Rozhraní přístupu dat k pevným diskům
 */
public interface HardDiskDAO {

	/**
	 * 	Načtení pevného disku z databáze podle zadaného ID
	 * 
	 * 	@param id - ID pevného disku
	 * 	@return pevný disk
	 */
	public Product getHardDisk(int id);
	
	/**
	 * 	Načtení všech pevných disků z databáze
	 * 
	 * 	@return List všech pevných disků
	 */
	public List<Product> getAllHardDisks();
	
	/**
	 * 	Načtení 3 nejprodávanějších pevných disků
	 * 
	 * 	@return List neprodávanejších pevných disků
	 */
	public List<Product> getTopSellingHardDisks();
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param hardDiskId - ID pevného disku
	 */
	public void incrementNumberOfPreview(int hardDiskId);
	
	/**
	 * 	Přičtení hodnocení pevného disku podle zadaného ID
	 * 
	 * 	@param hardDiskId - ID pevného disku
	 * 	@param rating - hodnocení produktu
	 */
	public void addRating(int hardDiskId, int rating);
	
}
