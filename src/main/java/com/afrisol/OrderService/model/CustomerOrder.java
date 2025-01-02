package com.afrisol.OrderService.model;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customer_order")
public class CustomerOrder {
    @Id
    private Long id;


    @Version
    private Long version;


    @NotNull
    private String orderNumber;


    @NotNull
    private String customerId;


    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @NotNull
    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    @NotNull
    private Double amount;


    @NotNull
    private String productId;

    private Integer quantity;

    public @NotNull String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(@NotNull String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public @NotNull String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(@NotNull String customerId) {
        this.customerId = customerId;
    }

    public @NotNull LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public @NotNull OrderStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public @NotNull Double getAmount() {
        return amount;
    }

    public void setAmount(@NotNull Double amount) {
        this.amount = amount;
    }

    public @NotNull String getProductId() {
        return productId;
    }

    public void setProductId(@NotNull String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }
}

