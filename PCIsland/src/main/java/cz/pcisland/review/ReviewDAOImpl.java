package cz.pcisland.review;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cz.pcisland.base_page.DatabaseConnection;

/**
 * 	Třída implementující rozhraní přístupu dat k uživatelským recenzím
 */

public class ReviewDAOImpl implements ReviewDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public ReviewDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Uložení recenze do databáze
	 */
	
	@Override
	public void saveReview(Review review) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"INSERT INTO pc_island.reviews (id_user, user_full_name, product_name, pros, cons, rating, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?)");
			
			ps.setInt(1, review.getIdUser());
			ps.setString(2, review.getUserFullName());
			ps.setString(3, review.getProductName());
			ps.setString(4, review.getPros());
			ps.setString(5, review.getCons());
			ps.setInt(6, review.getRating());
			LocalDate localDate = review.getCreationDate();
			ps.setDate(7, Date.valueOf(localDate));
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (recenze)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Načtení všech nenulových recenzí podle zadaného produktu
	 */
	
	@Override
	public List<Review> getProductNotNullReviews(String productName) {
		List<Review> reviews = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.reviews WHERE product_name = '" + productName + "' AND (pros IS NOT NULL OR cons IS NOT NULL) ORDER BY creation_date DESC");
			
			while (resultSet.next()) {
				Review review = new Review();
				review.setIdReview(resultSet.getInt("id_review"));
				review.setIdUser(resultSet.getInt("id_user"));
				review.setUserFullName(resultSet.getString("user_full_name"));
				review.setProductName(resultSet.getString("product_name"));
				review.setPros(resultSet.getString("pros"));
				review.setCons(resultSet.getString("cons"));
				review.setRating(resultSet.getInt("rating"));
				Date date = resultSet.getDate("creation_date");
				review.setCreationDate(date.toLocalDate());
				reviews.add(review);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data do databáze (recenze - produktu)");
			e.printStackTrace();
		}
		
		return reviews;
	}
	
	/**
	 * 	Načtení uživatelských recenzí podle přihlášeného uživatele
	 */
	
	@Override
	public List<Review> getUsersNotNullReviews(int idUser) {
		List<Review> reviews = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.reviews WHERE id_user = '" + idUser + "' AND (pros IS NOT NULL OR cons IS NOT NULL) ORDER BY creation_date DESC");
			
			while (resultSet.next()) {
				Review review = new Review();
				review.setIdReview(resultSet.getInt("id_review"));
				review.setIdUser(resultSet.getInt("id_user"));
				review.setUserFullName(resultSet.getString("user_full_name"));
				review.setProductName(resultSet.getString("product_name"));
				review.setPros(resultSet.getString("pros"));
				review.setCons(resultSet.getString("cons"));
				review.setRating(resultSet.getInt("rating"));
				Date date = resultSet.getDate("creation_date");
				review.setCreationDate(date.toLocalDate());
				reviews.add(review);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (recenze - uživatele)");
			e.printStackTrace();
		}
		
		return reviews;
	}
	
	/**
	 * 	Načtení všech nenulových recenzí produktu jednoho uživatele
	 */
	@Override
	public List<Review> getProductNotNullReviewsFromUser(String productName, int idUser) {
		List<Review> reviews = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
		
			Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(
		"SELECT * FROM pc_island.reviews WHERE (product_name = '" + productName + "' AND id_user = '" + idUser + "') AND (pros IS NOT NULL OR cons IS NOT NULL) ORDER BY creation_date DESC");
		
		while (resultSet.next()) {
			Review review = new Review();
			review.setIdReview(resultSet.getInt("id_review"));
			review.setIdUser(resultSet.getInt("id_user"));
			review.setUserFullName(resultSet.getString("user_full_name"));
			review.setProductName(resultSet.getString("product_name"));
			review.setPros(resultSet.getString("pros"));
			review.setCons(resultSet.getString("cons"));
			review.setRating(resultSet.getInt("rating"));
			Date date = resultSet.getDate("creation_date");
			review.setCreationDate(date.toLocalDate());
			reviews.add(review);
		}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (recenze - produktu, uživatele)");
			e.printStackTrace();
		}
		
		return reviews;
	}
	
	/**
	 * 	Načtení všech nenulových recenzí
	 */
	
	@Override
	public List<Review> getAllNotNullReviews() {
		List<Review> reviews = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.reviews WHERE (pros IS NOT NULL OR cons IS NOT NULL) ORDER BY creation_date DESC");
			
			while (resultSet.next()) {
				Review review = new Review();
				review.setIdReview(resultSet.getInt("id_review"));
				review.setIdUser(resultSet.getInt("id_user"));
				review.setUserFullName(resultSet.getString("user_full_name"));
				review.setProductName(resultSet.getString("product_name"));
				review.setPros(resultSet.getString("pros"));
				review.setCons(resultSet.getString("cons"));
				review.setRating(resultSet.getInt("rating"));
				Date date = resultSet.getDate("creation_date");
				review.setCreationDate(date.toLocalDate());
				reviews.add(review);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data do databáze (recenze - všechny)");
			e.printStackTrace();
		}
		
		return reviews;
	}
	
	/**
	 *	Upravení kladů recenze podle zadaného ID 
	 */
	
	@Override
	public void changeReviewPros(int idReview, String newPros) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.reviews SET pros = '" + newPros + "' WHERE id_review = '" + idReview + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (recenze - klady)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Upravení záporů recenze podle zadaného ID
	 */
	
	@Override
	public void changeReviewCons(int idReview, String newCons) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.reviews SET cons = '" + newCons + "' WHERE id_review = '" + idReview + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (recenze - zápory)");
			e.printStackTrace();
		}
	}
	
}
