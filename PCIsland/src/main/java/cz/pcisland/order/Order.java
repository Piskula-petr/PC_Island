package cz.pcisland.order;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *	Třída objednávky:
 *
 *		bezparametrový konstruktor,
 *		gettery + settery atributů;
 */
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Paramatry objednávky
	private int idOrder;
	private int idUser;
	private String customerFullName;
	private String customerEmail;
	private int customerPhoneNumber;
	private String streetAddress;
	private String zipCodeAndCity;
	private String country;
	private LocalDate creationDate;
	private String status;
	private String productTypes;
	private String productNames;
	private String productAmount;
	private String productPrices;
	private String deliveryTypeAndPrice;
	private String paymentTypeAndPrice;
	private int totalPrice;
	
// Bezparametrový konstruktor ///////////////////////////////////////////////////////////////////////
	
	public Order() {
		
	}
	
// Gettery + Settery ////////////////////////////////////////////////////////////////////////////////
	
	public int getIdOrder() {
		return idOrder;
	}	
	
	public void setIdOrder(int idOrder) {
		this.idOrder = idOrder;
	}

	public int getIdUser() {
		return idUser;
	}
	
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	
	public String getCustomerFullName() {
		return customerFullName;
	}
	
	public void setCustomerFullName(String customerFullName) {
		this.customerFullName = customerFullName;
	}
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	public int getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}
	
	public void setCustomerPhoneNumber(int customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}
	
	public String getStreetAddress() {
		return streetAddress;
	}
	
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	
	public String getZipCodeAndCity() {
		return zipCodeAndCity;
	}
	
	public void setZipCodeAndCity(String zipCodeAndCity) {
		this.zipCodeAndCity = zipCodeAndCity;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public LocalDate getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(String productTypes) {
		this.productTypes = productTypes;
	}

	public String getProductNames() {
		return productNames;
	}

	public void setProductNames(String productNames) {
		this.productNames = productNames;
	}

	public String getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(String productAmount) {
		this.productAmount = productAmount;
	}

	public String getProductPrices() {
		return productPrices;
	}

	public void setProductPrices(String productPrices) {
		this.productPrices = productPrices;
	}

	public String getDeliveryTypeAndPrice() {
		return deliveryTypeAndPrice;
	}
	
	public void setDeliveryTypeAndPrice(String deliveryTypeAndPrice) {
		this.deliveryTypeAndPrice = deliveryTypeAndPrice;
	}
	
	public String getPaymentTypeAndPrice() {
		return paymentTypeAndPrice;
	}
	
	public void setPaymentTypeAndPrice(String paymentTypeAndPrice) {
		this.paymentTypeAndPrice = paymentTypeAndPrice;
	}
	
	public int getTotalPrice() {
		return totalPrice;
	}
	
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
