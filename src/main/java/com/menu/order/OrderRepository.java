package com.menu.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	List<OrderEntity> findByRestaurantId(Long restaurantId);
}
