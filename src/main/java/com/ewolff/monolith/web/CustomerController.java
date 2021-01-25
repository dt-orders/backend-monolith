package com.ewolff.monolith.web;

import javax.servlet.http.HttpServletRequest;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.service.CustomerService;
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

import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.repository.CustomerRepository;

@Slf4j
@Controller
@RequestMapping("customer")
public class CustomerController {

	private String version;

	private CustomerService customerService;

	private String getVersion() {
		log.info("Current APP_VERSION: {}", this.version);
		return this.version;
	}

	private void setVersion(String newVersion) {
		this.version = newVersion;
		log.info("Setting APP_VERSION to: {}", this.version);
	}

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
		this.version = System.getenv("APP_VERSION");
	}


	@RequestMapping("/list.html")
	public ModelAndView customerList() {
		log.info("In CustomerController.customerList() with APP_VERSION: {}", this.getVersion());

		if (this.getVersion().equals("2")) {
			log.info("Response Time problem = ON");
			try
			{
				// ************************************************
				// Response Time problem
				// ************************************************
				Thread.sleep(5000);
			}
			catch(InterruptedException ex)
			{
			   Thread.currentThread().interrupt();
			}
			return new ModelAndView("customerlist", "customers",
					customerService.findAll());
		}
		else {
			log.info("Response Time problem = OFF");
			return new ModelAndView("customerlist", "customers",
					customerService.findAll());
		}
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		log.info("In CustomerController.add()");
		return new ModelAndView("customer", "customer", new CustomerDTO());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(CustomerDTO customer, HttpServletRequest httpRequest) {
		log.info("In CustomerController.post() with customerDto: {}", customer);
		customer = customerService.save(customer);
		return new ModelAndView("customerSuccess");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView customer(@PathVariable("id") Long id) {
		log.info("In CustomerController.customer() with id: {}", id);
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
		return new ModelAndView("customerSuccess");
	}

   @RequestMapping(value = "/health", method = RequestMethod.GET)
   @ResponseBody
   public String getHealth() {

	   Date dateNow = Calendar.getInstance().getTime();
	   String health = "{ \"health\":[{\"service\":\"customer-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
	   return health;
   }

}
