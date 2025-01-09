package com.afrisol.OrderService.controller;


import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.model.OrderStatus;
import com.afrisol.OrderService.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


public class OrderControllerTest {
    @Mock
    private OrderService orderService;


    @InjectMocks
    private OrderController orderController;


    private OrderRequest validOrderRequest;
    private OrderResponse orderResponse;
    private OrderResponse order1;
    private OrderResponse order2;
    private OrderResponse updatedOrderResponse;
    private OrderRequest orderRequest;




    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        validOrderRequest = OrderRequest.builder()
                .amount(BigDecimal.valueOf(10))
                .customerId("customer123")
                .productId("product456")
                .quantity(3)
                .build();


        orderResponse = OrderResponse.builder()
                .orderNumber("ORD-1234")
                .amount(BigDecimal.valueOf(10))
                .customerId("customer123")
                .productId("product456")
                .quantity(3)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();




        updatedOrderResponse = OrderResponse.builder()
                .orderNumber("ORD-1234")
                .amount(BigDecimal.valueOf(10))
                .customerId("customer456")
                .productId("product789")
                .quantity(5)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();


        order1 = OrderResponse.builder()
                .orderNumber("ORD-1234")
                .amount(BigDecimal.valueOf(10))
                .customerId("customer1")
                .productId("product1")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();


        order2 = OrderResponse.builder()
                .orderNumber("ORD-5678")
                .amount(BigDecimal.valueOf(10))
                .customerId("customer2")
                .productId("product2")
                .quantity(4)
                .status(OrderStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build();
        orderRequest = OrderRequest.builder()
                .amount(BigDecimal.valueOf(10))
                .customerId("customer456")
                .productId("product789")
                .quantity(5)
                .build();
    }


    @Test
    public void testCreateOrder_Success() {
        // Arrange
        when(orderService.addOrder(anyString(), eq(validOrderRequest))).thenReturn(Mono.just(orderResponse));


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.createOrder(validOrderRequest);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(orderResponse, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).addOrder(anyString(), eq(validOrderRequest));
    }


    @Test
    public void testCreateOrder_Error() {
        // Arrange
        when(orderService.addOrder(anyString(), eq(validOrderRequest)))
                .thenReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request")));


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.createOrder(validOrderRequest);


        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.BAD_REQUEST &&
                        throwable.getMessage().contains("Invalid request"))
                .verify();


        verify(orderService, times(1)).addOrder(anyString(), eq(validOrderRequest));
    }


    @Test
    public void testGetOrder_Success() {
        // Arrange
        String orderNumber = "ORD-1234";
        when(orderService.getOrder(anyString(), eq(orderNumber))).thenReturn(Mono.just(orderResponse));


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.getOrder(orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(orderResponse, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).getOrder(anyString(), eq(orderNumber));
    }


    @Test
    public void testGetOrder_NotFound() {
        // Arrange
        String orderNumber = "ORD-9999";
        when(orderService.getOrder(anyString(), eq(orderNumber)))
                .thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found")));


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.getOrder(orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    assertEquals(null, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).getOrder(anyString(), eq(orderNumber));
    }


    @Test
    public void testGetAllOrders_Success() {
        // Arrange
        when(orderService.getAllOrders(anyString())).thenReturn(Flux.fromIterable(Arrays.asList(order1, order2)));


        // Act
        Flux<OrderResponse> result = orderController.getAllOrders();


        // Assert
        StepVerifier.create(result)
                .assertNext(order -> {
                    assertEquals(order1.getOrderNumber(), order.getOrderNumber());
                    assertEquals(order1.getAmount(), order.getAmount());
                })
                .assertNext(order -> {
                    assertEquals(order2.getOrderNumber(), order.getOrderNumber());
                    assertEquals(order2.getAmount(), order.getAmount());
                })
                .verifyComplete();


        verify(orderService, times(1)).getAllOrders(anyString());
    }
    @Test
    public void testGetAllOrders_Empty() {
        // Arrange
        when(orderService.getAllOrders(anyString())).thenReturn(Flux.empty());


        // Act
        Flux<OrderResponse> result = orderController.getAllOrders();


        // Assert
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();


        verify(orderService, times(1)).getAllOrders(anyString());
    }


    @Test
    public void testUpdateOrder_Success() {
        // Arrange
        String orderNumber = "ORD-1234";
        when(orderService.updateOrder(eq(orderRequest), eq(orderNumber), anyString())).thenReturn(Mono.just(updatedOrderResponse));


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.updateOrder(orderRequest, orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
                    assertEquals(updatedOrderResponse, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).updateOrder(eq(orderRequest), eq(orderNumber), anyString());
    }


    @Test
    public void testUpdateOrder_NotFound() {
        // Arrange
        String orderNumber = "ORD-9999";
        when(orderService.updateOrder(eq(orderRequest), eq(orderNumber), anyString())).thenReturn(Mono.empty());


        // Act
        Mono<ResponseEntity<OrderResponse>> result = orderController.updateOrder(orderRequest, orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    assertEquals(null, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).updateOrder(eq(orderRequest), eq(orderNumber), anyString());
    }


    @Test
    public void testDeleteOrder_Success() {
        // Arrange
        String orderNumber = "ORD-1234";
        when(orderService.deleteOrder(eq(orderNumber), anyString())).thenReturn(Mono.empty());


        // Act
        Mono<ResponseEntity<Object>> result = orderController.deleteOrder(orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
                    assertEquals(null, responseEntity.getBody());
                })
                .verifyComplete();


        verify(orderService, times(1)).deleteOrder(eq(orderNumber), anyString());
    }


    @Test
    public void testDeleteOrder_NotFound() {
        // Arrange
        String orderNumber = "ORD-9999";
        when(orderService.deleteOrder(eq(orderNumber), anyString()))
                .thenReturn(Mono.error(new RuntimeException("Order not found")));

        // Act
        Mono<ResponseEntity<Object>> result = orderController.deleteOrder(orderNumber);

        // Assert
        StepVerifier.create(result)
                .assertNext(responseEntity -> {
                    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
                    assertEquals(null, responseEntity.getBody());
                })
                .verifyComplete();

        verify(orderService, times(1)).deleteOrder(eq(orderNumber), anyString());
    }
}

