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
public class TopProductResponse {

    private String category;
    private String name;
    private Long totalQuantitySold;
    private BigDecimal totalSales;
}
