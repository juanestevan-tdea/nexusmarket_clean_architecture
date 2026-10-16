package com.tdea.nexusmarket.application.domain.ports.in;

import com.tdea.nexusmarket.application.domain.models.CartItem;
import com.tdea.nexusmarket.application.domain.models.Order;
import java.util.List;

public interface BuyerCheckoutPort {

    Order createOrder(Long buyerId, List<CartItem> items);

    Order payOrder(Long orderId, String paymentMethod);

    List<Order> getOrderHistory(Long buyerId);
}
