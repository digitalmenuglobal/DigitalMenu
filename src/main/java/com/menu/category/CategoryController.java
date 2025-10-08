package com.menu.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getCategoriesByRestaurant(
            @PathVariable Long restaurantId) {
        List<Category> categories = categoryService.getCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Category> createCategory(
            @PathVariable Long restaurantId,
            @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(restaurantId, request);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String categoryId,
            @RequestBody CategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{categoryId}/visibility")
    public ResponseEntity<Category> updateCategoryVisibility(
            @PathVariable String categoryId,
            @RequestParam boolean isVisible) {
        Category updatedCategory = categoryService.updateCategoryVisibility(categoryId, isVisible);
        return ResponseEntity.ok(updatedCategory);
    }

    @PatchMapping("/restaurant/{restaurantId}/reorder/{categoryId}")
    public ResponseEntity<List<Category>> reorderCategory(
            @PathVariable Long restaurantId,
            @PathVariable String categoryId,
            @RequestParam String direction) {
        if (!direction.equalsIgnoreCase("up") && !direction.equalsIgnoreCase("down")) {
            return ResponseEntity.badRequest().build();
        }
        List<Category> updatedCategories = categoryService.reorderCategory(restaurantId, categoryId, direction);
        return ResponseEntity.ok(updatedCategories);
    }
}