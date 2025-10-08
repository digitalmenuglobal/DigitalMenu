package com.menu.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
    @Query("SELECT c FROM Category c WHERE c.restaurant.id = :restaurantId ORDER BY c.displayOrder ASC")
    List<Category> findCategoriesByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c " +
           "WHERE c.restaurant.id = :restaurantId AND c.displayOrder = :displayOrder")
    boolean existsByRestaurantAndDisplayOrder(
        @Param("restaurantId") Long restaurantId, 
        @Param("displayOrder") Integer displayOrder
    );
}