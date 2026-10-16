package com.tdea.nexusmarket.application.domain.models;

import com.tdea.nexusmarket.application.domain.enums.ProductStatus;
import java.math.BigDecimal;

public class Product {

    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private ProductStatus status;

    public Product() {
        this.status = ProductStatus.ACTIVE;
    }

    public Product(Long id, Long sellerId, String name, String description, BigDecimal price, Integer stock, ProductStatus status) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser estrictamente mayor a 0");
        }
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser estrictamente mayor a 0");
        }
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stock = stock;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
