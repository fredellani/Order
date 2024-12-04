package com.example.order.service;

import com.example.order.entity.OrderRequest;
import com.example.order.utils.WebhookConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate;
    private final WebhookConfig webhookConfig;

    @Autowired
    public WebhookService(RestTemplate restTemplate, WebhookConfig webhookConfig) {
        this.restTemplate = restTemplate;
        this.webhookConfig = webhookConfig;
    }

    public String sendRequestOrder(OrderRequest orderRequest) {
        return restTemplate.postForObject(webhookConfig.getAddress(), orderRequest, String.class);
    }
}
