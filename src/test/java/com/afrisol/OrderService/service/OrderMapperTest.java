package com.afrisol.OrderService.service;

import com.afrisol.OrderService.dto.OrderRequest;
import com.afrisol.OrderService.dto.OrderResponse;
import com.afrisol.OrderService.mapper.OrderMapper;
import com.afrisol.OrderService.model.CustomerOrder;
import com.afrisol.OrderService.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class OrderMapperTest {
    private final OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void testToOrderResponse() {
        // Arrange
        CustomerOrder customerOrder = CustomerOrder.builder()
                .id(1L)
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .orderNumber("ORD-1234")
                .quantity(2)
                .status(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // Act
        OrderResponse orderResponse = orderMapper.toOrderResponse(customerOrder);

        // Assert
        assertNotNull(orderResponse);
        assertEquals(customerOrder.getOrderNumber(), orderResponse.getOrderNumber());
        assertEquals(customerOrder.getAmount(), orderResponse.getAmount());
        assertEquals(customerOrder.getCustomerId(), orderResponse.getCustomerId());
        assertEquals(customerOrder.getProductId(), orderResponse.getProductId());
        assertEquals(customerOrder.getQuantity(), orderResponse.getQuantity());
        assertEquals(customerOrder.getStatus(), orderResponse.getStatus());
    }

    @Test
    public void testToOrder() {
        OrderRequest orderRequest = OrderRequest.builder()
                .amount(100.00)
                .customerId("customer1")
                .productId("product1")
                .quantity(2)
                .build();

        CustomerOrder customerOrder = orderMapper.toOrder(orderRequest);

        assertNotNull(customerOrder);
        assertEquals(orderRequest.getAmount(), customerOrder.getAmount());
        assertEquals(orderRequest.getCustomerId(), customerOrder.getCustomerId());
        assertEquals(orderRequest.getProductId(), customerOrder.getProductId());
        assertEquals(orderRequest.getQuantity(), customerOrder.getQuantity());

        assertNotNull(customerOrder.getCreatedAt());
        assertEquals(OrderStatus.NEW, customerOrder.getStatus());
        assertTrue(customerOrder.getOrderNumber().startsWith("ORD-"));
        assertEquals(8, customerOrder.getOrderNumber().length()); // Verify order number length
    }
}
