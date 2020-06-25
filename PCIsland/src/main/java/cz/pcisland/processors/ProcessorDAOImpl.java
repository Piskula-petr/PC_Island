package cz.pcisland.processors;

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
 * 	Třída implementující rozhraní přístupu dat k procesorům
 */
public class ProcessorDAOImpl implements ProcessorDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public ProcessorDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Načtení procesoru z databáze podle zadaného ID
	 */
	@Override
	public Product getProcessor(int id) {
		Product processor = new Product();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				
				processor.setId(resultSet.getInt("id_product"));
				processor.setType(resultSet.getString("type"));
				processor.setName(resultSet.getString("name"));
				processor.setPrice(resultSet.getInt("price"));
				processor.setStock(resultSet.getInt("stock"));
				processor.setSales(resultSet.getInt("sales"));
				processor.setOverallRating(resultSet.getInt("overall_rating"));
				processor.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				processor.setParametrs("processor", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (procesor výběr)");
			e.printStackTrace();
		}
		
		return processor;
	}
	
	/**
	 * 	Načtení všech procesorů z databáze
	 */
	@Override
	public List<Product> getAllProcessors() {
		List<Product> processors = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM products WHERE type = 'processor' ORDER BY id_product");
			
			while(resultSet.next()) {
				
				Product processor = new Product();
				processor.setId(resultSet.getInt("id_product"));
				processor.setType(resultSet.getString("type"));
				processor.setName(resultSet.getString("name"));
				processor.setPrice(resultSet.getInt("price"));
				processor.setStock(resultSet.getInt("stock"));
				processor.setSales(resultSet.getInt("sales"));
				processor.setOverallRating(resultSet.getInt("overall_rating"));
				processor.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				processor.setParametrs("processor", resultSet.getString("parametrs"));
				processors.add(processor);
			}

		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (procesory - všechny)");
			e.printStackTrace();
		}
		
		return processors;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších procesorů
	 */
	@Override
	public List<Product> getTopSellingProcessors() {
		List<Product> processors = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM products WHERE type = 'processor' ORDER BY sales DESC LIMIT 3");
			
			while(resultSet.next()) {
				
				Product processor = new Product();
				processor.setId(resultSet.getInt("id_product"));
				processor.setType(resultSet.getString("type"));
				processor.setName(resultSet.getString("name"));
				processor.setPrice(resultSet.getInt("price"));
				processor.setStock(resultSet.getInt("stock"));
				processor.setSales(resultSet.getInt("sales"));
				processor.setOverallRating(resultSet.getInt("overall_rating"));
				processor.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				processor.setParametrs("processor", resultSet.getString("parametrs"));
				processors.add(processor);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (procesory - nejprodávanější)");
			e.printStackTrace();
		}
		
		return processors;
	}
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 */
	@Override
	public void incrementNumberOfPreview(int processorId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM products WHERE id_product = '" + processorId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (procesory - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + processorId + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (procesory - počet recenzí)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Přičtení hodnocení procesoru podle zadaného ID
	 */
	@Override
	public void addRating(int processorId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM products WHERE id_product = '" + processorId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (procesory - celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + processorId + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (procesory - celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
