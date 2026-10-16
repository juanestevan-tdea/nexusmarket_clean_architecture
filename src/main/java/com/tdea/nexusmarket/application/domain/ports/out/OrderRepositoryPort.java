package com.tdea.nexusmarket.application.domain.ports.out;

import com.tdea.nexusmarket.application.domain.models.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Optional<Order> findById(Long id);

    List<Order> findByBuyerId(Long buyerId);

    Order save(Order order);
}
