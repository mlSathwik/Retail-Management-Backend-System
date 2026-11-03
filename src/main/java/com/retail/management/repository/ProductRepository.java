package com.retail.management.repository;

import com.retail.management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR :category = 'All category' OR LOWER(p.category) = LOWER(:category))")
    List<Product> searchProducts(@Param("name") String name, @Param("category") String category);
}
