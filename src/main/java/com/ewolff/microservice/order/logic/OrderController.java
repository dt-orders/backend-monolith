package com.ewolff.microservice.order.logic;

import java.util.Collection;
import java.util.Random;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewolff.microservice.order.clients.CatalogClient;
import com.ewolff.microservice.order.clients.Customer;
import com.ewolff.microservice.order.clients.CustomerClient;
import com.ewolff.microservice.order.clients.Item;

import java.util.Calendar;
import java.util.Date; 

import java.io.*;

@Controller
@RequestMapping("order")
class OrderController {

	private OrderRepository orderRepository;

	private OrderService orderService;

	private CustomerClient customerClient;
	private CatalogClient catalogClient;

	@Autowired
	private OrderController(OrderService orderService,
			OrderRepository orderRepository, CustomerClient customerClient,
			CatalogClient catalogClient) {
		super();
		this.orderRepository = orderRepository;
		this.customerClient = customerClient;
		this.catalogClient = catalogClient;
		this.orderService = orderService;
	}

	@ModelAttribute("items")
	public Collection<Item> items() {
		return catalogClient.findAll();
	}

	@ModelAttribute("customers")
	public Collection<Customer> customers() {

		System.out.println("APP_VERSION: " + System.getenv("APP_VERSION"));
		if (System.getenv("APP_VERSION").equals("2")) {
			System.out.println("N+1 problem = ON");
			Collection<Customer> allCustomers = customerClient.findAll();
			// ************************************************
			// N+1 Problem
			// Add additional lookups for each customer
			// this will cause additional SQL calls
			// ************************************************
			Iterator<Customer> itr = allCustomers.iterator();
			while (itr.hasNext()) {
				Customer cust = itr.next();
				long id = cust.getCustomerId();
				for(int i=1; i<=20; i++){
					customerClient.getOne(id);
				}
			}
			return allCustomers;
		}
		else {
			System.out.println("N+1 problem = OFF");
			return customerClient.findAll();
		}
	}

	@RequestMapping("/")
	public ModelAndView orderHome() {
		return new ModelAndView("orderlist", "orders",
				orderRepository.findAll());
	}

	@RequestMapping("/list.html")
	public ModelAndView orderList() {
		return new ModelAndView("orderlist", "orders",
				orderRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		return new ModelAndView("orderForm", "order", new Order());
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(Order order) {
		order.addLine(0, catalogClient.findAll().iterator().next().getItemId());
		return new ModelAndView("orderForm", "order", order);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") long id) {
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(Order order) {
		order = orderService.order(order);
		return new ModelAndView("orderSuccess");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView post(@PathVariable("id") long id) {
		orderRepository.deleteById(id);

		return new ModelAndView("orderSuccess");
	}

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ResponseBody
	public String getVersion() {
		 String version;
		 try {
			 version = System.getenv("APP_VERSION");
		 }
		 catch(Exception e) {
			 version = "APP_VERSION not found";
		 }
		 return version;
	}

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public String getHealth() {

		Date dateNow = Calendar.getInstance().getTime();
		String health = "{ \"health\":[{\"service\":\"order-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
		return health;
	}
}
