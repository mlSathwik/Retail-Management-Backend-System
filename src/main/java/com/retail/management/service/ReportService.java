package com.retail.management.service;

import com.retail.management.dto.CompanySalesResponse;
import com.retail.management.dto.MonthlySalesResponse;
import com.retail.management.dto.TopProductResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service invoking MySQL stored procedures for sales analytics and reports.
 */
@Service
public class ReportService {

    private final JdbcTemplate jdbcTemplate;

    public ReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Calls stored procedure GetMonthlySalesForEachStore(IN p_year INT, IN p_month INT)
     * Columns returned: store_id, total_sales, sale_month, sale_year
     */
    @Transactional(readOnly = true)
    public List<MonthlySalesResponse> getMonthlySalesForEachStore(int year, int month) {
        String sql = "CALL GetMonthlySalesForEachStore(?, ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> MonthlySalesResponse.builder()
                .storeId(rs.getLong("store_id"))
                .totalSales(rs.getBigDecimal("total_sales"))
                .saleMonth(rs.getInt("sale_month"))
                .saleYear(rs.getInt("sale_year"))
                .build(),
                year, month);
    }

    /**
     * Calls stored procedure GetAggregateSalesForCompany(IN p_year INT, IN p_month INT)
     * Columns returned: total_sales, sale_month, sale_year
     */
    @Transactional(readOnly = true)
    public CompanySalesResponse getAggregateSalesForCompany(int year, int month) {
        String sql = "CALL GetAggregateSalesForCompany(?, ?)";
        List<CompanySalesResponse> list = jdbcTemplate.query(sql, (rs, rowNum) -> CompanySalesResponse.builder()
                .totalSales(rs.getBigDecimal("total_sales"))
                .saleMonth(rs.getInt("sale_month"))
                .saleYear(rs.getInt("sale_year"))
                .build(),
                year, month);

        if (list.isEmpty()) {
            return CompanySalesResponse.builder()
                    .totalSales(java.math.BigDecimal.ZERO)
                    .saleMonth(month)
                    .saleYear(year)
                    .build();
        }
        return list.get(0);
    }

    /**
     * Calls stored procedure GetTopSellingProductsByCategory(IN p_month INT, IN p_year INT)
     * Parameter order: month first, then year.
     * Columns returned: category, name, total_quantity_sold, total_sales
     */
    @Transactional(readOnly = true)
    public List<TopProductResponse> getTopSellingProductsByCategory(int month, int year) {
        String sql = "CALL GetTopSellingProductsByCategory(?, ?)";
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopProductResponse.builder()
                .category(rs.getString("category"))
                .name(rs.getString("name"))
                .totalQuantitySold(rs.getLong("total_quantity_sold"))
                .totalSales(rs.getBigDecimal("total_sales"))
                .build(),
                month, year);
    }
}
