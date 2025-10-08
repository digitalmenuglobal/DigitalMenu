-- Insert sample categories
INSERT INTO categories (id, name, is_visible, display_order, restaurant_id, created_at, updated_at)
VALUES 
('cat1', 'Starters', true, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat2', 'Main Course', true, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat3', 'Desserts', true, 3, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat4', 'Beverages', true, 4, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);