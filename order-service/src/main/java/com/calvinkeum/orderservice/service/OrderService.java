package com.calvinkeum.orderservice.service;

import com.calvinkeum.orderservice.config.WebClientConfig;
import com.calvinkeum.orderservice.dto.*;
import com.calvinkeum.orderservice.model.Order;
import com.calvinkeum.orderservice.model.OrderLineItem;
import com.calvinkeum.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    public void placeOrder(OrderRequest orderRequst) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems =  orderRequst.getOrderLineItemsRequest()
                .stream()
                .map(orderLineItemRequest -> mapToDTO(orderLineItemRequest)).toList();

        order.setOrderLineItemList(orderLineItems);

        List<String> skus = order.getOrderLineItemList().stream().map(OrderLineItem::getSku).toList();

        // Call inventory service and place order if order is in stock
        //TODO: This does not take into account if the order quantity exceeds the inventory quantity
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("sku", skus).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        if (!allProductsInStock) {
            System.out.println("should error");
            //throw new IllegalAccessException("Product is not in stock");
        }

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
