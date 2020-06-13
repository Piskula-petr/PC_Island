package cz.pcisland.graphics_cards;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cz.pcisland.base_page.DatabaseConnection;
import cz.pcisland.product.Product;

/**
*	Třída implementující rozhraní přístupu dat ke grafickým kartám
*/
public class GraphicsCardDAOImpl implements GraphicsCardDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public GraphicsCardDAOImpl() {
				
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Načtení grafické karty z databáze podle zadaného ID
	 */
	@Override
	public Product getGraphicsCard(int id) {
		Product graphicsCard = new Product();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				graphicsCard.setId(resultSet.getInt("id_product"));
				graphicsCard.setType(resultSet.getString("type"));
				graphicsCard.setName(resultSet.getString("name"));
				graphicsCard.setPrice(resultSet.getInt("price"));
				graphicsCard.setStock(resultSet.getInt("stock"));
				graphicsCard.setSales(resultSet.getInt("sales"));
				graphicsCard.setOverallRating(resultSet.getInt("overall_rating"));
				graphicsCard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				graphicsCard.setParametrs("graphics_card", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (grafická karta - výběr)");
			e.printStackTrace();
		}
		
		return graphicsCard;
	}
	
	/**
	 * 	Načtení všech grafických karet z databáze
	 */
	@Override
	public List<Product> getAllGraphicsCards() {
		List<Product> graphicsCards = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE type = 'graphics_card' ORDER BY id_product");
			
			while (resultSet.next()) {
				
				Product graphicsCard = new Product();
				graphicsCard.setId(resultSet.getInt("id_product"));
				graphicsCard.setType(resultSet.getString("type"));
				graphicsCard.setName(resultSet.getString("name"));
				graphicsCard.setPrice(resultSet.getInt("price"));
				graphicsCard.setStock(resultSet.getInt("stock"));
				graphicsCard.setSales(resultSet.getInt("sales"));
				graphicsCard.setOverallRating(resultSet.getInt("overall_rating"));
				graphicsCard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				graphicsCard.setParametrs("graphics_card", resultSet.getString("parametrs"));
				graphicsCards.add(graphicsCard);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (grafické karty - všechny)");
			e.printStackTrace();
		}
		
		return graphicsCards;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších grafických karet
	 */
	@Override
	public List<Product> getTopSellingGraphicsCards() {
		List<Product> graphicsCards = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT TOP 3 * FROM pc_island.products WHERE type = 'graphics_card' ORDER BY sales DESC");
			
			while (resultSet.next()) {
				
				Product graphicsCard = new Product();
				graphicsCard.setId(resultSet.getInt("id_product"));
				graphicsCard.setType(resultSet.getString("type"));
				graphicsCard.setName(resultSet.getString("name"));
				graphicsCard.setPrice(resultSet.getInt("price"));
				graphicsCard.setStock(resultSet.getInt("stock"));
				graphicsCard.setSales(resultSet.getInt("sales"));
				graphicsCard.setOverallRating(resultSet.getInt("overall_rating"));
				graphicsCard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				graphicsCard.setParametrs("graphics_card", resultSet.getString("parametrs"));
				graphicsCards.add(graphicsCard);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (grafické karty - nejprodávanější)");
			e.printStackTrace();
		}
		
		return graphicsCards;
	}

	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 */
	@Override
	public void incrementNumberOfPreview(int graphicsCardId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM pc_island.products WHERE id_product = '" + graphicsCardId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (grafické karty - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + graphicsCardId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (grafické karty - počet recenzí)");
			e.printStackTrace();
		}
	}

	/**
	 * 	Přičtení hodnocení grafické karty podle zadaného ID
	 */
	@Override
	public void addRating(int graphicsCardId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM pc_island.products WHERE id_product = '" + graphicsCardId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + graphicsCardId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
