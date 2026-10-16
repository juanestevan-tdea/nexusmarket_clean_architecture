package com.tdea.nexusmarket.application.domain.services;

import com.tdea.nexusmarket.application.domain.models.CartItem;
import com.tdea.nexusmarket.application.domain.models.Order;
import com.tdea.nexusmarket.application.domain.models.Product;
import com.tdea.nexusmarket.application.domain.ports.in.BuyerCheckoutPort;
import com.tdea.nexusmarket.application.domain.ports.out.OrderRepositoryPort;
import com.tdea.nexusmarket.application.domain.ports.out.ProductRepositoryPort;
import java.util.List;

public class OrderCheckoutService implements BuyerCheckoutPort {

    private final OrderRepositoryPort orderRepository;
    private final ProductRepositoryPort productRepository;

    public OrderCheckoutService(OrderRepositoryPort orderRepository, ProductRepositoryPort productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order createOrder(Long buyerId, List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La orden no puede tener una lista de items vacia");
        }

        Order order = new Order(null, buyerId, items);
        order = orderRepository.save(order);

        // Reducir stock de cada producto involucrado
        for (CartItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Producto no encontrado con id: " + item.getProductId()));

            Integer newStock = product.getStock() - item.getQuantity();
            if (newStock < 0) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el producto: " + product.getId());
            }

            product.setStock(newStock);
            productRepository.save(product);
        }

        return order;
    }

    @Override
    public Order payOrder(Long orderId, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Orden no encontrada con id: " + orderId));

        if (order.getStatus() != com.tdea.nexusmarket.application.domain.enums.OrderStatus.PENDING) {
            throw new IllegalStateException(
                    "Solo se pueden pagar ordenes en estado PENDING");
        }

        order.setStatus(com.tdea.nexusmarket.application.domain.enums.OrderStatus.PAID);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderHistory(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
}
