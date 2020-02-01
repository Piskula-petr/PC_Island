package cz.pcisland.product;

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

/**
 * 	Třída implementující rozhraní přístupu dat k produktům
 */

public class ProductDAOImpl implements ProductDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public ProductDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Načtení produktu podle zadaného názvu
	 */
	
	@Override
	public Product getProductByName(String name) {
		Product product = new Product();

		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE name = '" + name + "'");
			
			while (resultSet.next()) {
				product.setId(resultSet.getInt("id_product"));
				product.setType(resultSet.getString("type"));
				product.setName(resultSet.getString("name"));
				product.setPrice(resultSet.getInt("price"));
				product.setStock(resultSet.getInt("stock"));
				product.setSales(resultSet.getInt("sales"));
				product.setOverallRating(resultSet.getInt("overall_rating"));
				product.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				product.setParametrs(resultSet.getString("type") ,resultSet.getString("parametrs"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (produktu podle nátvu)");
			e.printStackTrace();
		}
		
		return product;
	}
	
	/**
	 * 	Načtení všech produktů z databáze
	 */
	
	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products ORDER BY id_product");
			
			while (resultSet.next()) {
				Product product = new Product();
				product.setId(resultSet.getInt("id_product"));
				product.setType(resultSet.getString("type"));
				product.setName(resultSet.getString("name"));
				product.setPrice(resultSet.getInt("price"));
				product.setStock(resultSet.getInt("stock"));
				product.setSales(resultSet.getInt("sales"));
				product.setOverallRating(resultSet.getInt("overall_rating"));
				product.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				product.setParametrs(resultSet.getString("type") ,resultSet.getString("parametrs"));
				products.add(product);
			}
					
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (všechny produkty)");
			e.printStackTrace();
		}
		
		return products;
	}
	
	/**
	 * 	Načtení všech produktů z databáze seřazených podle počtu kusů skladem
	 */
	
	@Override
	public List<Product> getAllProductSortByStock() {
		List<Product> products = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products ORDER BY stock");
			
			while (resultSet.next()) {
				Product product = new Product();
				product.setId(resultSet.getInt("id_product"));
				product.setType(resultSet.getString("type"));
				product.setName(resultSet.getString("name"));
				product.setPrice(resultSet.getInt("price"));
				product.setStock(resultSet.getInt("stock"));
				product.setSales(resultSet.getInt("sales"));
				product.setOverallRating(resultSet.getInt("overall_rating"));
				product.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				product.setParametrs(resultSet.getString("type") ,resultSet.getString("parametrs"));
				products.add(product);
			}
					
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (všechny produkty - prodeje)");
			e.printStackTrace();
		}
		
		return products;
	}

	/**
	 * 	Načtení produktů podle zadané společnosti
	 */
	
	@Override
	public List<Product> getProductsFromCompany(String companyName) {
		List<Product> products = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM pc_island.products WHERE name LIKE '%" + companyName + "%'");
			
			while (resultSet.next()) {
				Product product = new Product();
				product.setId(resultSet.getInt("id_product"));
				product.setType(resultSet.getString("type"));
				product.setName(resultSet.getString("name"));
				product.setPrice(resultSet.getInt("price"));
				product.setStock(resultSet.getInt("stock"));
				product.setSales(resultSet.getInt("sales"));
				product.setOverallRating(resultSet.getInt("overall_rating"));
				product.setNumberOfPreview(resultSet.getInt("number_of_preview"));
				product.setParametrs(resultSet.getString("type") ,resultSet.getString("parametrs"));
				products.add(product);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (produkty - AMD)");
			e.printStackTrace();
		}
		
		return products;
	}
	
	/**
	 * 	Změna počtu kusů skladem
	 */
	
	@Override
	public void changeStock(int idProduct, int newStock) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET stock = '" + newStock + "' WHERE id_product = '" + idProduct + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (produkty - změna množství)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Změna ceny produktu
	 */
	
	@Override
	public void changePrice(int idProduct, int newPrice) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
				 												 databaseConnection.getUsername(), 
				 												 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE pc_island.products SET price = '" + newPrice + "' WHERE id_product = '" + idProduct + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (produkty - změna ceny)");
			e.printStackTrace();
		}
	}
	
}
