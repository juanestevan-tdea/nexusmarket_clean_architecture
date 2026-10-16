package com.tdea.nexusmarket.application.adapters.rest.controllers;

import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.ports.in.PublicCatalogPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogRestController {

    private final PublicCatalogPort publicCatalogPort;

    public CatalogRestController(PublicCatalogPort publicCatalogPort) {
        this.publicCatalogPort = publicCatalogPort;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getActiveProducts() {
        List<Product> products = publicCatalogPort.getActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = publicCatalogPort.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
