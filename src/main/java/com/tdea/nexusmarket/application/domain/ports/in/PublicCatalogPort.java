package com.tdea.nexusmarket.application.domain.ports.in;

import com.tdea.nexusmarket.application.domain.models.Product;
import java.util.List;

public interface PublicCatalogPort {

    List<Product> getActiveProducts();

    Product getProductById(Long productId);
}
