package com.tdea.nexusmarket.application.domain.models;

import com.tdea.nexusmarket.application.domain.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long id;
    private Long buyerId;
    private List<CartItem> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order() {
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Order(Long id, Long buyerId, List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Una orden no puede instanciarse si la lista de items esta vacia");
        }
        this.id = id;
        this.buyerId = buyerId;
        this.items = items;
        this.totalAmount = calculateTotal();
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
        this.totalAmount = calculateTotal();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
