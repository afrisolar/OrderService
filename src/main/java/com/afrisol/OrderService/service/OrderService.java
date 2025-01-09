package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.dto.PaymentResponseDto;
import com.afrisol.OrderService.model.CustomerOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderResponse> addOrder(String requestID, OrderRequest orderRequestDto);
    Mono<OrderResponse> getOrder(String requestID, String orderNumber);
    Flux<OrderResponse> getAllOrders(String requestID);
    Mono<OrderResponse> updateOrder(OrderRequest orderRequestDto, String orderNumber, String requestID);
    Mono<Void> deleteOrder(String orderNumber, String requestID);
    Mono<CustomerOrder> processOrder(PaymentResponseDto paymentResponse);
}
