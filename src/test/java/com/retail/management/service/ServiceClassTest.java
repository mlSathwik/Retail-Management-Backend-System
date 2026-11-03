package com.retail.management.service;

import com.retail.management.dto.ProductDTO;
import com.retail.management.dto.StoreDTO;
import com.retail.management.entity.Product;
import com.retail.management.entity.Store;
import com.retail.management.exception.ResourceNotFoundException;
import com.retail.management.repository.InventoryRepository;
import com.retail.management.repository.ProductRepository;
import com.retail.management.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceClassTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ServiceClass serviceClass;

    private Store sampleStore;
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleStore = Store.builder()
                .id(1L)
                .name("Downtown Store")
                .addressLine1("100 Main St")
                .addressLine2("Suite 1")
                .addressLine3("City A")
                .build();

        sampleProduct = Product.builder()
                .id(1L)
                .name("Galaxy S24")
                .category("Mobile")
                .price(new BigDecimal("999.99"))
                .sku("MOB-S24")
                .build();
    }

    @Test
    @DisplayName("createStore saves and returns StoreDTO")
    void testCreateStore() {
        StoreDTO requestDto = StoreDTO.builder()
                .name("Downtown Store")
                .addressLine1("100 Main St")
                .build();

        when(storeRepository.save(any(Store.class))).thenReturn(sampleStore);

        StoreDTO created = serviceClass.createStore(requestDto);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Downtown Store", created.getName());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    @DisplayName("getStoreById throws ResourceNotFoundException when not found")
    void testGetStoreByIdNotFound() {
        when(storeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> serviceClass.getStoreById(99L));
    }

    @Test
    @DisplayName("createProduct saves and returns ProductDTO")
    void testCreateProduct() {
        ProductDTO requestDto = ProductDTO.builder()
                .name("Galaxy S24")
                .category("Mobile")
                .price(new BigDecimal("999.99"))
                .sku("MOB-S24")
                .build();

        when(productRepository.findBySku("MOB-S24")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductDTO created = serviceClass.createProduct(requestDto);

        assertNotNull(created);
        assertEquals("Galaxy S24", created.getName());
        assertEquals("MOB-S24", created.getSku());
    }

    @Test
    @DisplayName("searchProducts queries repository with category filter")
    void testSearchProducts() {
        when(productRepository.searchProducts("Galaxy", "Mobile")).thenReturn(List.of(sampleProduct));

        List<ProductDTO> results = serviceClass.searchProducts("Galaxy", "Mobile");

        assertEquals(1, results.size());
        assertEquals("Galaxy S24", results.get(0).getName());
    }
}
