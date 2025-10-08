package com.menu.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.user.User;
import com.menu.user.UserRepository;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Category createCategory(Long restaurantId, CategoryRequest request) {
        User restaurant = userRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));

        // Check if display order already exists for this restaurant
        if (categoryRepository.existsByRestaurantIdAndDisplayOrder(restaurant.getId(), request.getDisplayOrder())) {
            throw new IllegalArgumentException("Display order already exists for this restaurant");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setVisible(request.isVisible());
        category.setDisplayOrder(request.getDisplayOrder());
        category.setRestaurant(restaurant);

        return categoryRepository.save(category);
    }

    public List<Category> getCategoriesByRestaurant(Long restaurantId) {
        return categoryRepository.findByRestaurantIdOrderByDisplayOrder(restaurantId);
    }

    @Transactional
    public Category updateCategory(String categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Check if new display order conflicts with existing ones
        if (!category.getDisplayOrder().equals(request.getDisplayOrder()) &&
            categoryRepository.existsByRestaurantIdAndDisplayOrder(
                category.getRestaurant().getId(), 
                request.getDisplayOrder())) {
            throw new IllegalArgumentException("Display order already exists for this restaurant");
        }

        category.setName(request.getName());
        category.setVisible(request.isVisible());
        category.setDisplayOrder(request.getDisplayOrder());

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category not found");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Transactional
    public Category updateCategoryVisibility(String categoryId, boolean isVisible) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setVisible(isVisible);
        return categoryRepository.save(category);
    }

    @Transactional
    public List<Category> reorderCategory(Long restaurantId, String categoryId, String direction) {
        // 1. Get current categories ordered by display_order
        List<Category> categories = categoryRepository.findByRestaurantIdOrderByDisplayOrder(restaurantId);
        
        // 2. Find current index
        int currentIndex = -1;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equals(categoryId)) {
                currentIndex = i;
                break;
            }
        }
        
        if (currentIndex == -1) {
            throw new IllegalArgumentException("Category not found");
        }

        // 3. Calculate swap index and validate move
        int swapIndex = direction.equalsIgnoreCase("up") ? currentIndex - 1 : currentIndex + 1;
        
        if (swapIndex < 0 || swapIndex >= categories.size()) {
            throw new IllegalArgumentException("Invalid move: Cannot move " + direction);
        }

        // 4. Swap display orders
        Category currentCategory = categories.get(currentIndex);
        Category swapCategory = categories.get(swapIndex);
        
        Integer tempOrder = currentCategory.getDisplayOrder();
        currentCategory.setDisplayOrder(swapCategory.getDisplayOrder());
        swapCategory.setDisplayOrder(tempOrder);

        // Save both categories
        categoryRepository.save(currentCategory);
        categoryRepository.save(swapCategory);

        // 5. Return updated list
        return categoryRepository.findByRestaurantIdOrderByDisplayOrder(restaurantId);
    }
}