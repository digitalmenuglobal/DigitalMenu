
package com.menu.menuitem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.menu.user.User;
import com.menu.user.UserRepository;


@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    public MenuItemService(MenuItemRepository menuItemRepository, UserRepository userRepository) {
        this.menuItemRepository = menuItemRepository;
        this.userRepository = userRepository;
    }

     @Transactional
    public List<MenuItem> addMenuItems(Long restaurantId, List<MenuItemRequest> items) {
        User restaurant = userRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        List<MenuItem> saved = new ArrayList<>();
        for (MenuItemRequest req : items) {
            MenuItem item = new MenuItem();
            item.setName(req.name);
            item.setDescription(req.description);
            item.setType(req.type);
            item.setPrice(req.price);
            item.setQuantity(req.quantity);
            item.setCategory(req.category);
            item.setImage(req.image);
            item.setRestaurant(restaurant);
            saved.add(menuItemRepository.save(item));
        }
        return saved;
    }
    public List<MenuItem> getMenuItemsByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }
    @Transactional
    public MenuItem updateMenuItem(Long menuItemId, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        item.setName(request.name);
        item.setDescription(request.description);
        item.setType(request.type);
        item.setPrice(request.price);
        item.setQuantity(String.valueOf(request.quantity));
        item.setCategory(request.category);
        item.setImage(request.image);
        return menuItemRepository.save(item);
    }

    @Transactional
    public void deleteMenuItem(Long menuItemId) {
        if (!menuItemRepository.existsById(menuItemId)) {
            throw new IllegalArgumentException("Menu item not found");
        }
        menuItemRepository.deleteById(menuItemId);
    }
    @Transactional
    public MenuItem updateMenuItemAvailability(Long menuItemId, boolean isAvailable) {
        MenuItem item = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new IllegalArgumentException("Menu item not found"));
        item.setAvailable(isAvailable);
        return menuItemRepository.save(item);
    }
}
