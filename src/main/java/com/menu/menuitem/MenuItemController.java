
package com.menu.menuitem;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu")
public class MenuItemController {
    @GetMapping("/public/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurantPublic(@PathVariable Long restaurantId) {
        List<MenuItem> items = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(items);
    }
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }


    @PostMapping("/add/{restaurantId}")
    public ResponseEntity<List<MenuItem>> addMenuItems(
            @PathVariable Long restaurantId,
            @RequestBody List<MenuItemRequest> items) {
        List<MenuItem> saved = menuItemService.addMenuItems(restaurantId, items);
        return new ResponseEntity<List<MenuItem>>(saved, HttpStatus.CREATED);
    }


    @PutMapping("/update/{menuItemId}")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable Long menuItemId,
            @RequestBody MenuItemRequest request) {
        MenuItem updated = menuItemService.updateMenuItem(menuItemId, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/availability/{menuItemId}")
    public ResponseEntity<MenuItem> updateMenuItemAvailability(
            @PathVariable Long menuItemId,
            @org.springframework.web.bind.annotation.RequestParam boolean isAvailable) {
        MenuItem updated = menuItemService.updateMenuItemAvailability(menuItemId, isAvailable);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{menuItemId}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long menuItemId) {
        menuItemService.deleteMenuItem(menuItemId);
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItem> items = menuItemService.getMenuItemsByRestaurant(restaurantId);
        return ResponseEntity.ok(items);
    }
}
