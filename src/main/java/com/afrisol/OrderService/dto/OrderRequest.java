package com.afrisol.OrderService.dto;


import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class OrderRequest {
    private String customerId;
    private BigDecimal amount;
    private String productId;
    private Integer quantity;
}


