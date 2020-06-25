package cz.pcisland.hard_disks;

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
*	Třída implementující rozhraní přístupu dat k pevným diskům
*/
public class HardDiskDAOImpl implements HardDiskDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor /////////////////////////////////////////////////////////////////////////////////////////
	
	public HardDiskDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Načtení pevného disku z databáze podle zadaného ID
	 */
	@Override
	public Product getHardDisk(int id) {
		Product hardDisk = new Product();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				
				hardDisk.setId(resultSet.getInt("id_product"));
				hardDisk.setType(resultSet.getString("type"));
				hardDisk.setName(resultSet.getString("name"));
				hardDisk.setPrice(resultSet.getInt("price"));
				hardDisk.setStock(resultSet.getInt("stock"));
				hardDisk.setSales(resultSet.getInt("sales"));
				hardDisk.setOverallRating(resultSet.getInt("overall_rating"));
				hardDisk.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				hardDisk.setParametrs("hard_disk", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (pevný disk - výběr)");
			e.printStackTrace();
		}
		
		return hardDisk;
	}
	
	/**
	 * 	Načtení všech pevných disků z databáze
	 */
	@Override
	public List<Product> getAllHardDisks() {
		List<Product> hardDisks = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE type = 'hard_disk' ORDER BY id_product");
			
			while (resultSet.next()) {
				
				Product hardDisk = new Product();
				hardDisk.setId(resultSet.getInt("id_product"));
				hardDisk.setType(resultSet.getString("type"));
				hardDisk.setName(resultSet.getString("name"));
				hardDisk.setPrice(resultSet.getInt("price"));
				hardDisk.setStock(resultSet.getInt("stock"));
				hardDisk.setSales(resultSet.getInt("sales"));
				hardDisk.setOverallRating(resultSet.getInt("overall_rating"));
				hardDisk.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				hardDisk.setParametrs("hard_disk", resultSet.getString("parametrs"));
				hardDisks.add(hardDisk);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (pevné disky - všechny)");
			e.printStackTrace();
		}
		
		return hardDisks;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších pevných disků
	 */
	@Override
	public List<Product> getTopSellingHardDisks() {
		List<Product> hardDisks = new ArrayList<>();
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT TOP 3 * FROM pc_island.products WHERE type = 'hard_disk' ORDER BY sales DESC");
			
			while (resultSet.next()) {
				
				Product hardDisk = new Product();
				hardDisk.setId(resultSet.getInt("id_product"));
				hardDisk.setType(resultSet.getString("type"));
				hardDisk.setName(resultSet.getString("name"));
				hardDisk.setPrice(resultSet.getInt("price"));
				hardDisk.setStock(resultSet.getInt("stock"));
				hardDisk.setSales(resultSet.getInt("sales"));
				hardDisk.setOverallRating(resultSet.getInt("overall_rating"));
				hardDisk.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				hardDisk.setParametrs("hard_disk", resultSet.getString("parametrs"));
				hardDisks.add(hardDisk);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (pevné disky - nejprodávanější)");
			e.printStackTrace();
		}
		
		return hardDisks;
	}

	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID 
	 */
	@Override
	public void incrementNumberOfPreview(int hardDiskId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM pc_island.products WHERE id_product = '" + hardDiskId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (pevné disky - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + hardDiskId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (pevné disky - počet recenzí)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Přičtení hodnocení pevného disku podle zadaného ID
	 */
	@Override
	public void addRating(int hardDiskId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM pc_island.products WHERE id_product = '" + hardDiskId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (pevné disky - celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + hardDiskId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (pevné disky - celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
