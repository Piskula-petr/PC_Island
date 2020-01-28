package cz.pcisland.memory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cz.pcisland.base_page.PostgreSQL;
import cz.pcisland.product.Product;

/**
 *	Třída implementující rozhraní přístupu dat k operačním pamětem
 */

public class MemoryDAOImpl implements MemoryDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private PostgreSQL postgreSQL = new PostgreSQL();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public MemoryDAOImpl() {
		
		try {
			Class.forName("org.postgresql.Driver");
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Načtení operační paměťi z databáze podle zadaného ID
	 */
	
	@Override
	public Product getMemory(int id) {
		Product memory = new Product();
		
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				memory.setId(resultSet.getInt("id_product"));
				memory.setType(resultSet.getString("type"));
				memory.setName(resultSet.getString("name"));
				memory.setPrice(resultSet.getInt("price"));
				memory.setStock(resultSet.getInt("stock"));
				memory.setSales(resultSet.getInt("sales"));
				memory.setOverallRating(resultSet.getInt("overall_rating"));
				memory.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				memory.setParametrs("memory", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (operační pamět - výběr)");
			e.printStackTrace();
		}
		return memory;
	}
	
	/**
	 * 	Načtení všech operačních pamětí z databáze
	 */
	
	@Override
	public List<Product> getAllMemory() {
		List<Product> memories = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM products WHERE type = 'memory' ORDER BY id_product");
		
			while(resultSet.next()) {
				Product memory = new Product();
				memory.setId(resultSet.getInt("id_product"));
				memory.setType(resultSet.getString("type"));
				memory.setName(resultSet.getString("name"));
				memory.setPrice(resultSet.getInt("price"));
				memory.setStock(resultSet.getInt("stock"));
				memory.setSales(resultSet.getInt("sales"));
				memory.setOverallRating(resultSet.getInt("overall_rating"));
				memory.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				memory.setParametrs("memory", resultSet.getString("parametrs"));
				memories.add(memory);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (operační paměti - všechny)");
			e.printStackTrace();
		}
		return memories;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších operačních pamětí
	 */
	
	@Override
	public List<Product> getTopSellingMemory() {
		List<Product> memories = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM products WHERE type = 'memory' ORDER BY sales DESC LIMIT 3");
		
			while(resultSet.next()) {
				Product memory = new Product();
				memory.setId(resultSet.getInt("id_product"));
				memory.setType(resultSet.getString("type"));
				memory.setName(resultSet.getString("name"));
				memory.setPrice(resultSet.getInt("price"));
				memory.setStock(resultSet.getInt("stock"));
				memory.setSales(resultSet.getInt("sales"));
				memory.setOverallRating(resultSet.getInt("overall_rating"));
				memory.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				memory.setParametrs("memory", resultSet.getString("parametrs"));
				memories.add(memory);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (operační paměti - nejprodávanější)");
			e.printStackTrace();
		}
		return memories;
	}

	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 */
	
	@Override
	public void incrementNumberOfPreview(int memoryId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM products WHERE id_product = '" + memoryId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (operační paměti - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + memoryId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (operační paměti - počet recenzí)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Přičtení hodnocení operační paměti podle zadaného ID
	 */
	
	@Override
	public void addRating(int memoryId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM products WHERE id_product = '" + memoryId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (operační paměti - celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(postgreSQL.getConnection(), postgreSQL.getUsername(), postgreSQL.getPassword())) {
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + memoryId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (operační paměti - celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
