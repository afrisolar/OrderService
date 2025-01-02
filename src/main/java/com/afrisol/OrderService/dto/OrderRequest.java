package com.afrisol.OrderService.dto;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private String customerId;
    private Double amount;
    private String productId;
    private Integer quantity;

    public OrderRequest(String customer1, String product1, int quanity, double amount) {
        this.customerId = customer1;
        this.amount = amount;
        this.productId = product1;
        this.quantity =  quanity;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

