package com.afrisol.OrderService.model;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer_order")
@Data
public class CustomerOrder {
    @Id
    private Long id;

    @Version
    private Long version;

    @NotNull
    private String orderNumber;

    @NotNull(message = "CustomerID can not be null")
    private String customerId;

    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotNull(message = "amount can not be null")
    private BigDecimal amount;

    @NotNull(message = "ProductID can not be null")
    private String productId;

    @Setter
    private Integer quantity;


}

