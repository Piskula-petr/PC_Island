package cz.pcisland.order;

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
 *	Třída implementující rozhraní přístupu dat k objednávkám
 */
public class OrderDAOImpl implements OrderDAO,Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public OrderDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Uložení novou objednávku do databáze
	 */
	
	@Override
	public void saveOrder(Order order) {

		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"INSERT INTO orders (id_user, customer_full_name, customer_email, customer_phone_number, street_address, zip_code_and_city, country, creation_date, status, product_types, product_names, product_amount, product_prices, delivery_type_and_price, payment_type_and_price, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			ps.setInt(1, order.getIdUser());
			ps.setString(2, order.getCustomerFullName());
			ps.setString(3, order.getCustomerEmail());
			ps.setInt(4, order.getCustomerPhoneNumber());
			ps.setString(5, order.getStreetAddress());
			ps.setString(6, order.getZipCodeAndCity());
			ps.setString(7, order.getCountry());
			LocalDate localDate = order.getCreationDate();
			ps.setDate(8, Date.valueOf(localDate));
			ps.setString(9, order.getStatus());
			ps.setString(10, order.getProductTypes());
			ps.setString(11, order.getProductNames());
			ps.setString(12, order.getProductAmount());
			ps.setString(13, order.getProductPrices());
			ps.setString(14, order.getDeliveryTypeAndPrice());
			ps.setString(15, order.getPaymentTypeAndPrice());
			ps.setInt(16, order.getTotalPrice());
			
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (objednávky)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Načte všechny objednávky z databáze
	 */
	@Override
	public List<Order> getAllOrders() {
		List<Order> orders = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM orders ORDER BY creation_date DESC");
			
			while (resultSet.next()) {
				
				Order order = new Order();
				order.setIdOrder(resultSet.getInt("id_order"));
				order.setIdUser(resultSet.getInt("id_user"));
				order.setCustomerFullName(resultSet.getString("customer_full_name"));
				order.setCustomerEmail(resultSet.getString("customer_email"));
				order.setCustomerPhoneNumber(resultSet.getInt("customer_phone_number"));
				order.setStreetAddress(resultSet.getString("street_address"));
				order.setZipCodeAndCity(resultSet.getString("zip_code_and_city"));
				order.setCountry(resultSet.getString("country"));
				
				Date date = resultSet.getDate("creation_date");
				order.setCreationDate(date.toLocalDate());
				order.setStatus(resultSet.getString("status"));
				order.setProductTypes(resultSet.getString("product_types"));
				order.setProductNames(resultSet.getString("product_names"));
				order.setProductAmount(resultSet.getString("product_amount"));
				order.setProductPrices(resultSet.getString("product_prices"));
				order.setDeliveryTypeAndPrice(resultSet.getString("delivery_type_and_price"));
				order.setPaymentTypeAndPrice(resultSet.getString("payment_type_and_price"));
				order.setTotalPrice(resultSet.getInt("total_price"));
				orders.add(order);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data do databáze (objednávky)");
			e.printStackTrace();
		}
		
		return orders;
	}
	
	/**
	 * 	Načte objednávky podle přihlášeného uživatele
	 */
	@Override
	public List<Order> getUsersOrders(int idUser) {
		List<Order> orders = new ArrayList();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM orders WHERE id_user = '" + idUser + "' ORDER BY creation_date DESC");

			while (resultSet.next()) {
				
				Order order = new Order();
				order.setIdOrder(resultSet.getInt("id_order"));
				order.setIdUser(resultSet.getInt("id_user"));
				order.setCustomerFullName(resultSet.getString("customer_full_name"));
				order.setCustomerEmail(resultSet.getString("customer_email"));
				order.setCustomerPhoneNumber(resultSet.getInt("customer_phone_number"));
				order.setStreetAddress(resultSet.getString("street_address"));
				order.setZipCodeAndCity(resultSet.getString("zip_code_and_city"));
				order.setCountry(resultSet.getString("country"));
				
				Date date = resultSet.getDate("creation_date");
				order.setCreationDate(date.toLocalDate());
				order.setStatus(resultSet.getString("status"));
				order.setProductTypes(resultSet.getString("product_types"));
				order.setProductNames(resultSet.getString("product_Names"));
				order.setProductAmount(resultSet.getString("product_amount"));
				order.setProductPrices(resultSet.getString("product_prices"));
				order.setDeliveryTypeAndPrice(resultSet.getString("delivery_type_and_price"));
				order.setPaymentTypeAndPrice(resultSet.getString("payment_type_and_price"));
				order.setTotalPrice(resultSet.getInt("total_price"));
				orders.add(order);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data do databáze (objednávky)");
			e.printStackTrace();
		}
		
		return orders;
	}

	/**
	 * 	Načtení ID objednávky podle uživatele z databáze
	 */
	@Override
	public int getIdOrder(String userFullName) {
		int idOrder = 0;
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT id_order FROM orders WHERE customer_full_name = '" + userFullName + "' ORDER BY id_order DESC LIMIT 1");
			
			while (resultSet.next()) {
				idOrder = resultSet.getInt("id_order");
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (id objednávky)");
			e.printStackTrace();
		}
		
		return idOrder;
	}
	
	/**
	 * 	Zvýšení počtu prodaných kusů podle zadaného názvu produktu
	 */
	@Override
	public void incrementProductSales(String productName, int amount) {
		int sales = 0;
		
		// Načtení počtu prodaných kusů
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT sales FROM products WHERE name = '" + productName + "'");
			
			while (resultSet.next()) {
				sales = resultSet.getInt("sales");
			}
			
			sales = sales + amount;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (počet prodaných kusů)");
			e.printStackTrace();
		}
		
		// Uložení zvýšené hodnoty prodaných kusů
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET sales = '" + sales + "' WHERE name = '" + productName + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (počet prodaných kusů)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Snížení počtu kusů skladem podle zadaného názvu produktu
	 */
	@Override
	public void decrementProductStock(String productName, int amount) {
		int stock = 0;
		
		// Načtení počtu kusů skladem
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT stock FROM products WHERE name = '" + productName + "'");
			
			while (resultSet.next()) {
				stock = resultSet.getInt("stock");
			}
			
			stock = stock - amount;
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (počet kusů skladem)");
			e.printStackTrace();
		}
		
		// Uložení snížené hodnoty kusů skladem
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE products SET stock = '" + stock + "' WHERE name = '" + productName + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (počet kusů skladem)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Změna stavu objednávky podle zadaného ID
	 */
	@Override
	public void changeOrderStatus(int idOrder, String status) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE orders SET status = '" + status + "' WHERE id_order = '" + idOrder + "'");
			ps.executeUpdate();
		
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (změna stavu objednávky)");
			e.printStackTrace();
		}
	}
	
}
