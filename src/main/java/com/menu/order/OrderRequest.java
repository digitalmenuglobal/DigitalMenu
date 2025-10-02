package com.menu.order;

import java.time.Instant;
import java.util.List;


public class OrderRequest {
    public Long restaurantId;
    public List<OrderItemRequest> items;
    public String status;
    public double totalPrice;
    public int tableNumber;
    public String paymentStatus;
    public String customerName;
    public String customerPhoneNumber;
    public Instant createdAt;

    public static class OrderItemRequest {
        public Long menuItemId;
        public int quantity;
        public String name;
        public String description;
        public String type;
        public double price;
        public String category;
        public String image;
        public String specialInstructions;
    }
}
