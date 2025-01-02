package com.afrisol.OrderService.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class OrderRequest {
    private String customerId;
    private Double amount;
    private String productId;
    private Integer quantity;
}


