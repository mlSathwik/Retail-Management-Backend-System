package com.retail.management.service;

import com.retail.management.dto.CustomerDTO;
import com.retail.management.dto.OrderItemRequest;
import com.retail.management.dto.OrderResponse;
import com.retail.management.dto.PlaceOrderRequest;
import com.retail.management.entity.Customer;
import com.retail.management.entity.Inventory;
import com.retail.management.entity.OrderDetails;
import com.retail.management.entity.Product;
import com.retail.management.entity.Store;
import com.retail.management.exception.InsufficientStockException;
import com.retail.management.repository.*;
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
class OrderServiceTest {

    @Mock
    private OrderDetailsRepository orderDetailsRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderService orderService;

    private Store sampleStore;
    private Product sampleProduct;
    private Customer sampleCustomer;
    private Inventory sampleInventory;

    @BeforeEach
    void setUp() {
        sampleStore = Store.builder()
                .id(1L)
                .name("Flagship Store")
                .addressLine1("100 Market St")
                .build();

        sampleProduct = Product.builder()
                .id(10L)
                .name("MacBook Pro")
                .category("Laptops")
                .price(new BigDecimal("2000.00"))
                .sku("LAP-MBP")
                .build();

        sampleCustomer = Customer.builder()
                .id(5L)
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        sampleInventory = Inventory.builder()
                .id(100L)
                .store(sampleStore)
                .product(sampleProduct)
                .stockLevel(10)
                .build();
    }

    @Test
    @DisplayName("placeOrder successfully deducts inventory and saves order")
    void testPlaceOrderSuccess() {
        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .storeId(1L)
                .customer(CustomerDTO.builder().name("John Doe").email("john@example.com").phone("1234567890").build())
                .items(List.of(OrderItemRequest.builder().productId(10L).quantity(2).build()))
                .build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(sampleStore));
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(inventoryRepository.findByStoreIdAndProductId(1L, 10L)).thenReturn(Optional.of(sampleInventory));
        when(orderDetailsRepository.save(any(OrderDetails.class))).thenAnswer(invocation -> {
            OrderDetails order = invocation.getArgument(0);
            order.setId(500L);
            return order;
        });

        OrderResponse response = orderService.placeOrder(request);

        assertNotNull(response);
        assertEquals(500L, response.getId());
        assertEquals(new BigDecimal("4000.00"), response.getTotalPrice());
        assertEquals(8, sampleInventory.getStockLevel()); // 10 - 2 = 8
        verify(inventoryRepository, times(1)).save(sampleInventory);
        verify(orderDetailsRepository, times(1)).save(any(OrderDetails.class));
    }

    @Test
    @DisplayName("placeOrder throws InsufficientStockException when stock is less than requested quantity")
    void testPlaceOrderInsufficientStock() {
        sampleInventory.setStockLevel(1);

        PlaceOrderRequest request = PlaceOrderRequest.builder()
                .storeId(1L)
                .customer(CustomerDTO.builder().name("John Doe").email("john@example.com").phone("1234567890").build())
                .items(List.of(OrderItemRequest.builder().productId(10L).quantity(5).build()))
                .build();

        when(storeRepository.findById(1L)).thenReturn(Optional.of(sampleStore));
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleCustomer));
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productRepository.findById(10L)).thenReturn(Optional.of(sampleProduct));
        when(inventoryRepository.findByStoreIdAndProductId(1L, 10L)).thenReturn(Optional.of(sampleInventory));

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(request));
        verify(orderDetailsRepository, never()).save(any(OrderDetails.class));
    }
}
