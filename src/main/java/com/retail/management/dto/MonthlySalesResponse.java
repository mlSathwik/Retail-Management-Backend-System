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
public class MonthlySalesResponse {

    private Long storeId;
    private BigDecimal totalSales;
    private Integer saleMonth;
    private Integer saleYear;
}
