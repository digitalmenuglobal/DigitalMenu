package com.menu.order;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.menu.user.User;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private double totalPrice;
    private int tableNumber;
    private String paymentStatus;
    private String customerName;
	private String customerPhoneNumber;
    private Instant createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User restaurant;

    @ElementCollection
    private List<OrderItem> items;
   
       

    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public double getTotalPrice() {
		return totalPrice;
	}



	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}



	public int getTableNumber() {
		return tableNumber;
	}



	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}



	public String getPaymentStatus() {
		return paymentStatus;
	}



	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}



	public String getCustomerName() {
		return customerName;
	}



	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}



	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}



	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}



	public Instant getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}



	public User getRestaurant() {
		return restaurant;
	}



	public void setRestaurant(User restaurant) {
		this.restaurant = restaurant;
	}



	public List<OrderItem> getItems() {
		return items;
	}



	public void setItems(List<OrderItem> items) {
		this.items = items;
	}



	@Embeddable
	public static class OrderItem {
		private Long menuItemId;
		private int quantity;
		private String name;
		private String description;
		private String type;
		private double price;
		private String category;
		private String image;
		private String specialInstructions;
		public String getSpecialInstructions() {
			return specialInstructions;
		}
		public void setSpecialInstructions(String specialInstructions) {
			this.specialInstructions = specialInstructions;
		}
	public Long getMenuItemId() {
		return menuItemId;
	}
	public void setMenuItemId(Long menuItemId) {
		this.menuItemId = menuItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    
       
    }
}
