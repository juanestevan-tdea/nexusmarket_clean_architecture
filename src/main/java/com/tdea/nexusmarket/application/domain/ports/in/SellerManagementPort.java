package com.tdea.nexusmarket.application.domain.ports.in;

import com.tdea.nexusmarket.application.domain.models.Product;

public interface SellerManagementPort {

    Product createProduct(Long sellerId, Product product);

    void updateProductStock(Long productId, Integer newStock, Long sellerId);

    void deactivateProduct(Long productId, Long sellerId);
}
