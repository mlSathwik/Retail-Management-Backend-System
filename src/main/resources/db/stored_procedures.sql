-- =============================================================================
-- Retail Management System - Stored Procedures (MySQL)
-- =============================================================================

DROP PROCEDURE IF EXISTS GetMonthlySalesForEachStore;
DROP PROCEDURE IF EXISTS GetAggregateSalesForCompany;
DROP PROCEDURE IF EXISTS GetTopSellingProductsByCategory;

-- -----------------------------------------------------------------------------
-- 1. GetMonthlySalesForEachStore
-- Parameters: IN p_year INT, IN p_month INT
-- Columns: store_id, total_sales, sale_month, sale_year
-- -----------------------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE GetMonthlySalesForEachStore(
    IN p_year INT,
    IN p_month INT
)
BEGIN
    SELECT 
        s.id AS store_id,
        COALESCE(SUM(od.total_price), 0.00) AS total_sales,
        p_month AS sale_month,
        p_year AS sale_year
    FROM stores s
    LEFT JOIN order_details od 
        ON s.id = od.store_id 
        AND YEAR(od.order_date) = p_year 
        AND MONTH(od.order_date) = p_month
    GROUP BY s.id
    ORDER BY s.id ASC;
END //

DELIMITER ;

-- -----------------------------------------------------------------------------
-- 2. GetAggregateSalesForCompany
-- Parameters: IN p_year INT, IN p_month INT
-- Columns: total_sales, sale_month, sale_year
-- -----------------------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE GetAggregateSalesForCompany(
    IN p_year INT,
    IN p_month INT
)
BEGIN
    SELECT 
        COALESCE(SUM(total_price), 0.00) AS total_sales,
        p_month AS sale_month,
        p_year AS sale_year
    FROM order_details
    WHERE YEAR(order_date) = p_year 
      AND MONTH(order_date) = p_month;
END //

DELIMITER ;

-- -----------------------------------------------------------------------------
-- 3. GetTopSellingProductsByCategory
-- Parameters: IN p_month INT, IN p_year INT (Note: month first, then year)
-- Columns: category, name, total_quantity_sold, total_sales
-- -----------------------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE GetTopSellingProductsByCategory(
    IN p_month INT,
    IN p_year INT
)
BEGIN
    WITH AggregatedSales AS (
        SELECT 
            p.category AS category,
            p.name AS name,
            SUM(oi.quantity) AS total_quantity_sold,
            SUM(oi.quantity * oi.price) AS total_sales,
            ROW_NUMBER() OVER (
                PARTITION BY p.category 
                ORDER BY SUM(oi.quantity) DESC, SUM(oi.quantity * oi.price) DESC
            ) AS rank_in_category
        FROM order_items oi
        JOIN order_details od ON oi.order_id = od.id
        JOIN products p ON oi.product_id = p.id
        WHERE YEAR(od.order_date) = p_year 
          AND MONTH(od.order_date) = p_month
        GROUP BY p.category, p.id, p.name
    )
    SELECT 
        category,
        name,
        total_quantity_sold,
        total_sales
    FROM AggregatedSales
    WHERE rank_in_category = 1
    ORDER BY category ASC;
END //

DELIMITER ;
