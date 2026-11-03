package com.retail.management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Store ID is required")
    private Long storeId;

    @NotNull(message = "Stock level is required")
    @Min(value = 0, message = "Stock level must be greater than or equal to 0")
    private Integer stockLevel;
}
