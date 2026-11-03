-- =============================================================================
-- Retail Management System - Sample Data
-- =============================================================================

-- Clear existing data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE order_items;
TRUNCATE TABLE order_details;
TRUNCATE TABLE inventory;
TRUNCATE TABLE customers;
TRUNCATE TABLE products;
TRUNCATE TABLE stores;
SET FOREIGN_KEY_CHECKS = 1;

-- -----------------------------------------------------------------------------
-- 1. STORES (5 Stores)
-- -----------------------------------------------------------------------------
INSERT INTO stores (id, name, address_line1, address_line2, address_line3) VALUES
(1, 'Downtown Flagship Store', '100 Market Street', 'Suite 100', 'San Francisco, CA 94105'),
(2, 'Metro Mall Outlet', '250 Broadway Avenue', 'Building B, 2nd Floor', 'New York, NY 10007'),
(3, 'Westside Tech Hub', '1200 Wilshire Boulevard', 'Unit 405', 'Los Angeles, CA 90017'),
(4, 'Uptown Electronics', '45 Michigan Avenue', 'Floor 1', 'Chicago, IL 60603'),
(5, 'Suburban Retail Plaza', '890 Peachtree Street NE', 'Shop 12', 'Atlanta, GA 30309');

-- -----------------------------------------------------------------------------
-- 2. PRODUCTS (Categories: Mobile, TV and AV, Home Appliances, Laptops and Monitors, Accessories)
-- -----------------------------------------------------------------------------
-- Mobile
INSERT INTO products (id, name, category, price, sku) VALUES
(1, 'Samsung Galaxy S24 Ultra', 'Mobile', 1199.99, 'MOB-SAM-S24U'),
(2, 'Apple iPhone 15 Pro Max', 'Mobile', 1299.99, 'MOB-APP-15PM'),
(3, 'Google Pixel 8 Pro', 'Mobile', 999.00, 'MOB-GOO-PX8P');

-- TV and AV
INSERT INTO products (id, name, category, price, sku) VALUES
(4, 'Sony Bravia 65 Inch 4K OLED', 'TV and AV', 1799.99, 'TAV-SNY-65OLED'),
(5, 'LG C3 55 Inch 4K OLED TV', 'TV and AV', 1399.99, 'TAV-LGE-55C3'),
(6, 'Bose Smart Soundbar 900', 'TV and AV', 749.00, 'TAV-BSE-SB900');

-- Home Appliances
INSERT INTO products (id, name, category, price, sku) VALUES
(7, 'Dyson V15 Detect Cordless Vacuum', 'Home Appliances', 699.99, 'HAP-DYS-V15'),
(8, 'LG InstaView French Door Refrigerator', 'Home Appliances', 2299.00, 'HAP-LGE-INSTA'),
(9, 'Bosch 800 Series Smart Dishwasher', 'Home Appliances', 1149.00, 'HAP-BSH-800');

-- Laptops and Monitors
INSERT INTO products (id, name, category, price, sku) VALUES
(10, 'Apple MacBook Pro 16 Inch M3 Max', 'Laptops and Monitors', 3499.00, 'LAP-APP-MBP16'),
(11, 'Dell XPS 15 OLED Touch Laptop', 'Laptops and Monitors', 1899.99, 'LAP-DEL-XPS15'),
(12, 'ASUS ROG Swift 32 Inch 4K Gaming Monitor', 'Laptops and Monitors', 899.00, 'LAP-ASU-PG32U');

-- Accessories
INSERT INTO products (id, name, category, price, sku) VALUES
(13, 'Sony WH-1000XM5 Wireless Headphones', 'Accessories', 399.99, 'ACC-SNY-XM5'),
(14, 'Apple Magic Keyboard with Touch ID', 'Accessories', 179.00, 'ACC-APP-KBID'),
(15, 'Anker 737 Power Bank (PowerCore 24K)', 'Accessories', 149.99, 'ACC-ANK-737');

-- -----------------------------------------------------------------------------
-- 3. CUSTOMERS
-- -----------------------------------------------------------------------------
INSERT INTO customers (id, name, email, phone) VALUES
(1, 'Alice Johnson', 'alice.johnson@example.com', '+1-415-555-0101'),
(2, 'Bob Smith', 'bob.smith@example.com', '+1-212-555-0102'),
(3, 'Carol Williams', 'carol.williams@example.com', '+1-310-555-0103'),
(4, 'David Brown', 'david.brown@example.com', '+1-312-555-0104'),
(5, 'Emily Davis', 'emily.davis@example.com', '+1-404-555-0105');

-- -----------------------------------------------------------------------------
-- 4. INVENTORY (Stock levels per store)
-- -----------------------------------------------------------------------------
-- Store 1 (Downtown Flagship Store)
INSERT INTO inventory (product_id, store_id, stock_level) VALUES
(1, 1, 45), (2, 1, 40), (3, 1, 35), (4, 1, 20), (5, 1, 25),
(6, 1, 30), (7, 1, 25), (8, 1, 10), (9, 1, 12), (10, 1, 22),
(11, 1, 28), (12, 1, 18), (13, 1, 60), (14, 1, 55), (15, 1, 70);

