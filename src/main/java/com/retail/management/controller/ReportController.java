package com.retail.management.controller;

import com.retail.management.dto.CompanySalesResponse;
import com.retail.management.dto.MonthlySalesResponse;
import com.retail.management.dto.TopProductResponse;
import com.retail.management.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/monthly-sales")
    public ResponseEntity<List<MonthlySalesResponse>> getMonthlySales(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getMonthlySalesForEachStore(year, month));
    }

    @GetMapping("/company-sales")
    public ResponseEntity<CompanySalesResponse> getCompanySales(
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(reportService.getAggregateSalesForCompany(year, month));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductResponse>> getTopProducts(
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(reportService.getTopSellingProductsByCategory(month, year));
    }
}
