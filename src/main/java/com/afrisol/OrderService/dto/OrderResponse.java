package com.afrisol.OrderService.dto;

import com.afrisol.OrderService.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderNumber;
    private String customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String productId;
    private Integer quantity;
    private Double amount;
    private OrderStatus status;
}
