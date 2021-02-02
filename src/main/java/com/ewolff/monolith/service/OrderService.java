package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.OrderDTO;
import com.ewolff.monolith.persistence.domain.Order;

import java.util.Collection;

public interface OrderService {

    OrderDTO save(OrderDTO order);

    double getPrice(long orderId);

    Collection<OrderDTO> findAll();

    OrderDTO getOne(Long id);

    void delete(Long id);

}
