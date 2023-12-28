package com.calvinkeum.orderservice.controller;

import com.calvinkeum.orderservice.dto.OrderResponse;
import com.calvinkeum.orderservice.service.OrderService;
import com.calvinkeum.orderservice.dto.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderResponse> getAllOrders() {

        //TODO: Add pagination + Search Criteria

        return orderService.getAllOrders();
    }
}
