package com.calvinkeum.orderservice.service;

import com.calvinkeum.orderservice.dto.OrderLineItemRequest;
import com.calvinkeum.orderservice.dto.OrderLineItemResponse;
import com.calvinkeum.orderservice.dto.OrderRequest;
import com.calvinkeum.orderservice.dto.OrderResponse;
import com.calvinkeum.orderservice.model.Order;
import com.calvinkeum.orderservice.model.OrderLineItem;
import com.calvinkeum.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    public void placeOrder(OrderRequest orderRequst) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems =  orderRequst.getOrderLineItemsRequest()
                .stream()
                .map(orderLineItemRequest -> mapToDTO(orderLineItemRequest)).toList();

        order.setOrderLineItemList(orderLineItems);

        orderRepository.save(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> mapToDTO(order))
                .toList();
    }

    private OrderResponse mapToDTO(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderNumber(order.getOrderNumber());

        List<OrderLineItemResponse> orderLineItemResponses = new ArrayList<>();

        order.getOrderLineItemList()
                .forEach(orderLineItem -> {
                    OrderLineItemResponse orderLineItemResponse = new OrderLineItemResponse();

                    orderLineItemResponse.setSku(orderLineItem.getSku());
                    orderLineItemResponse.setPrice(orderLineItem.getPrice());
                    orderLineItemResponse.setQuantity(orderLineItem.getQuantity());

                    orderLineItemResponses.add(orderLineItemResponse);
                });

        orderResponse.setOrderLineItemsResponse(orderLineItemResponses);

        return orderResponse;
    }
    private OrderLineItem mapToDTO(OrderLineItemRequest orderLineItemsRequest) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsRequest.getPrice());
        orderLineItem.setQuantity(orderLineItemsRequest.getQuantity());
        orderLineItem.setSku(orderLineItemsRequest.getSku());

        return orderLineItem;
    }
}
