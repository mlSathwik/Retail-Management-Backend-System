package com.retail.management.controller;

import com.retail.management.dto.OrderResponse;
import com.retail.management.dto.PlaceOrderRequest;
import com.retail.management.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/validate/{storeId}")
    public ResponseEntity<Map<String, Object>> validateStore(@PathVariable Long storeId) {
        boolean valid = orderService.validateStore(storeId);
        return ResponseEntity.ok(Map.of(
                "storeId", storeId,
                "valid", valid
        ));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        OrderResponse orderResponse = orderService.placeOrder(request);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
