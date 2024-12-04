package com.example.order.service;

import com.example.order.dto.OrderRequestCreate;
import com.example.order.dto.ProductCreate;
import com.example.order.entity.OrderRequest;
import com.example.order.entity.Product;
import com.example.order.repository.OrderRequestRepository;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderRequestServiceTests {

    // Successfully create order with valid input and save to repository
    @Test
    public void test_create_order_success() {
        OrderRequestRepository orderRequestRepository = mock(OrderRequestRepository.class);
        WebhookService webhookService = mock(WebhookService.class);
        OrderRequestService orderRequestService = new OrderRequestService(orderRequestRepository, webhookService);

        // Arrange
        OrderRequestCreate orderRequestCreate = new OrderRequestCreate();
        orderRequestCreate.setId("123");
        orderRequestCreate.setDescription("Test Order");
        Set<ProductCreate> items = new HashSet<>();
        ProductCreate productCreate = new ProductCreate();
        productCreate.setPrice(10.0);
        productCreate.setName("Test Product");
        items.add(productCreate);
        orderRequestCreate.setItems(items.stream().toList());

        OrderRequest expectedOrder = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .products(new Product().toProductSet(items))
                .finalPrice(10.0)
                .build();

        when(orderRequestRepository.save(any(OrderRequest.class))).thenReturn(expectedOrder);
        when(webhookService.sendRequestOrder(any(OrderRequest.class))).thenReturn("SUCCESS");

        // Act
        OrderRequest result = orderRequestService.createOrder(orderRequestCreate);

        // Assert
        verify(orderRequestRepository).save(any(OrderRequest.class));
        verify(webhookService).sendRequestOrder(any(OrderRequest.class));
        assertEquals(expectedOrder.getId(), result.getId());
        assertEquals(expectedOrder.getDescription(), result.getDescription());
        assertEquals(expectedOrder.getFinalPrice(), result.getFinalPrice(), 0.1);
    }

    // Handle empty product set in order request
    @Test
    public void test_create_order_with_empty_products() {
        OrderRequestRepository orderRequestRepository = mock(OrderRequestRepository.class);
        WebhookService webhookService = mock(WebhookService.class);
        OrderRequestService orderRequestService = new OrderRequestService(orderRequestRepository, webhookService);

        // Arrange
        OrderRequestCreate orderRequestCreate = new OrderRequestCreate();
        orderRequestCreate.setId("123");
        orderRequestCreate.setDescription("Empty Order");
        orderRequestCreate.setItems(new ArrayList<>());

        OrderRequest expectedOrder = OrderRequest.builder()
                .id("123")
                .description("Empty Order")
                .products(new HashSet<>())
                .finalPrice(0.0)
                .build();

        when(orderRequestRepository.save(any(OrderRequest.class))).thenReturn(expectedOrder);
        when(webhookService.sendRequestOrder(any(OrderRequest.class))).thenReturn("SUCCESS");

        // Act
        OrderRequest result = orderRequestService.createOrder(orderRequestCreate);

        // Assert
        verify(orderRequestRepository).save(any(OrderRequest.class));
        verify(webhookService).sendRequestOrder(any(OrderRequest.class));
        assertEquals(expectedOrder.getId(), result.getId());
        assertEquals(expectedOrder.getDescription(), result.getDescription());
        assertEquals(0.0, result.getFinalPrice(), 0.1);
        assertTrue(result.getProducts().isEmpty());
    }

}
