package cz.pcisland.base_page;

import java.io.Serializable;

/**
 *	Třída uchovávající přístupové údaje k databázi
 */
public class DatabaseConnection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje k Azure databázi
	private final String DRIVER = "org.postgresql.Driver";
	private final String CONNECTION = "jdbc:postgresql://localhost/pc_island";
	
	private final String USERNAME = "postgres";
	private final String PASSWORD = "123abc";
	
// Gettery ///////////////////////////////////////////////////////////////////////////////////////////////
	
	public String getDriver() {
		return DRIVER;
	}

	public String getConnection() {
		return CONNECTION;
	}
	
	public String getUsername() {
		return USERNAME;
	}

	public String getPassword() {
		return PASSWORD;
	}
	
}
