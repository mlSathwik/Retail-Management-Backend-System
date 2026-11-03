package com.retail.management.repository;

import com.retail.management.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    List<Inventory> findByStoreId(Long storeId);

    Optional<Inventory> findByStoreIdAndProductId(Long storeId, Long productId);

    void deleteByStoreIdAndProductId(Long storeId, Long productId);

    @Query("SELECT i FROM Inventory i WHERE i.store.id = :storeId AND " +
           "(:name IS NULL OR :name = '' OR LOWER(i.product.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR :category = '' OR :category = 'All category' OR LOWER(i.product.category) = LOWER(:category))")
    List<Inventory> searchStoreInventory(@Param("storeId") Long storeId,
                                         @Param("name") String name,
                                         @Param("category") String category);
}
