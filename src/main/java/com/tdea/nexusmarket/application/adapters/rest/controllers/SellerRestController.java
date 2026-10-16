package com.tdea.nexusmarket.application.adapters.rest.controllers;

import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.enums.ProductStatus;
import com.tdea.nexusmarket.application.domain.ports.in.SellerManagementPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/seller")
public class SellerRestController {

    private final SellerManagementPort sellerManagementPort;

    public SellerRestController(SellerManagementPort sellerManagementPort) {
        this.sellerManagementPort = sellerManagementPort;
    }

    public record CreateProductRequest(Long sellerId, String name, String description, BigDecimal price, Integer stock) {}

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = new Product(
            null,
            request.sellerId(),
            request.name(),
            request.description(),
            request.price(),
            request.stock(),
            ProductStatus.ACTIVE
        );
        Product created = sellerManagementPort.createProduct(request.sellerId(), product);
        return ResponseEntity.ok(created);
    }

    public record UpdateStockRequest(Long sellerId, Integer newStock) {}

    @PutMapping("/products/{productId}/stock")
    public ResponseEntity<Void> updateProductStock(@PathVariable Long productId, @RequestBody UpdateStockRequest request) {
        sellerManagementPort.updateProductStock(productId, request.newStock(), request.sellerId());
        return ResponseEntity.ok().build();
    }

    public record DeactivateProductRequest(Long sellerId) {}

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long productId, @RequestBody DeactivateProductRequest request) {
        sellerManagementPort.deactivateProduct(productId, request.sellerId());
        return ResponseEntity.noContent().build();
    }
}
