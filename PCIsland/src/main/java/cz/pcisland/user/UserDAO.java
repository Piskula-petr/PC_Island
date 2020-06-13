package cz.pcisland.user;

import java.util.List;

/**
 * 	Rozhraní přístupu dat k uživatelům
 */
public interface UserDAO {

	/**
	 * 	Uložení nového uživatele do databáze
	 * 
	 * 	@param user - uživatel
	 */
	public void saveUser(User user);
	
	/**
	 * 	Načtení všech uživatelů z databáze
	 * 
	 * 	@return List všech uživatelů
	 */
	public List<User> getAllUsers();
	
	/**
	 * 	Načtení všech států z databáze
	 * 
	 * 	@return List států
	 */
	public List<String> getCountries();
	
	/**
	 * 	Uložení nového emailu podle zadaného emailu uživatele
	 * 
	 * 	@param currentUserEmail - aktuální email uživatele
	 * 	@param newEmail - nový email uživatele
	 */
	public void changeEmail(String currentUserEmail, String newEmail);
	
	/**
	 * 	Uložení nového heslo podle zadaného emailu uživatele
	 * 
	 * 	@param currentUserEmail - aktuální email uživatele
	 *	@param newPassword - nové heslo uživatele
	 */
	public void changePassword(String currentUserEmail, String newPassword);
	
	/**
	 * 	Uložení nových dodacích údajů podle zadaného emailu uživatele 
	 * 
	 * 	@param currentUserEmail - aktuální email uživatele
	 * 	@param columnName - název sloupce v databázi
	 * 	@param newValue - nová hodnota dodacích údajů
	 */
	public void changeDeliveryData(String currentUserEmail, String columnName, String newValue);
	
}
