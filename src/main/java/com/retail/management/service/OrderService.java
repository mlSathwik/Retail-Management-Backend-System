package com.retail.management.service;

import com.retail.management.dto.*;
import com.retail.management.entity.*;
import com.retail.management.exception.InsufficientStockException;
import com.retail.management.exception.ResourceNotFoundException;
import com.retail.management.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service managing order placement and order validations.
 */
@Service
public class OrderService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderDetailsRepository orderDetailsRepository,
                        CustomerRepository customerRepository,
                        StoreRepository storeRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Validates whether a store with the given ID exists.
     *
     * @param storeId store identifier
     * @return true if store exists
     */
    @Transactional(readOnly = true)
    public boolean validateStore(Long storeId) {
        return storeRepository.existsById(storeId);
    }

    /**
     * Places a customer order.
     * Performs customer retrieval/creation, inventory availability check,
     * stock decrement, total computation, and atomic order persistence.
     *
     * @param request PlaceOrderRequest payload
     * @return OrderResponse with full order details
     */
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // 1. Validate Store
        Store store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with id: " + request.getStoreId()));

        // 2. Find or create Customer by email
        CustomerDTO customerDTO = request.getCustomer();
        Customer customer = customerRepository.findByEmail(customerDTO.getEmail())
                .map(existingCustomer -> {
                    if (customerDTO.getName() != null && !customerDTO.getName().isBlank()) {
                        existingCustomer.setName(customerDTO.getName());
                    }
                    if (customerDTO.getPhone() != null && !customerDTO.getPhone().isBlank()) {
                        existingCustomer.setPhone(customerDTO.getPhone());
                    }
                    return customerRepository.save(existingCustomer);
                })
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .name(customerDTO.getName())
                            .email(customerDTO.getEmail())
                            .phone(customerDTO.getPhone())
                            .build();
                    return customerRepository.save(newCustomer);
                });

        // 3. Process line items, validate stock, and decrement inventory
        BigDecimal grandTotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        OrderDetails orderDetails = OrderDetails.builder()
                .customer(customer)
                .store(store)
                .orderDate(LocalDateTime.now())
                .totalPrice(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemReq.getProductId()));

            Inventory inventory = inventoryRepository.findByStoreIdAndProductId(store.getId(), product.getId())
                    .orElseThrow(() -> new InsufficientStockException(
                            "Product '" + product.getName() + "' (ID: " + product.getId() + ") is not stocked in store '" + store.getName() + "'"));

            if (inventory.getStockLevel() < itemReq.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product '" + product.getName() + "'. Available: " +
                        inventory.getStockLevel() + ", Requested: " + itemReq.getQuantity());
            }

            // Deduct stock
            inventory.setStockLevel(inventory.getStockLevel() - itemReq.getQuantity());
            inventoryRepository.save(inventory);

            // Compute line item subtotal
            BigDecimal itemPrice = product.getPrice();
            BigDecimal subtotal = itemPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            grandTotal = grandTotal.add(subtotal);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .price(itemPrice)
                    .build();

            orderDetails.addItem(orderItem);
        }

        orderDetails.setTotalPrice(grandTotal);
        OrderDetails savedOrder = orderDetailsRepository.save(orderDetails);

        return mapToOrderResponse(savedOrder);
    }

    private OrderResponse mapToOrderResponse(OrderDetails order) {
        Customer cust = order.getCustomer();
        CustomerDTO custDTO = CustomerDTO.builder()
                .id(cust.getId())
                .name(cust.getName())
                .email(cust.getEmail())
                .phone(cust.getPhone())
                .build();

        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .sku(item.getProduct().getSku())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .subtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customer(custDTO)
                .storeId(order.getStore().getId())
                .storeName(order.getStore().getName())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .items(itemResponses)
                .build();
    }
}
