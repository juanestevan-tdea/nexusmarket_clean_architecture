package com.tdea.nexusmarket.application.domain.ports.in;

public interface AdminPort {

    void disableUser(Long userId);

    void forceCancelOrder(Long orderId, String reason);
}
