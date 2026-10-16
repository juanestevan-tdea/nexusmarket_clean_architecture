package com.tdea.nexusmarket.application.domain.services;

import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.ports.in.PublicCatalogPort;
import com.tdea.nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import java.util.List;

public class ProductCatalogService implements PublicCatalogPort {

    private final ProductRepositoryPort productRepository;

    public ProductCatalogService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getActiveProducts() {
        return productRepository.findAllActive();
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Producto no encontrado con id: " + productId));
    }
}
