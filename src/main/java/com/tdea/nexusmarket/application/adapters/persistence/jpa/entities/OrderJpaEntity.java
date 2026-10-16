package com.tdea.nexusmarket.application.adapters.persistence.jpa.entities;

import com.tdea.nexusmarket.application.domain.models.CartItem;
import com.tdea.nexusmarket.application.domain.models.Order;
import com.tdea.nexusmarket.application.domain.enums.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class OrderJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Transient
    private List<CartItem> items;

    public OrderJpaEntity() {
    }

    public OrderJpaEntity(Long id, Long buyerId, BigDecimal totalAmount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order toDomain() {
        List<CartItem> orderItems = (items != null) ? items : new ArrayList<>();
        Order order = new Order(id, buyerId, orderItems);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.valueOf(status));
        order.setCreatedAt(createdAt);
        return order;
    }

    public static OrderJpaEntity fromDomain(Order order) {
        return new OrderJpaEntity(
            order.getId(),
            order.getBuyerId(),
            order.getTotalAmount(),
            order.getStatus().name(),
            order.getCreatedAt()
        );
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderJpaEntity)) return false;
        OrderJpaEntity that = (OrderJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
