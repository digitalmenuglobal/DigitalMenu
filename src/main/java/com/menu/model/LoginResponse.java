package com.menu.model;

public class LoginResponse {

	
	private String restaurantName;
	private String email;
	private String phoneNumber;
	private String logo;
	private String token;
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	@Override
	public String toString() {
		return "LoginResponse [restaurantName=" + restaurantName + ", email=" + email + ", phoneNumber=" + phoneNumber
				+ ", logo=" + logo + ", token=" + token + "]";
	}
	
	
	
	
}
