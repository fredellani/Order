package com.example.order.controller;

import com.example.order.dto.OrderRequestCreate;
import com.example.order.entity.OrderRequest;
import com.example.order.service.OrderRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderRequestService orderRequestService;

    @Autowired
    public OrderController(OrderRequestService orderRequestService) {
        this.orderRequestService = orderRequestService;
    }

    @PostMapping()
    public ResponseEntity<OrderRequest> createOrder(@Valid @RequestBody OrderRequestCreate orderRequestCreate) {
        try {
            return new ResponseEntity<>(orderRequestService.createOrder(orderRequestCreate), HttpStatus.CREATED);
        } catch (Exception e) {
            var trace = e.getStackTrace();
            for (StackTraceElement elem : trace) {
                System.err.println(elem);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/")
    public ResponseEntity<String> getOrder() {
        return ResponseEntity.ok("Ok");
    }
}
