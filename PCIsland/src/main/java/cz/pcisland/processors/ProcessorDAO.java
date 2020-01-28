package cz.pcisland.processors;

import java.util.List;

/*
 * 	Rozhraní přístupu dat k procesorům:
 * 
 * 		vrací procesor podle zadaného ID,
 * 		vrací List všech procesorů,
 * 		vrací List nejprodávanějších procesorů,
 * 		zvýší počet recenzí procesoru podle zadaného ID,
 * 		přičte hodnocení procesoru podle zadaného ID;
 */

import cz.pcisland.product.Product;

/**
 * 	Rozhraní přístupu dat k procesorům
 */

public interface ProcessorDAO {

	/**
	 * 	Načtení procesoru z databáze podle zadaného ID
	 * 
	 * 	@param id - ID procesoru
	 * 	@return procesor
	 */
	public Product getProcessor(int id);
	
	/**
	 * 	Načtení všech procesorů z databáze
	 * 
	 * 	@return List všech procesorů
	 */
	public List<Product> getAllProcessors();
	
	/**
	 * 	Načtení 3 nejprodávanějších procesorů
	 * 
	 * 	@return List nejprodávanějších procesorů
	 */
	public List<Product> getTopSellingProcessors();
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param processorId - ID procesoru
	 */
	public void incrementNumberOfPreview(int processorId);
	
	/**
	 * 	Přičtení hodnocení procesoru podle zadaného ID
	 * 
	 * 	@param processorId - ID procesoru
	 * 	@param rating - hodnocení
	 */
	public void addRating(int processorId, int rating);
	
}
