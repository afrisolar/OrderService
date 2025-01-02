package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.mapper.OrderMapper;
import com.afrisol.OrderService.repostiory.OrderRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private static final Logger log = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Mono<OrderResponse> addOrder(String requestId, OrderRequest orderRequest) {
        return orderRepository.save(OrderMapper.INSTANCE.toOrder(orderRequest))
                .doOnNext(savedCustomerOrder -> {
                    log.info("CustomerOrder created successfully: {}", savedCustomerOrder); // Log the saved CustomerOrder
                    log.info("CustomerOrder ID: {}", savedCustomerOrder.getId());
                })
                .doOnError(error -> log.error("Error creating order: {}", error.getMessage()))
                .map(OrderMapper.INSTANCE::toOrderResponse)
                .doOnNext(orderResponse -> log.info("Mapped OrderResponse: {}", orderResponse));
    }

    @Override
    public Mono<OrderResponse> getOrder(String requestId, String orderNumber) {
        // Validate input parameters
        if (orderNumber == null || orderNumber.isBlank()) {
            log.error("Invalid orderNumber provided: requestId={}", requestId);
            return Mono.error(new IllegalArgumentException("Order number cannot be null or empty"));
        }
        log.info("Fetching order with orderNumber: {} and requestId: {}", orderNumber, requestId);
        return orderRepository.findByOrderNumber(orderNumber)
                .map(OrderMapper.INSTANCE::toOrderResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomerOrder not found")))
                .doOnError(error -> log.error("Error fetching order: {}", error.getMessage()));
    }

    @Override
    public Flux<OrderResponse> getAllOrders(String requestId) {
        log.info("Fetching all orders with requestID: {}", requestId);
        return orderRepository.findAll()
                .map(OrderMapper.INSTANCE::toOrderResponse)
                .doOnComplete(() -> log.info("All orders fetched successfully"))
                .doOnError(error -> log.error("Error fetching all orders: {}", error.getMessage()));
    }

    @Override
    public Mono<OrderResponse> updateOrder(OrderRequest orderRequest, String orderNumber, String requestId) {
        log.info("Updating order with ID: {} and requestID: {}", orderNumber, requestId);
        return orderRepository.findByOrderNumber(orderNumber)
                .flatMap(existingOrder -> {
                    existingOrder.setCustomerId(orderRequest.getCustomerId());
                    existingOrder.setProductId(orderRequest.getProductId());
                    existingOrder.setQuantity(orderRequest.getQuantity());
                    existingOrder.setAmount(orderRequest.getAmount());
                    existingOrder.setUpdatedAt(LocalDateTime.now());
                    return orderRepository.save(existingOrder);
                })
                .map(OrderMapper.INSTANCE::toOrderResponse)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomerOrder not found")))
                .doOnSuccess(updatedOrder -> log.info("CustomerOrder updated successfully with OrderNumber: {}", updatedOrder.getOrderNumber()))
                .doOnError(error -> log.error("Error updating order: {}", error.getMessage()));
    }

    @Override
    public Mono<Void> deleteOrder(String orderNumber, String requestId) {
        log.info("Deleting order with ID: {} and requestID: {}", orderNumber, requestId);
        return orderRepository.findByOrderNumber(orderNumber)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomerOrder not found")))
                .flatMap(orderRepository::delete)
                .doOnSuccess(unused -> log.info("CustomerOrder deleted successfully with OrderNumber: {}", orderNumber))
                .doOnError(error -> log.error("Error deleting order: {}", error.getMessage()));
    }
}
