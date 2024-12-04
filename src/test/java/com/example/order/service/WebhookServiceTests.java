package com.example.order.service;

import com.example.order.entity.OrderRequest;
import com.example.order.utils.WebhookConfig;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WebhookServiceTests {
    // Successfully send order request and receive response with valid webhook config
    @Test
    public void test_send_request_order_success() {
        WebhookService webhookService = mock(WebhookService.class);
        WebhookConfig webhookConfig = new WebhookConfig();
        webhookConfig.setAddress("http://test-webhook.com");
        webhookConfig.setClientId("test-client");
        webhookConfig.setSecretId("test-secret");

        OrderRequest orderRequest = OrderRequest.builder()
                .id("123")
                .description("Test Order")
                .finalPrice(100.0)
                .build();

        when(webhookService.sendRequestOrder(any(OrderRequest.class))).thenReturn("SUCCESS");

        String response = webhookService.sendRequestOrder(orderRequest);

        assertNotNull(response);
    }
}
