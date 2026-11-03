package com.retail.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private Long id;
    private Long storeId;
    private String storeName;
    private Long productId;
    private String productName;
    private String category;
    private BigDecimal price;
    private String sku;
    private Integer stockLevel;
}
