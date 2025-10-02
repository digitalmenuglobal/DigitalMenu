
package com.menu.order;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create/{restaurantId}")
    public ResponseEntity<OrderEntity> createOrder(
            @PathVariable Long restaurantId,
            @RequestBody OrderRequest request) {
        OrderEntity order = orderService.createOrder(restaurantId, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<java.util.List<OrderEntity>> getOrdersByRestaurantId(@PathVariable Long restaurantId) {
        java.util.List<OrderEntity> orders = orderService.getOrdersByRestaurantId(restaurantId);
        return ResponseEntity.ok(orders);
    }
    @org.springframework.web.bind.annotation.PutMapping("/status/{orderId}")
    public ResponseEntity<OrderEntity> updateOrderStatus(
            @PathVariable Long orderId,
            @org.springframework.web.bind.annotation.RequestParam String status) {
        OrderEntity updated = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable Long orderId) {
        OrderEntity order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}
