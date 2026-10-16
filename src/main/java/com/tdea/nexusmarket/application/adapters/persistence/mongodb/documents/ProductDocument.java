package com.tdea.nexusmarket.application.adapters.persistence.mongodb.documents;

import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.enums.ProductStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.util.Objects;

@Document(collection = "products")
public class ProductDocument {

    @Id
    private Long id;

    private Long sellerId;

    private String name;

    private String description;

    private BigDecimal price;

    private Integer stock;

    private String status;

    public ProductDocument() {
    }

    public ProductDocument(Long id, Long sellerId, String name, String description, BigDecimal price, Integer stock, String status) {
        this.id = id;
        this.sellerId = sellerId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public Product toDomain() {
        return new Product(id, sellerId, name, description, price, stock, ProductStatus.valueOf(status));
    }

    public static ProductDocument fromDomain(Product product) {
        return new ProductDocument(
            product.getId(),
            product.getSellerId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getStatus().name()
        );
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
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDocument)) return false;
        ProductDocument that = (ProductDocument) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
