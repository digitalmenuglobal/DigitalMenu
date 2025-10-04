  
package com.menu.order;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.menuitem.MenuItemRepository;
import com.menu.user.User;
import com.menu.user.UserRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final com.menu.util.SmsService smsService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, MenuItemRepository menuItemRepository, com.menu.util.SmsService smsService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.smsService = smsService;
    }

    @Transactional
    public OrderEntity createOrder(Long restaurantId, OrderRequest request) {
        User restaurant = userRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        OrderEntity order = new OrderEntity();
        order.setRestaurant(restaurant);
        order.setStatus(request.status);
        order.setTotalPrice(request.totalPrice);
        order.setTableNumber(request.tableNumber);
        order.setPaymentStatus(request.paymentStatus);
        order.setCustomerName(request.customerName);
        order.setCustomerPhoneNumber(request.customerPhoneNumber);
    order.setCreatedAt(request.createdAt != null ? request.createdAt : Instant.now());

        List<OrderEntity.OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.OrderItemRequest itemReq : request.items) {
            OrderEntity.OrderItem item = new OrderEntity.OrderItem();
            item.setMenuItemId(itemReq.menuItemId);
            item.setQuantity(itemReq.quantity);
            item.setCategory(itemReq.category);
            item.setDescription(itemReq.description);
            item.setImage(itemReq.image);
            item.setName(itemReq.name);
            item.setPrice(itemReq.price);
            item.setType(itemReq.type);
            item.setSpecialInstructions(itemReq.specialInstructions);
            orderItems.add(item);
        }
        order.setItems(orderItems);
        order = orderRepository.save(order);
        
        // // Send SMS confirmation
        // try {
        //     smsService.sendOrderConfirmation(order);
        // } catch (Exception e) {
        //     // Log the error but don't stop the order process
        //     System.err.println("Failed to send order confirmation SMS: " + e.getMessage());
        // }
        return order;
    }
    public List<OrderEntity> getOrdersByRestaurantId(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public OrderEntity updateOrderStatus(Long orderId, String status) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    public OrderEntity getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
}
