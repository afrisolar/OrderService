package com.afrisol.OrderService.service;




import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.mapper.OrderMapper;
import com.afrisol.OrderService.model.CustomerOrder;
import com.afrisol.OrderService.model.OrderStatus;
import com.afrisol.OrderService.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import java.time.LocalDateTime;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;


    @InjectMocks
    private OrderServiceImpl orderService;


    private OrderMapper orderMapper = OrderMapper.INSTANCE;
    OrderResponse orderResponse1;
    OrderResponse orderResponse;
    OrderRequest orderRequest;
    OrderRequest orderRequest1;
    CustomerOrder mockCustomerOrder;


    private CustomerOrder order1;
    private CustomerOrder order2;


    private CustomerOrder existingOrder;
    private CustomerOrder updatedOrder;


    @BeforeEach
    public void setUp() {
        System.out.println("Executing @BeforeEach setup...");
        MockitoAnnotations.openMocks(this);
        orderRequest = OrderRequest.builder()
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .quantity(2)
                .build();
        orderResponse = OrderResponse.builder()
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .orderNumber("ORD-ABR-0001")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        orderRequest1 = OrderRequest.builder()
                .amount(10.00)
                .customerId("customer2")
                .productId("product2")
                .quantity(2)
                .build();


        orderResponse1 = OrderResponse.builder()
                .amount(10.00)
                .customerId("customer2")
                .productId("product2")
                .orderNumber("ORD-ABR-0022")
                .quantity(1)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        mockCustomerOrder = CustomerOrder.builder()
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .orderNumber("ORD-ABR-0001")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .id(1L)
                .version(1L)
                .build();


        order1 = CustomerOrder.builder()
                .id(1L)
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .orderNumber("ORD-1234")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();


        order2 = CustomerOrder.builder()
                .id(2L)
                .amount(50.00)
                .customerId("customer2")
                .productId("product2")
                .orderNumber("ORD-5678")
                .quantity(1)
                .status(OrderStatus.DELIVERED)
                .createdAt(LocalDateTime.now())
                .build();
        existingOrder = CustomerOrder.builder()
                .id(1L)
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .orderNumber("ORD-1234")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(null)
                .build();


        updatedOrder = CustomerOrder.builder()
                .id(1L)
                .amount(150.00)
                .customerId("customer2")
                .productId("product2")
                .orderNumber("ORD-1234")
                .quantity(3)
                .status(OrderStatus.PENDING)
                .createdAt(existingOrder.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();
    }




    @Test
    public void testAddOrder_Success() {
        // Arrange
        String requestId = "req123";
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(Mono.just(mockCustomerOrder));


        // Act
        Mono<OrderResponse> result = orderService.addOrder(requestId, orderRequest);


        // Assert
        StepVerifier.create(result)
                .assertNext(orderResponse -> {
                    assertNotNull(orderResponse);
                    assertEquals("ORD-ABR-0001", orderResponse.getOrderNumber());
                })
                .verifyComplete();


        verify(orderRepository, times(1)).save(any(CustomerOrder.class));
    }
    @Test
    public void testGetOrder_Success() {
        // Arrange
        String orderNumber = "ORD-ABR-0001";
        String requestId = "req123";
        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.just(mockCustomerOrder));


        // Act
        Mono<OrderResponse> result = orderService.getOrder(requestId, orderNumber);


        // Assert
        StepVerifier.create(result)
                .assertNext(orderResponse -> {
                    assertNotNull(orderResponse);
                    assertEquals(orderResponse.getOrderNumber(), orderResponse.getOrderNumber());
                    assertEquals(orderResponse.getAmount(), orderResponse.getAmount());
                })
                .verifyComplete();


        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
    }
    @Test
    public void testGetOrder_NotFound() {
        // Arrange
        String nonExistentOrderNumber = "ORD-9999";
        String requestId = "req123";
        when(orderRepository.findByOrderNumber(nonExistentOrderNumber)).thenReturn(Mono.empty());


        // Act
        Mono<OrderResponse> result = orderService.getOrder(requestId, nonExistentOrderNumber);


        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND &&
                        throwable.getMessage().contains("CustomerOrder not found"))
                .verify();


        verify(orderRepository, times(1)).findByOrderNumber(nonExistentOrderNumber);
    }
    @Test
    public void testGetAllOrders_Success() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Flux.fromIterable(Arrays.asList(order1, order2)));


        // Act
        Flux<OrderResponse> result = orderService.getAllOrders("req123");


        // Assert
        StepVerifier.create(result)
                .assertNext(orderResponse -> {
                    assertNotNull(orderResponse);
                    assertEquals(order1.getOrderNumber(), orderResponse.getOrderNumber());
                    assertEquals(order1.getAmount(), orderResponse.getAmount());
                })
                .assertNext(orderResponse -> {
                    assertNotNull(orderResponse);
                    assertEquals(order2.getOrderNumber(), orderResponse.getOrderNumber());
                    assertEquals(order2.getAmount(), orderResponse.getAmount());
                })
                .verifyComplete();


        verify(orderRepository, times(1)).findAll();
    }
    @Test
    public void testUpdateOrder_Success() {
        // Arrange
        String orderNumber = "ORD-1234";
        String requestId = "req123";


        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.just(existingOrder));
        when(orderRepository.save(any(CustomerOrder.class))).thenReturn(Mono.just(updatedOrder));


        // Act
        Mono<OrderResponse> result = orderService.updateOrder(orderRequest, orderNumber, requestId);


        // Assert
        StepVerifier.create(result)
                .assertNext(orderResponse -> {
                    assertNotNull(orderResponse);
                    assertEquals(updatedOrder.getOrderNumber(), orderResponse.getOrderNumber());
                    assertEquals(updatedOrder.getCustomerId(), orderResponse.getCustomerId());
                    assertEquals(updatedOrder.getProductId(), orderResponse.getProductId());
                    assertEquals(updatedOrder.getAmount(), orderResponse.getAmount());
                    assertEquals(updatedOrder.getQuantity(), orderResponse.getQuantity());
                })
                .verifyComplete();


        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
        verify(orderRepository, times(1)).save(any(CustomerOrder.class));
    }


    @Test
    public void testUpdateOrder_NotFound() {
        // Arrange
        String orderNumber = "ORD-9999";
        String requestId = "req123";


        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.empty());


        // Act
        Mono<OrderResponse> result = orderService.updateOrder(orderRequest, orderNumber, requestId);


        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND &&
                        throwable.getMessage().contains("CustomerOrder not found"))
                .verify();


        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
        verify(orderRepository, never()).save(any(CustomerOrder.class));
    }


    @Test
    public void testDeleteOrder_Success() {
        // Arrange
        String orderNumber = "ORD-1234";
        String requestId = "req123";


        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.just(existingOrder));
        when(orderRepository.delete(existingOrder)).thenReturn(Mono.empty());


        // Act
        Mono<Void> result = orderService.deleteOrder(orderNumber, requestId);


        // Assert
        StepVerifier.create(result)
                .verifyComplete();


        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
        verify(orderRepository, times(1)).delete(existingOrder);
    }


    @Test
    public void testDeleteOrder_NotFound() {
        // Arrange
        String orderNumber = "ORD-9999";
        String requestId = "req123";


        when(orderRepository.findByOrderNumber(orderNumber)).thenReturn(Mono.empty());


        // Act
        Mono<Void> result = orderService.deleteOrder(orderNumber, requestId);


        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException &&
                        ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.NOT_FOUND &&
                        throwable.getMessage().contains("CustomerOrder not found"))
                .verify();


        verify(orderRepository, times(1)).findByOrderNumber(orderNumber);
        verify(orderRepository, never()).delete(any(CustomerOrder.class));
    }


    public OrderMapper getOrderMapper() {
        return orderMapper;
    }


    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }
}

