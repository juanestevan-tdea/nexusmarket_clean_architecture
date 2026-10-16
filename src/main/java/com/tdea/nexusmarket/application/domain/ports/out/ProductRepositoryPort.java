package com.tdea.nexusmarket.application.domain.ports.out;

import com.tdea.nexusmarket.application.domain.models.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Optional<Product> findById(Long id);

    List<Product> findAllActive();

    Product save(Product product);
}
