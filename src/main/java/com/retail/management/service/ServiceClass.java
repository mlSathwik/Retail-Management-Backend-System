package com.retail.management.service;

import com.retail.management.dto.*;
import com.retail.management.entity.Inventory;
import com.retail.management.entity.Product;
import com.retail.management.entity.Store;
import com.retail.management.exception.ResourceNotFoundException;
import com.retail.management.repository.InventoryRepository;
import com.retail.management.repository.ProductRepository;
import com.retail.management.repository.StoreRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Core business service handling store, product, and inventory logic.
 */
@Service
public class ServiceClass {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ServiceClass(StoreRepository storeRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    // =========================================================================
    // STORE OPERATIONS
    // =========================================================================

    @Transactional
    public StoreDTO createStore(StoreDTO dto) {
        Store store = Store.builder()
                .name(dto.getName())
                .addressLine1(dto.getAddressLine1())
                .addressLine2(dto.getAddressLine2())
                .addressLine3(dto.getAddressLine3())
                .build();
        Store saved = storeRepository.save(store);
        return mapToStoreDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream()
                .map(this::mapToStoreDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreDTO getStoreById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + id));
        return mapToStoreDTO(store);
    }

    @Transactional(readOnly = true)
    public boolean validateStoreExists(Long id) {
        return storeRepository.existsById(id);
    }

    // =========================================================================
    // PRODUCT OPERATIONS
    // =========================================================================

    @Transactional
    public ProductDTO createProduct(ProductDTO dto) {
        productRepository.findBySku(dto.getSku()).ifPresent(p -> {
            throw new DataIntegrityViolationException("Product with SKU '" + dto.getSku() + "' already exists.");
        });

        Product product = Product.builder()
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .sku(dto.getSku())
                .build();
        Product saved = productRepository.save(product);
        return mapToProductDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToProductDTO(product);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // If SKU changed, verify no other product has it
        if (!product.getSku().equalsIgnoreCase(dto.getSku())) {
            productRepository.findBySku(dto.getSku()).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new DataIntegrityViolationException("Product with SKU '" + dto.getSku() + "' already exists.");
                }
            });
        }

        product.setName(dto.getName());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setSku(dto.getSku());

        Product updated = productRepository.save(product);
        return mapToProductDTO(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String name, String category) {
        String cleanCategory = (category != null && category.equalsIgnoreCase("All category")) ? null : category;
        return productRepository.searchProducts(name, cleanCategory).stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // INVENTORY OPERATIONS
    // =========================================================================

    @Transactional(readOnly = true)
    public List<InventoryResponse> getInventoryByStore(Long storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store not found with id: " + storeId);
        }
        return inventoryRepository.findByStoreId(storeId).stream()
                .map(this::mapToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> searchStoreInventory(Long storeId, String name, String category) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store not found with id: " + storeId);
        }
        String cleanCategory = (category != null && category.equalsIgnoreCase("All category")) ? null : category;
        return inventoryRepository.searchStoreInventory(storeId, name, cleanCategory).stream()
                .map(this::mapToInventoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponse updateStockLevel(Long storeId, Long productId, Integer stockLevel) {
        Inventory inventory = inventoryRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for store id " + storeId + " and product id " + productId));
        inventory.setStockLevel(stockLevel);
        Inventory updated = inventoryRepository.save(inventory);
        return mapToInventoryResponse(updated);
    }

    @Transactional
    public InventoryResponse addInventory(InventoryRequest request) {
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + request.getStoreId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + request.getProductId()));

        // If inventory record already exists, update its stock level or throw exception
        Inventory inventory = inventoryRepository.findByStoreIdAndProductId(request.getStoreId(), request.getProductId())
                .map(existing -> {
                    existing.setStockLevel(request.getStockLevel());
                    return existing;
                })
                .orElseGet(() -> Inventory.builder()
                        .store(store)
                        .product(product)
                        .stockLevel(request.getStockLevel())
                        .build());

        Inventory saved = inventoryRepository.save(inventory);
        return mapToInventoryResponse(saved);
    }

    @Transactional
    public void deleteInventory(Long storeId, Long productId) {
        Inventory inventory = inventoryRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory record not found for store id " + storeId + " and product id " + productId));
        inventoryRepository.delete(inventory);
    }

    // =========================================================================
    // MAPPERS
    // =========================================================================

    private StoreDTO mapToStoreDTO(Store store) {
        return StoreDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .addressLine1(store.getAddressLine1())
                .addressLine2(store.getAddressLine2())
                .addressLine3(store.getAddressLine3())
                .build();
    }

    private ProductDTO mapToProductDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .price(product.getPrice())
                .sku(product.getSku())
                .build();
    }

    private InventoryResponse mapToInventoryResponse(Inventory inv) {
        return InventoryResponse.builder()
                .id(inv.getId())
                .storeId(inv.getStore().getId())
                .storeName(inv.getStore().getName())
                .productId(inv.getProduct().getId())
                .productName(inv.getProduct().getName())
                .category(inv.getProduct().getCategory())
                .price(inv.getProduct().getPrice())
                .sku(inv.getProduct().getSku())
                .stockLevel(inv.getStockLevel())
                .build();
    }
}
