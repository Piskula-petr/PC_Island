package cz.pcisland.review;

import java.util.List;

/**
 * 	Rozhraní přístupu dat k uživatelským recenzím
 */
public interface ReviewDAO {

	/**
	 * 	Uložení recenze do databáze
	 * 
	 * 	@param review - recenze
	 */
	public void saveReview(Review review);
	
	/**
	 * 	Načtení všech nenulových recenzí podle zadaného produktu
	 * 
	 * 	@param productName - název produktu
	 * 	@return List recenzí
	 */
	public List<Review> getProductNotNullReviews(String productName);
	
	/**
	 * 	Načtení uživatelských recenzí podle přihlášeného uživatele
	 * 
	 * @param idUser - ID uživatele
	 * @return List recenzí
	 */
	public List<Review> getUsersNotNullReviews(int idUser);
	
	/**
	 * 	Načtení všech nenulových recenzí produktu jednoho uživatele
	 * 
	 * 	@param productName - název produktu
	 * 	@param idUser - ID uživatele
	 * 	@return List recenzí
	 */
	public List<Review> getProductNotNullReviewsFromUser(String productName, int idUser);
	
	/**
	 * 	Načtení všech nenulových recenzí
	 * 
	 * 	@return List všech recenzí
	 */
	public List<Review> getAllNotNullReviews();
	
	/**
	 * 	Upravení kladů recenze podle zadaného ID
	 * 
	 * 	@param idReview - ID recenze
	 * 	@param newPros - nové klady recenze
	 */
	public void changeReviewPros(int idReview, String newPros);
	
	/**
	 * 	Upravení záporů recenze podle zadaného ID
	 * 
	 * 	@param idReview - ID recenze
	 * 	@param newCons - nové zápory recenze
	 */
	public void changeReviewCons(int idReview, String newCons);
	
}