-- Store 2 (Metro Mall Outlet)
INSERT INTO inventory (product_id, store_id, stock_level) VALUES
(1, 2, 30), (2, 2, 35), (3, 2, 25), (4, 2, 15), (5, 2, 18),
(6, 2, 20), (7, 2, 15), (8, 2, 8),  (9, 2, 10), (10, 2, 15),
(11, 2, 20), (12, 2, 12), (13, 2, 45), (14, 2, 40), (15, 2, 50);

-- Store 3 (Westside Tech Hub)
INSERT INTO inventory (product_id, store_id, stock_level) VALUES
(1, 3, 50), (2, 3, 45), (3, 3, 30), (4, 3, 22), (5, 3, 20),
(6, 3, 25), (7, 3, 18), (8, 3, 12), (9, 3, 14), (10, 3, 30),
(11, 3, 35), (12, 3, 25), (13, 3, 65), (14, 3, 60), (15, 3, 80);

-- Store 4 (Uptown Electronics)
INSERT INTO inventory (product_id, store_id, stock_level) VALUES
(1, 4, 25), (2, 4, 28), (3, 4, 20), (4, 4, 14), (5, 4, 16),
(6, 4, 18), (7, 4, 12), (8, 4, 6),  (9, 4, 8),  (10, 4, 14),
(11, 4, 16), (12, 4, 10), (13, 4, 35), (14, 4, 30), (15, 4, 40);

-- Store 5 (Suburban Retail Plaza)
INSERT INTO inventory (product_id, store_id, stock_level) VALUES
(1, 5, 20), (2, 5, 22), (3, 5, 18), (4, 5, 10), (5, 5, 12),
(6, 5, 15), (7, 5, 10), (8, 5, 5),  (9, 5, 7),  (10, 5, 10),
(11, 5, 12), (12, 5, 8),  (13, 5, 30), (14, 5, 25), (15, 5, 35);

-- -----------------------------------------------------------------------------
-- 5. ORDERS IN MARCH 2025 (For Stored Procedure Analytics)
-- -----------------------------------------------------------------------------

-- Order 1: Store 1, Alice, March 5, 2025
-- 2x iPhone 15 Pro Max ($2599.98) + 1x Sony XM5 ($399.99) = $2999.97
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(1, 1, 1, '2025-03-05 14:30:00', 2999.97);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(1, 1, 2, 2, 1299.99),
(2, 1, 13, 1, 399.99);

-- Order 2: Store 2, Bob, March 10, 2025
-- 1x LG C3 55" OLED ($1399.99) + 1x Bose Soundbar 900 ($749.00) = $2148.99
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(2, 2, 2, '2025-03-10 11:15:00', 2148.99);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(3, 2, 5, 1, 1399.99),
(4, 2, 6, 1, 749.00);

-- Order 3: Store 3, Carol, March 14, 2025
-- 2x MacBook Pro 16" ($6998.00) + 2x Apple Magic Keyboard ($358.00) = $7356.00
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(3, 3, 3, '2025-03-14 16:45:00', 7356.00);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(5, 3, 10, 2, 3499.00),
(6, 3, 14, 2, 179.00);

-- Order 4: Store 4, David, March 18, 2025
-- 1x Dyson V15 ($699.99) + 2x Anker 737 Power Bank ($299.98) = $999.97
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(4, 4, 4, '2025-03-18 10:20:00', 999.97);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(7, 4, 7, 1, 699.99),
(8, 4, 15, 2, 149.99);

-- Order 5: Store 5, Emily, March 22, 2025
-- 1x Samsung Galaxy S24 Ultra ($1199.99) + 1x Sony XM5 ($399.99) = $1599.98
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(5, 5, 5, '2025-03-22 13:00:00', 1599.98);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(9, 5, 1, 1, 1199.99),
(10, 5, 13, 1, 399.99);

-- Order 6: Store 1, David, March 26, 2025
-- 1x Sony Bravia 65" ($1799.99) + 1x Dyson V15 ($699.99) = $2499.98
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(6, 4, 1, '2025-03-26 15:10:00', 2499.98);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(11, 6, 4, 1, 1799.99),
(12, 6, 7, 1, 699.99);

-- Order 7: Store 3, Alice, March 29, 2025
-- 1x Dell XPS 15 ($1899.99) + 3x iPhone 15 Pro Max ($3899.97) = $5799.96
INSERT INTO order_details (id, customer_id, store_id, order_date, total_price) VALUES
(7, 1, 3, '2025-03-29 17:30:00', 5799.96);

INSERT INTO order_items (id, order_id, product_id, quantity, price) VALUES
(13, 7, 11, 1, 1899.99),
(14, 7, 2, 3, 1299.99);
