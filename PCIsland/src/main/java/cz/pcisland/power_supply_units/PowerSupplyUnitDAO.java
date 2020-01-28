package cz.pcisland.power_supply_units;

import java.util.List;

import cz.pcisland.product.Product;

/**
 *	Rozhraní přístupu dat ke zdrojům
 */

public interface PowerSupplyUnitDAO {

	/**
	 * 	Načtení zdroje z databáze podle zadaného ID
	 * 
	 * 	@param id - ID zdroje
	 * 	@return zdroj
	 */
	public Product getPowerSupplyUnit(int id);
	
	/**
	 * 	Načtení všech zdrojů z databáze
	 * 
	 * 	@return List všech zdrojů
	 */
	public List<Product> getAllPowerSupplyUnits();
	
	/**
	 * 	Načtení 3 nejprodávanějších zdrojů
	 * 
	 * 	@return List nejprodávanějších zdrojů
	 */
	public List<Product> getTopSellingPowerSupplyUnits();
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 * 
	 * 	@param powerSupplyUnitId - ID zdroje
	 */
	public void incrementNumberOfPreview(int powerSupplyUnitId);
	
	/**
	 * 	Přičtení hodnocení zdroje podle zadaného ID
	 * 
	 * 	@param powerSupplyUnitId - ID zdroje
	 * 	@param rating - hodnocení
	 */
	public void addRating(int powerSupplyUnitId, int rating);
	
}
