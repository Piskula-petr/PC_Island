package cz.pcisland.base_page;

import java.io.Serializable;

/**
 *	Třída uchovávající přístupové údaje k databázi
 */

public class DatabaseConnection implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje k Azure databázi
	private final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private final String CONNECTION = "jdbc:sqlserver://pc-island-server.database.windows.net:1433;"
									+ "database=pc-island;"
									+ "encrypt=true;"
									+ "trustServerCertificate=false;"
									+ "hostNameInCertificate=*.database.windows.net;"
									+ "loginTimeout=30;";
	
	private final String USERNAME = "pc-island@pc-island-server";
	private final String PASSWORD = "123456Abc";
	
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
