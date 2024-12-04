package com.example.order.controller;

import com.example.order.dto.OrderRequestCreate;
import com.example.order.dto.ProductCreate;
import com.example.order.entity.OrderRequest;
import com.example.order.service.OrderRequestService;
import com.example.order.service.WebhookService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderControllerTests {
    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderRequestService orderRequestService;

    @Mock
    WebhookService webhookService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_create_order_success() {
        OrderRequestCreate request = new OrderRequestCreate();
        request.setId("123");
        request.setDescription("Test Order");
        request.setItems(new ArrayList<>());

        OrderRequest expectedOrder = new OrderRequest();
        expectedOrder.setId("123");

        when(orderRequestService.createOrder(any(OrderRequestCreate.class))).thenReturn(expectedOrder);

        ResponseEntity<OrderRequest> response = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_create_order_invalid_request() {
        OrderRequestCreate request = new OrderRequestCreate();
        request.setDescription("Test Order");
        request.setItems(new ArrayList<>());

        when(orderRequestService.createOrder(any(OrderRequestCreate.class)))
                .thenThrow(new IllegalArgumentException("Id is mandatory"));

        ResponseEntity<OrderRequest> response = orderController.createOrder(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(orderRequestService).createOrder(request);
    }

    @Test
    public void test_get_order_returns_ok() {
        ResponseEntity<String> response = orderController.getOrder();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ok", response.getBody());
    }

    @Test
    public void test_order_total_price_calculation() {
        OrderRequestCreate request = new OrderRequestCreate();
        request.setId("123");
        request.setDescription("Test Order");
        Set<ProductCreate> items = new HashSet<>();
        ProductCreate product1 = new ProductCreate();
        product1.setPrice(10.0);
        ProductCreate product2 = new ProductCreate();
        product2.setPrice(20.0);
        items.add(product1);
        items.add(product2);
        request.setItems(items.stream().toList());

        OrderRequest expectedOrder = new OrderRequest();
        expectedOrder.setId("123");
        expectedOrder.setFinalPrice(30.0);

        when(orderRequestService.createOrder(any(OrderRequestCreate.class))).thenReturn(expectedOrder);

        ResponseEntity<OrderRequest> response = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_create_order_with_empty_product_set() {
        OrderRequestCreate request = new OrderRequestCreate();
        request.setId("123");
        request.setDescription("Test Order");
        request.setItems(Collections.emptyList());

        when(orderRequestService.createOrder(any(OrderRequestCreate.class)))
                .thenThrow(new IllegalArgumentException("Product items is mandatory."));

        ResponseEntity<OrderRequest> response = orderController.createOrder(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(orderRequestService).createOrder(request);
    }
}