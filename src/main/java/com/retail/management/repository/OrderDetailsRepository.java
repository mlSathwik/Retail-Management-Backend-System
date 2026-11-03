package com.retail.management.repository;

import com.retail.management.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findByStoreId(Long storeId);

    List<OrderDetails> findByCustomerId(Long customerId);
}
