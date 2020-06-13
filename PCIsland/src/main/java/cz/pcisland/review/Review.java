package cz.pcisland.review;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 	Třída recenze (Java Bean):
 * 
 * 		bezparametrový konstruktor,
 * 		gettery + settery atributů;
 */

public class Review implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// Parametry recenze
	private int idReview;
	private int idUser;
	private String userFullName;
	private String productName;
	private String pros;
	private String cons;
	private int rating;
	private LocalDate creationDate;
	
// Bezparametrový konstruktor ///////////////////////////////////////////////////////////////////////
	
	public Review() {
		
	}
	
// Gettery + Settery ////////////////////////////////////////////////////////////////////////////////
	
	public int getIdReview() {
		return idReview;
	}
	
	public void setIdReview(int idReview) {
		this.idReview = idReview;
	}
	
	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getUserFullName() {
		return userFullName;
	}
	
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getPros() {
		return pros;
	}
	
	public void setPros(String pros) {
		this.pros = pros;
	}
	
	public String getCons() {
		return cons;
	}
	
	public void setCons(String cons) {
		this.cons = cons;
	}
	
	public int getRating() {
		return rating;
	}
	
	public void setRating(int rating) {
		this.rating = rating;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
}
