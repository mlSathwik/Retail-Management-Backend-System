package com.retail.management.controller;

import com.retail.management.dto.StoreDTO;
import com.retail.management.service.ServiceClass;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final ServiceClass serviceClass;

    public StoreController(ServiceClass serviceClass) {
        this.serviceClass = serviceClass;
    }

    @PostMapping
    public ResponseEntity<StoreDTO> addStore(@Valid @RequestBody StoreDTO storeDTO) {
        StoreDTO created = serviceClass.createStore(storeDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(serviceClass.getAllStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceClass.getStoreById(id));
    }
}
