package com.ewolff.monolith.web;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.service.CustomerService;
import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.repository.CustomerRepository;

@Slf4j
@Controller
@RequestMapping("customer")
public class CustomerController {

	private CustomerService customerService;
	private BackendController backendController;

	@Autowired
	public CustomerController(CustomerService customerService, BackendController backendController) {
		this.customerService = customerService;
		this.backendController = backendController;
	}

	@RequestMapping("/list.html")
	public ModelAndView customerList() throws InterruptedException, Exception {
		log.info("In CustomerController.customerList() APP_VERSION: {}", backendController.getVersion());
		String version = backendController.getVersion();
		if (version.equals("2")) {
			System.out.println("About to call backendController.slowMeDown");
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("customerlist", "customers", customerService.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() throws InterruptedException, Exception {
		log.info("In CustomerController.add()");
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("customer", "customer", new CustomerDTO());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(CustomerDTO customer, HttpServletRequest httpRequest) {
		log.info("In CustomerController.post() with customerDto: {}", customer);
		customer = customerService.save(customer);
		return new ModelAndView("customerSuccess");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView customer(@PathVariable("id") Long id) throws InterruptedException, Exception {
		log.info("In CustomerController.customer() with id: {}", id);
		String version = backendController.getVersion();
		if (version.equals("2")) {
			backendController.slowMeDown();
		} 
		else if (version.equals("3")) {
			backendController.throwException();
		}
		return new ModelAndView("customer", "customer",
				customerService.getOne(id));
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") Long id, CustomerDTO customer,
							HttpServletRequest httpRequest) {
		log.info("In CustomerController.put() with id: {} and customerDto: {}", id, customer);
		customer.setCustomerId(id);
		log.info("Updating customer: {}", customer.getCustomerId());
		customerService.save(customer);
		return new ModelAndView("customerSuccess");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id) {
		log.info("In CustomerController.delete() with id: {}", id);
		customerService.delete(id);
		return new ModelAndView("customerSuccess");
	}
}
