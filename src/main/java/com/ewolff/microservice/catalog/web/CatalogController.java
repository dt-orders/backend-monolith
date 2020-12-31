package com.ewolff.microservice.catalog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.Date; 

import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;

import java.io.*;

@Controller
@RequestMapping("catalog")
public class CatalogController {

	private final ItemRepository itemRepository;

	@Autowired
	public CatalogController(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView Item(@PathVariable("id") long id) {
		return new ModelAndView("item", "item", itemRepository.findById(id).get());
	}

	@RequestMapping("/list.html")
	public ModelAndView ItemList() {
		return new ModelAndView("itemlist", "items", itemRepository.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		return new ModelAndView("item", "item", new Item());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(Item Item) {
		Item = itemRepository.save(Item);
		return new ModelAndView("itemSuccess");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") long id, Item item) {
		item.setId(id);
		itemRepository.save(item);
		return new ModelAndView("itemSuccess");
	}

	@RequestMapping(value = "/searchForm.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView searchForm() {
		return new ModelAndView("searchForm");
	}

	@RequestMapping(value = "/searchByName.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView search(@RequestParam("query") String query) {
		return new ModelAndView("itemlist", "items",
				itemRepository.findByNameContaining(query));
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") long id) {
		itemRepository.deleteById(id);
		return new ModelAndView("itemSuccess");
	}

	@RequestMapping(value = "/version", method = RequestMethod.GET)
	@ResponseBody
	public String getVersion() {
		 File file = new File("version"); 
		 String version = "version not found";
		 try {
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 version = br.readLine();
		 }
		 catch(Exception e) {
			 version = e.getMessage();
		 }
		 return version;
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public String getHealth() {

		Date dateNow = Calendar.getInstance().getTime();
		String health = "{ \"health\":[{\"service\":\"catalog-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
		return health;
	}
}
