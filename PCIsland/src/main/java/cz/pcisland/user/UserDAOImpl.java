package cz.pcisland.user;

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
 * 	Třída implementující rozhraní přístupu dat k uživatelům
 */
public class UserDAOImpl implements UserDAO, Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje
	private DatabaseConnection databaseConnection = new DatabaseConnection();
	
// Konstruktor ////////////////////////////////////////////////////////////////////////////////////////
	
	public UserDAOImpl() {
		
		try {
			Class.forName(databaseConnection.getDriver());
		
		} catch (ClassNotFoundException e) {
			System.out.println("Nepodařilo se načíst databázový ovladač");
			e.printStackTrace();
		}
	}

	/**
	 * 	Uložení nového uživatele do databáze
	 */
	@Override
	public void saveUser(User user) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"INSERT INTO users (email, password, name, surname, gender, phone_number, street_address, city, zip_code, country) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getName());
			ps.setString(4, user.getSurname());
			ps.setString(5, user.getGender());
			ps.setInt(6, user.getPhoneNumber());
			ps.setString(7, user.getStreetAddress());
			ps.setString(8, user.getCity());
			ps.setInt(9, user.getZipCode());
			ps.setString(10, user.getCountry());
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (uživatelé)");
			e.printStackTrace();
		}
	}

	/**
	 * 	Načtení všech uživatelů z databáze
	 */
	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM users ORDER BY id_user");
			
			while(resultSet.next()) {
				
				User user = new User();
				user.setId(resultSet.getInt("id_user"));
				user.setEmail(resultSet.getString("email"));
				user.setPassword(resultSet.getString("password"));
				user.setName(resultSet.getString("name"));
				user.setSurname(resultSet.getString("surname"));
				user.setGender(resultSet.getString("gender"));
				user.setPhoneNumber(resultSet.getInt("phone_number"));
				user.setStreetAddress(resultSet.getString("street_address"));
				user.setCity(resultSet.getString("city"));
				user.setZipCode(resultSet.getInt("zip_code"));
				user.setCountry(resultSet.getString("country"));
				users.add(user);
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (uživatelé)");
			e.printStackTrace();
		}
		
		return users;
	}
	
	/**
	 * 	Načtení všech států z databáze
	 */
	@Override
	public List<String> getCountries() {
		List<String> countries = new ArrayList<>();
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(
			"SELECT * FROM countries ORDER BY id_country");
			
			while (resultSet.next()) {
				countries.add(resultSet.getString("country_name"));
			}
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se načíst data z databáze (státy)");
			e.printStackTrace();
		}
		
		return countries;
	}
	
	/**
	 * 	Uložení nového emailu podle zadaného emailu uživatele
	 */
	@Override
	public void changeEmail(String currentUserEmail, String newEmail) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE users SET email = '" + newEmail + "' WHERE email = '" + currentUserEmail + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (změna emailu)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Uložení nového heslo podle zadaného emailu uživatele
	 */
	@Override
	public void changePassword(String currentUserEmail, String newPassword) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE users SET password = '" + newPassword + "' WHERE email = '" + currentUserEmail + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (změna hesla)");
			e.printStackTrace();
		}
	}
	
	/**
	 * 	Uložení nových dodacích údajů podle zadaného emailu uživatele 
	 */
	@Override
	public void changeDeliveryData(String currentUserEmail, String columnName, String newValue) {
		
		try (Connection connection = DriverManager.getConnection(databaseConnection.getConnection(), 
																 databaseConnection.getUsername(), 
																 databaseConnection.getPassword())) {
			
			PreparedStatement ps = connection.prepareStatement(
			"UPDATE users SET " + columnName + " = '" + newValue + "' WHERE email = '" + currentUserEmail + "'");
			ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("Nepodařilo se uložit data do databáze (dodací údaje)");
			e.printStackTrace();
		}
	}
	
}
