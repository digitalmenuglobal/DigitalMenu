package com.menu.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.menu.category.Category;
import com.menu.category.CategoryRepository;

@Configuration
public class DataInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    public CommandLineRunner initData(CategoryRepository categoryRepository) {
        return args -> {
            logger.info("Starting categories initialization...");

            // Check if we already have categories
            if (categoryRepository.count() > 0) {
                logger.info("Categories already exist, skipping initialization");
                return;
            }

            try {
                // Define default categories with their display order
                List<CategoryInit> defaultCategories = Arrays.asList(
                    new CategoryInit("Appetizers & Starters", 1),
                    new CategoryInit("Soups & Salads", 2),
                    new CategoryInit("Main Course", 3),
                    new CategoryInit("Biryanis & Rice", 4),
                    new CategoryInit("Breads & Rotis", 5),
                    new CategoryInit("Desserts", 6),
                    new CategoryInit("Beverages", 7),
                    new CategoryInit("Today's Special", 8)
                );

                // Create each category
                for (CategoryInit catInit : defaultCategories) {
                    Category category = new Category();
                    category.setName(catInit.name);
                    category.setVisible(true);
                    category.setDisplayOrder(catInit.order);
                    categoryRepository.save(category);
                    logger.info("Created category: {}", catInit.name);
                }

                logger.info("Categories initialization completed successfully");
            } catch (Exception e) {
                logger.error("Error during categories initialization", e);
                throw e;
            }
        };
    }

    // Helper class for category initialization
    private static class CategoryInit {
        String name;
        int order;

        CategoryInit(String name, int order) {
            this.name = name;
            this.order = order;
        }
    }
}