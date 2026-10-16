package com.tdea.nexusmarket.application.adapters.rest.controllers;

import com.tdea.nexusmarket.application.domain.models.CartItem;
import com.tdea.nexusmarket.application.domain.models.Order;
import com.tdea.nexusmarket.application.domain.ports.in.BuyerCheckoutPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutRestController {

    private final BuyerCheckoutPort buyerCheckoutPort;

    public CheckoutRestController(BuyerCheckoutPort buyerCheckoutPort) {
        this.buyerCheckoutPort = buyerCheckoutPort;
    }

    public record CreateOrderRequest(Long buyerId, List<CartItemRequest> items) {}

    public record CartItemRequest(Long productId, Integer quantity, BigDecimal unitPrice) {}

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
        List<CartItem> items = request.items().stream()
            .map(item -> new CartItem(item.productId(), item.quantity(), item.unitPrice()))
            .toList();
        Order order = buyerCheckoutPort.createOrder(request.buyerId(), items);
        return ResponseEntity.ok(order);
    }

    public record PayOrderRequest(String paymentMethod) {}

    @PostMapping("/orders/{orderId}/pay")
    public ResponseEntity<Order> payOrder(@PathVariable Long orderId, @RequestBody PayOrderRequest request) {
        Order order = buyerCheckoutPort.payOrder(orderId, request.paymentMethod());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/{buyerId}")
    public ResponseEntity<List<Order>> getOrderHistory(@PathVariable Long buyerId) {
        List<Order> orders = buyerCheckoutPort.getOrderHistory(buyerId);
        return ResponseEntity.ok(orders);
    }
}
