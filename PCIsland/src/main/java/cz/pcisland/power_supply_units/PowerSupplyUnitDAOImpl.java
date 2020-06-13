package cz.pcisland.power_supply_units;

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
 *	Třída implementující rozhraní přístupu dat ke zdrojům
 */

public class PowerSupplyUnitDAOImpl implements PowerSupplyUnitDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public PowerSupplyUnitDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Načtení zdroje z databáze podle zadaného ID
	 */
	
	@Override
	public Product getPowerSupplyUnit(int id) {
		Product powerSupplyUnit = new Product();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet =  statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE id_product = '" + id + "'");
			
			while (resultSet.next()) {
				powerSupplyUnit.setId(resultSet.getInt("id_product"));
				powerSupplyUnit.setType(resultSet.getString("type"));
				powerSupplyUnit.setName(resultSet.getString("name"));
				powerSupplyUnit.setPrice(resultSet.getInt("price"));
				powerSupplyUnit.setStock(resultSet.getInt("stock"));
				powerSupplyUnit.setSales(resultSet.getInt("sales"));
				powerSupplyUnit.setOverallRating(resultSet.getInt("overall_rating"));
				powerSupplyUnit.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				powerSupplyUnit.setParametrs("power_supply_unit", resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (zdroj - výběr)");
			e.printStackTrace();
		}
		
		return powerSupplyUnit;
	}	
	
	/**
	 * 	Načtení všech zdrojů z databáze
	 */
	
	@Override
	public List<Product> getAllPowerSupplyUnits() {
		List<Product> powerSupplyUnits = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE type = 'power_supply_unit' ORDER BY id_product");
		
			while (resultSet.next()) {
				Product powerSupplyUnit = new Product();
				powerSupplyUnit.setId(resultSet.getInt("id_product"));
				powerSupplyUnit.setType(resultSet.getString("type"));
				powerSupplyUnit.setName(resultSet.getString("name"));
				powerSupplyUnit.setPrice(resultSet.getInt("price"));
				powerSupplyUnit.setStock(resultSet.getInt("stock"));
				powerSupplyUnit.setSales(resultSet.getInt("sales"));
				powerSupplyUnit.setOverallRating(resultSet.getInt("overall_rating"));
				powerSupplyUnit.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				powerSupplyUnit.setParametrs("power_supply_unit", resultSet.getString("parametrs"));
				powerSupplyUnits.add(powerSupplyUnit);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (zdroje - všechny)");
			e.printStackTrace();
		}
		
		return powerSupplyUnits;
	}
	
	/**
	 * 	Načtení 3 nejprodávanějších zdrojů
	 */
	
	@Override
	public List<Product> getTopSellingPowerSupplyUnits() {
		List<Product> powerSupplyUnits = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT TOP 3 * FROM pc_island.products WHERE type = 'power_supply_unit' ORDER BY sales DESC");
		
			while (resultSet.next()) {
				Product powerSupplyUnit = new Product();
				powerSupplyUnit.setId(resultSet.getInt("id_product"));
				powerSupplyUnit.setType(resultSet.getString("type"));
				powerSupplyUnit.setName(resultSet.getString("name"));
				powerSupplyUnit.setPrice(resultSet.getInt("price"));
				powerSupplyUnit.setStock(resultSet.getInt("stock"));
				powerSupplyUnit.setSales(resultSet.getInt("sales"));
				powerSupplyUnit.setOverallRating(resultSet.getInt("overall_rating"));
				powerSupplyUnit.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				powerSupplyUnit.setParametrs("power_supply_unit", resultSet.getString("parametrs"));
				powerSupplyUnits.add(powerSupplyUnit);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (zdroje - nejprodávanější)");
			e.printStackTrace();
		}
		
		return powerSupplyUnits;
	}
	
	/**
	 * 	Zvýšení počtu recenzí podle zadaného ID
	 */
	
	@Override
	public void incrementNumberOfPreview(int powerSupplyUnitId) {
		int numberOfPreview = 0;
		
		// Načtení počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT number_of_preview FROM pc_island.products WHERE id_product = '" + powerSupplyUnitId + "'");
			
			while (resultSet.next()) {
				numberOfPreview = resultSet.getInt("number_of_preview");
			}
			numberOfPreview++;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (zdroje - počet recenzí)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty počtu recenzí
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET number_of_preview = '" + numberOfPreview + "' WHERE id_product = '" + powerSupplyUnitId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (zdroje - počet recenzí)");
			e.printStackTrace();
		}
	}

	/**
	 * 	Přičtení hodnocení zdroje podle zadaného ID
	 */
	
	@Override
	public void addRating(int powerSupplyUnitId, int rating) {
		int overallRating = 0;
		
		// Načtení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT overall_rating FROM pc_island.products WHERE id_product = '" + powerSupplyUnitId + "'");
			
			while (resultSet.next()) {
				overallRating = resultSet.getInt("overall_rating");
			}
			overallRating = overallRating + rating;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (zdroje - celkové hodnocení)");
			e.printStackTrace();
		}
		
		// Uložení celkového hodnocení
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET overall_rating = '" + overallRating + "' WHERE id_product = '" + powerSupplyUnitId + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (zdroje - celkové hodnocení)");
			e.printStackTrace();
		}
	}
	
}
