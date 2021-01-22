package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.persistence.domain.Order;
import com.ewolff.monolith.persistence.repository.OrderRepository;
import com.ewolff.monolith.service.CatalogService;
import com.ewolff.monolith.service.CustomerService;
import com.ewolff.monolith.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private CustomerService customerService;
	private CatalogService catalogService;

	public OrderServiceImpl(OrderRepository orderRepository,
							 CustomerService customerService, CatalogService catalogService) {
		super();
		this.orderRepository = orderRepository;
		this.customerService = customerService;
		this.catalogService = catalogService;
	}

	@Override
	public Order order(Order order) {
		if (order.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		if (!customerService.isValidCustomerId(order.getCustomerId())) {
			throw new IllegalArgumentException("Customer does not exist! Customer id = " + order.getCustomerId());
		}
		return orderRepository.save(order);
	}

	@Override
	public double getPrice(long orderId) {
		return orderRepository.findById(orderId).get().totalPrice(catalogService);
	}

	@Override
	public Collection<Order> findAll() {
		return null;
	}

}
