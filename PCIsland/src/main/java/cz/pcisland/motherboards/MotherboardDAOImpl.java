package cz.pcisland.motherboards;

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
 *	Třída implementující rozhraní přístupu dat k základním deskám
 */
public class MotherboardDAOImpl implements MotherboardDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public MotherboardDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Načtení základní desky z databáze podle zadaného ID
	 */
	@Override
	public Product getMotherboard(int id) {
		Product motherboard = new Product();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				
				motherboard.setId(resultSet.getInt("id_product"));
				motherboard.setType(resultSet.getString("type"));
				motherboard.setName(resultSet.getString("name"));
				motherboard.setPrice(resultSet.getInt("price"));
				motherboard.setStock(resultSet.getInt("stock"));
				motherboard.setSales(resultSet.getInt("sales"));
				motherboard.setOverallRating(resultSet.getInt("overall_rating"));
				motherboard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				motherboard.setParametrs("motherboard", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (základní deska - výběr)");
			e.printStackTrace();
		}
		
		return motherboard;
	}
	
	/**
	 * 	Načtení všech základních desek z databáze
	 */
	@Override
	public List<Product> getAllMotherboards() {
		List<Product> motherboards = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE type = 'motherboard' ORDER BY id_product");
			
			while (resultSet.next()) {
				
				Product motherboard = new Product();
				motherboard.setId(resultSet.getInt("id_product"));
				motherboard.setType(resultSet.getString("type"));
				motherboard.setName(resultSet.getString("name"));
				motherboard.setPrice(resultSet.getInt("price"));
				motherboard.setStock(resultSet.getInt("stock"));
				motherboard.setSales(resultSet.getInt("sales"));
				motherboard.setOverallRating(resultSet.getInt("overall_rating"));
				motherboard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				motherboard.setParametrs("motherboard", resultSet.getString("parametrs"));
				motherboards.add(motherboard);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (základní desky - všechny)");
			e.printStackTrace();
		}
		
		return motherboards;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších základních desek
	 */
	@Override
	public List<Product> getTopSellingMotherboards() {
		List<Product> motherboards = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT TOP 3 * FROM pc_island.products WHERE type = 'motherboard' ORDER BY sales DESC");
			
			while (resultSet.next()) {
				
				Product motherboard = new Product();
				motherboard.setId(resultSet.getInt("id_product"));
				motherboard.setType(resultSet.getString("type"));
				motherboard.setName(resultSet.getString("name"));
				motherboard.setPrice(resultSet.getInt("price"));
				motherboard.setStock(resultSet.getInt("stock"));
				motherboard.setSales(resultSet.getInt("sales"));
				motherboard.setOverallRating(resultSet.getInt("overall_rating"));
				motherboard.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				motherboard.setParametrs("motherboard", resultSet.getString("parametrs"));
				motherboards.add(motherboard);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (základní desky - nejprodávanější)");
			e.printStackTrace();
		}
		
		return motherboards;
	}
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 */
	
	@Override
	public void incrementNumberOfPreview(int motherboardId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM pc_island.products WHERE id_product = '" + motherboardId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (základní desky - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + motherboardId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (základní desky - počet recenzí)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Přičtení hodnocení základní desky podle zadaného ID
	 */
	@Override
	public void addRating(int motherboardId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM pc_island.products WHERE id_product = '" + motherboardId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (základní desky - celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + motherboardId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (základní desky - celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
