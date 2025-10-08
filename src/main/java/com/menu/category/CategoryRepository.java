package com.menu.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    List<Category> findByRestaurantIdOrderByDisplayOrder(Long restaurantId);
    boolean existsByRestaurantIdAndDisplayOrder(Long restaurantId, Integer displayOrder);
}