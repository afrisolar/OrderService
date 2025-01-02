package com.afrisol.OrderService.model;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public @NotNull OrderStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull OrderStatus status) {
        this.status = status;
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
}
