package com.ewolff.monolith.web;

import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;
import java.util.Date;

import com.ewolff.monolith.persistence.domain.Order;
import com.ewolff.monolith.persistence.repository.OrderRepository;
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

	private OrderRepository orderRepository;

	private OrderService orderService;
	private CustomerService customerService;
	private CatalogService catalogService;

	private String version;

	@Autowired
	private OrderController(OrderService orderService,
							CustomerService customerService,
							CatalogService catalogService) {
		super();
		this.customerService = customerService;
		this.catalogService = catalogService;
		this.orderService = orderService;
		this.version = System.getenv("APP_VERSION");
	}

	private String getVersion() {
		log.info("Current APP_VERSION: {}", this.version);
		return this.version;
	}

	private void setVersion(String newVersion) {
		this.version = newVersion;
		log.info("Setting APP_VERSION to: {}", this.version);
	}

	@ModelAttribute("items")
	public Collection<ItemDTO> items() {
		log.info("Calling OrderController.items()");
		return catalogService.findAll();
	}

	@ModelAttribute("customers")
	public Collection<CustomerDTO> customers() {
		log.info("Calling OrderController.customers()");

		if (this.getVersion().equals("2")) {
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
	public ModelAndView orderHome() {
		log.info("Calling OrderController.orderHome()");
		return new ModelAndView("orderlist", "orders",
				orderService.findAll());
	}

	@RequestMapping("/list.html")
	public ModelAndView orderList() {
		log.info("Calling OrderController.orderList()");
		return new ModelAndView("orderlist", "orders",
				orderService.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView form() {
		log.info("Calling OrderController.form()");
		return new ModelAndView("orderForm", "order", new Order());
	}

	@RequestMapping(value = "/line", method = RequestMethod.POST)
	public ModelAndView addLine(Order order) {
		log.info("Calling OrderController.addLine() with order: {}", order);
		order.addLine(0, catalogService.findAll().iterator().next().getItemId());
		return new ModelAndView("orderForm", "order", order);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView get(@PathVariable("id") long id) {
		log.info("Calling OrderController.get() with id: {}", id);
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView post(Order order) {
		log.info("Calling OrderController.post() with order: {}", order);
		order = orderService.order(order);
		return new ModelAndView("orderSuccess");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		log.info("Calling OrderController.delete() with id: {}", id);
		orderRepository.deleteById(id);

		return new ModelAndView("orderSuccess");
	}

   @RequestMapping(value = "/version", method = RequestMethod.GET)
   @ResponseBody
   public String showVersion() {
		String version;
		try {
			version = this.getVersion();
		}
		catch(Exception e) {
			version = "APP_VERSION not found";
		}
		return version;
   } 

	@RequestMapping(value = "setversion/{version}", method = RequestMethod.GET)
	public ModelAndView webSetVersion(@PathVariable("version") String newVersion) {
		this.setVersion(newVersion);
		return new ModelAndView("orderSuccess");
	}

	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public String getHealth() {

		Date dateNow = Calendar.getInstance().getTime();
		String health = "{ \"health\":[{\"service\":\"order-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
		return health;
	}
}
