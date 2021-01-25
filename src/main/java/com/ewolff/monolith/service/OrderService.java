package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.OrderDTO;
import com.ewolff.monolith.persistence.domain.Order;

import java.util.Collection;

public interface OrderService {
    Order order(Order order);

    OrderDTO save(OrderDTO order);

    double getPrice(long orderId);

    Collection<OrderDTO> findAll();

    Order getOne(Long orderId);

    OrderDTO getOneDTO(Long id);

    void delete(Long id);

}
