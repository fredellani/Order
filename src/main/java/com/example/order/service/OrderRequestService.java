package com.example.order.service;

import com.example.order.dto.OrderRequestCreate;
import com.example.order.entity.OrderRequest;
import com.example.order.entity.Product;
import com.example.order.repository.OrderRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class OrderRequestService {
    private final OrderRequestRepository orderRequestRepository;
    private final WebhookService webhookService;

    @Autowired
    public OrderRequestService(OrderRequestRepository orderRequestRepository, WebhookService webhookService) {
        this.orderRequestRepository = orderRequestRepository;
        this.webhookService = webhookService;
    }

    public OrderRequest createOrder(OrderRequestCreate orderRequestCreate) {
        OrderRequest order = OrderRequest.builder()
                .id(orderRequestCreate.getId())
                .description(orderRequestCreate.getDescription())
                .products(new Product().toProductSet(new HashSet<>(orderRequestCreate.getItems())))
                .build();
        order.calculateTotalProductValue();
        orderRequestRepository.save(order);
        webhookService.sendRequestOrder(order);

        return order;
    }
}
