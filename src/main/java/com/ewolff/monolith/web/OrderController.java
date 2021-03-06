package com.ewolff.monolith.web;

import java.util.*;

import com.ewolff.monolith.dto.OrderDTO;
import com.ewolff.monolith.dto.OrderLineDTO;
import com.ewolff.monolith.service.CatalogService;
import com.ewolff.monolith.service.CustomerService;
import com.ewolff.monolith.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;

@Slf4j
@Controller
@RequestMapping("order")
class OrderController {

	private OrderService orderService;
	private CustomerService customerService;
	private CatalogService catalogService;
	private BackendController backendController;

	@Autowired
	private OrderController(OrderService orderService,
							CustomerService customerService,
							CatalogService catalogService, 
							BackendController backendController) {
		super();
		this.customerService = customerService;
		this.catalogService = catalogService;
		this.orderService = orderService;
		this.backendController = backendController;
	}

	@ModelAttribute("items")
	public Collection<ItemDTO> items() {
		log.info("Calling OrderController.items()");
		return catalogService.findAll();
	}

	@ModelAttribute("customers")
	public Collection<CustomerDTO> customers() throws InterruptedException, Exception {
		log.info("Calling OrderController.customers()");
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}

		if (backendController.getVersion().equals("2")) {
			log.info("N+1 problem = ON");
			Collection<CustomerDTO> allCustomers = customerService.findAll();
			// ************************************************
			// N+1 Problem
			// Add additional lookups for each customer
			// this will cause additional SQL calls
			// ************************************************
			Iterator<CustomerDTO> itr = allCustomers.iterator();
			while (itr.hasNext()) {
				CustomerDTO cust = itr.next();
				long id = cust.getCustomerId();
				for(int i=1; i<=20; i++){
					customerService.getOne(id);
				}
			}
			return allCustomers;
		}
		else {
			log.info("N+1 problem = OFF");
			return customerService.findAll();
		}
	}

	@RequestMapping("/")
	public ModelAndView orderHome() throws InterruptedException, Exception {
		log.info("Calling OrderController.orderHome()");
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("orderlist", "orders",
				orderService.findAll());
	}

	@RequestMapping("/list.html")
	public ModelAndView orderList() throws InterruptedException, Exception {
		log.info("Calling OrderController.orderList()");
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("orderlist", "orders",
				orderService.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() throws InterruptedException, Exception {
		log.info("Calling OrderController.form()");
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("orderForm", "order", new OrderDTO());
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(OrderDTO order) throws InterruptedException, Exception {
		log.info("Calling OrderController.addLine() with order: {}", order);
		ItemDTO item = catalogService.findAll().iterator().next();
		if (order.getOrderLine() == null) {
			order.setOrderLine(new ArrayList<OrderLineDTO>());
		}
		order.getOrderLine().add(new OrderLineDTO(0L, item.getItemId(), item.getName(),0, item.getPrice()));
		//order.addLine(0, catalogService.findAll().iterator().next().getItemId());
		return new ModelAndView("orderForm", "order", order);
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") Long id) throws InterruptedException, Exception {
		log.info("Calling OrderController.get() with id: {}", id);
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		OrderDTO order = orderService.getOne(id);
		log.info("Order: {}", order);
		return new ModelAndView("order", "order", orderService.getOne(id));
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(OrderDTO order) {
		log.info("Calling OrderController.post() with order: {}", order);
		order = orderService.save(order);
		return new ModelAndView("orderSuccess");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		log.info("Calling OrderController.delete() with id: {}", id);
		orderService.delete(id);

		return new ModelAndView("orderSuccess");
	}
}
