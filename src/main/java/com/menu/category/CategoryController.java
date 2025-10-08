package com.menu.category;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/categories")
@Validated
@CrossOrigin(origins = "*")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getCategoriesByRestaurant(
            @PathVariable @NotNull(message = "Restaurant ID is required") Long restaurantId) {
        List<Category> categories = categoryService.getCategoriesByRestaurant(restaurantId);
        return ResponseEntity.ok()
                .body(categories);
    }

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Category> createCategory(
            @PathVariable @NotNull(message = "Restaurant ID is required") Long restaurantId,
            @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(category);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable @NotNull(message = "Category ID is required") String categoryId,
            @Valid @RequestBody CategoryRequest request) {
        Category updatedCategory = categoryService.updateCategory(categoryId, request);
        return ResponseEntity.ok()
                .body(updatedCategory);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable @NotNull(message = "Category ID is required") String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{categoryId}/visibility")
    public ResponseEntity<Category> updateCategoryVisibility(
            @PathVariable @NotNull(message = "Category ID is required") String categoryId,
            @RequestParam(required = true) boolean isVisible) {
        Category updatedCategory = categoryService.updateCategoryVisibility(categoryId, isVisible);
        return ResponseEntity.ok()
                .body(updatedCategory);
    }

    @PatchMapping("/restaurant/{restaurantId}/reorder/{categoryId}")
    public ResponseEntity<List<Category>> reorderCategory(
            @PathVariable @NotNull(message = "Restaurant ID is required") Long restaurantId,
            @PathVariable @NotNull(message = "Category ID is required") String categoryId,
            @RequestParam(required = true) String direction) {
        
        if (!isValidDirection(direction)) {
            return ResponseEntity.badRequest()
                    .build();
        }
        
        List<Category> updatedCategories = categoryService.reorderCategory(restaurantId, categoryId, direction);
        return ResponseEntity.ok()
                .body(updatedCategories);
    }

    private boolean isValidDirection(String direction) {
        return direction != null && 
               (direction.equalsIgnoreCase("up") || direction.equalsIgnoreCase("down"));
    }
}