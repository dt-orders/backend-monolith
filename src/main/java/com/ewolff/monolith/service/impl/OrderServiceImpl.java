package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.OrderDTO;
import com.ewolff.monolith.dto.OrderLineDTO;
import com.ewolff.monolith.persistence.domain.Order;
import com.ewolff.monolith.persistence.repository.OrderRepository;
import com.ewolff.monolith.service.CatalogService;
import com.ewolff.monolith.service.CustomerService;
import com.ewolff.monolith.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

	private OrderRepository orderRepository;
	private CustomerService customerService;
	private CatalogService catalogService;
	private final ModelMapper mapper;


	public OrderServiceImpl(OrderRepository orderRepository,
							 CustomerService customerService, CatalogService catalogService, ModelMapper mapper) {
		super();
		this.orderRepository = orderRepository;
		this.customerService = customerService;
		this.catalogService = catalogService;
		this.mapper = mapper;
	}

	@Override
	public Order order(Order order) {
		log.info("Calling OrderService.order() for order: {}", order);
		if (order.getNumberOfLines() == 0) {
			throw new IllegalArgumentException("No order lines!");
		}
		if (!customerService.isValidCustomerId(order.getCustomerId())) {
			throw new IllegalArgumentException("Customer does not exist! Customer id = " + order.getCustomerId());
		}
		return orderRepository.save(order);
	}

	@Override
	public double getPrice(long id) {
		log.info("Calling OrderService.getPrice() for id: {}", id);
		return orderRepository.findById(id).get().totalPrice(catalogService);
	}

	@Override
	public Collection<OrderDTO> findAll() {
		log.info("Calling OrderService.findAll()");
		Collection<OrderDTO> orders = StreamSupport.stream(orderRepository.findAll().spliterator(), false)
				.map(order -> {
					OrderDTO dto = mapper.map(order, OrderDTO.class);
					dto.setTotalPrice(order.totalPrice(catalogService));
					dto.setOrderLine(updateOrderLineDTOs(dto.getOrderLine()));
					CustomerDTO customer = customerService.getOne(order.getCustomerId());
					dto.setDisplayName(customer.getFirstname() + " " + customer.getName());
					return dto;
				})
				.collect(Collectors.toList());
		log.info("OrderDTOs: {}", orders);
		return orders;
	}

	private List<OrderLineDTO> updateOrderLineDTOs(List<OrderLineDTO> orderLines) {
		return orderLines.stream()
				.map(orderLine -> {
							OrderLineDTO newOrderLine = new OrderLineDTO(orderLine.getId(), orderLine.getItemId(), null, orderLine.getCount());
							newOrderLine.setItemName(catalogService.getOne(orderLine.getItemId()).getName());
							return newOrderLine;
				})
				.collect(Collectors.toList());
	}

	@Override
	public Order getOne(Long id) {
		log.info("Calling OrderService.getOne() for id: {}", id);
		Order order = null;
		Optional<Order> orderOpt = orderRepository.findById(id);
		if (orderOpt.isPresent()) {
			order = orderOpt.get();
		}
		return order;
	}

	@Override
	public void delete(Long id) {
		log.info("Calling OrderService.delete() for id: {}", id);
		if (orderRepository.findById(id) != null) {
			orderRepository.deleteById(id);
		}

	}

}
