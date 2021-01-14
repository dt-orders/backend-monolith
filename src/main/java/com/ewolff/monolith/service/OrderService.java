package com.ewolff.monolith.service;

import com.ewolff.monolith.persistence.domain.Order;
import com.ewolff.monolith.persistence.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.order.clients.CustomerClient;

@Service
public class OrderService {

	private OrderRepository orderRepository;
	private CustomerClient customerClient;
	private CatalogClient itemClient;

	@Autowired
	private OrderService(OrderRepository orderRepository,
			CustomerClient customerClient, CatalogClient itemClient) {
		super();
		this.orderRepository = orderRepository;
		this.customerClient = customerClient;
		this.itemClient = itemClient;
	}

	public Order order(Order order) {
		if (order.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		if (!customerClient.isValidCustomerId(order.getCustomerId())) {
			throw new IllegalArgumentException("Customer does not exist! Customer id = " + order.getCustomerId());
		}
		return orderRepository.save(order);
	}

	public double getPrice(long orderId) {
		return orderRepository.findById(orderId).get().totalPrice(itemClient);
	}

}
