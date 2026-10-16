package com.tdea.nexusmarket.application.domain.models;

import java.math.BigDecimal;

public class CartItem {

    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;

    public CartItem() {
    }

    public CartItem(Long productId, Integer quantity, BigDecimal unitPrice) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser estrictamente mayor a 0");
        }
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser estrictamente mayor a 0");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser estrictamente mayor a 0");
        }
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser estrictamente mayor a 0");
        }
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return quantity.multiply(unitPrice);
    }
}
