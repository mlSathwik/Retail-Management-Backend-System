package com.retail.management.controller;

import com.retail.management.dto.InventoryRequest;
import com.retail.management.dto.InventoryResponse;
import com.retail.management.dto.InventoryUpdateRequest;
import com.retail.management.service.ServiceClass;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final ServiceClass serviceClass;

    public InventoryController(ServiceClass serviceClass) {
        this.serviceClass = serviceClass;
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<InventoryResponse>> getInventoryByStore(@PathVariable Long storeId) {
        return ResponseEntity.ok(serviceClass.getInventoryByStore(storeId));
    }

    @GetMapping("/store/{storeId}/search")
    public ResponseEntity<List<InventoryResponse>> searchStoreInventory(
            @PathVariable Long storeId,
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(serviceClass.searchStoreInventory(storeId, name, category));
    }

    @PutMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<InventoryResponse> updateStockLevel(
            @PathVariable Long storeId,
            @PathVariable Long productId,
            @Valid @RequestBody InventoryUpdateRequest request) {
        return ResponseEntity.ok(serviceClass.updateStockLevel(storeId, productId, request.getStockLevel()));
    }

    @PostMapping
    public ResponseEntity<InventoryResponse> addInventory(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = serviceClass.addInventory(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/store/{storeId}/product/{productId}")
    public ResponseEntity<Void> deleteInventory(
            @PathVariable Long storeId,
            @PathVariable Long productId) {
        serviceClass.deleteInventory(storeId, productId);
        return ResponseEntity.noContent().build();
    }
}
