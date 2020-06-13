package cz.pcisland.user;

import java.io.Serializable;

/**
 * 	Třída uživatele:
 * 
 * 		bezparametrový konstruktor,
 * 		gettery + settery atributů;
 */

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Parametry uživatele
	private int id;
	private String email;
	private String password;
	private String name;
	private String surname;
	private String gender;
	private Integer phoneNumber;
	private String streetAddress;
	private String city;
	private Integer zipCode;
	private String country;
	
// Bezparametrový konstruktor ///////////////////////////////////////////////////////////////////////
	
	public User() {
		
	}
	
// Gettery + Settery ////////////////////////////////////////////////////////////////////////////////
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
