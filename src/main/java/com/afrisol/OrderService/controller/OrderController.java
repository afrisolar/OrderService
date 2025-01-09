package com.afrisol.OrderService.controller;


import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.UUID;


@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping
    public Mono<ResponseEntity<OrderResponse>> createOrder(@RequestBody @Valid OrderRequest orderRequestDto) {
        String requestId = UUID.randomUUID().toString();
        log.info("Creating order with customerID: {}", orderRequestDto.getCustomerId());
        Mono<OrderResponse> orderResponse = orderService.addOrder(requestId, orderRequestDto);
        return orderResponse.map(ResponseEntity::ok);
    }


    @GetMapping("/{orderNumber}")
    public Mono<ResponseEntity<OrderResponse>> getOrder(@PathVariable String orderNumber) {
        String requestId = UUID.randomUUID().toString();


        log.info("Getting order with orderId  : {} and requestID: {}", orderNumber, requestId);
        return orderService.getOrder(requestId, orderNumber)
                .map(ResponseEntity::ok)
                .onErrorResume(ResponseStatusException.class, ex -> {
                    log.error("Error: {}", ex.getReason());
                    return Mono.just(ResponseEntity.status(ex.getStatusCode()).body(null));
                })
                .onErrorResume(RuntimeException.class, ex ->
                {
                    log.error("Unhandled RuntimeException: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
                });
    }


    @GetMapping
    public Flux<OrderResponse> getAllOrders() {
        String requestId = UUID.randomUUID().toString();
        log.info("Getting orders with requestID: {}", requestId);
        return orderService.getAllOrders(requestId);
    }


    @PutMapping("/{orderNumber}")
    public Mono<ResponseEntity<OrderResponse>> updateOrder(@RequestBody @Valid OrderRequest orderRequestDto, @PathVariable String orderNumber) {
        String requestId = UUID.randomUUID().toString();
        log.info("Updating order with ID: {} and requestID {}", orderNumber, requestId);
        return orderService.updateOrder(orderRequestDto, orderNumber, requestId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{orderNumber}")
    public Mono<ResponseEntity<Object>> deleteOrder(@PathVariable String orderNumber) {
        String requestID = UUID.randomUUID().toString();
        log.info("Deleting order with ID: {} and requestID {}", orderNumber, requestID);
        return orderService.deleteOrder(orderNumber, requestID)
                .then(Mono.just(ResponseEntity.noContent().<Object>build())) // Success: 204 No Content
                .onErrorResume(e -> {
                    log.error("Error deleting order with ID: {} - {}", orderNumber, e.getMessage(), e);
                    return Mono.just(ResponseEntity.status(404).<Object>build()); // Error: 404 Not Found
                });
    }
}
