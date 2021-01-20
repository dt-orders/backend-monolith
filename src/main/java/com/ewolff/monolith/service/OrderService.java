package com.ewolff.monolith.service;

import com.ewolff.monolith.persistence.domain.Order;

public interface OrderService {
    Order order(Order order);
    double getPrice(long orderId);
}
