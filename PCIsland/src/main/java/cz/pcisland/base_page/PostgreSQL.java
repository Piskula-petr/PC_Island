package cz.pcisland.base_page;

import java.io.Serializable;

/**
 *	Třída uchovávající přístupové údaje k PostgreSQL databázi
 */

public class PostgreSQL implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Přístupové údaje k databázi
	private final String CONNECTION = "jdbc:postgresql://localhost/pc_island?serverTimezone=UTC";
	private final String USERNAME = "postgres";
	private final String PASSWORD = "123abc";
	
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
