package com.ewolff.monolith.web;

import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
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

import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.ItemRepository;

@Slf4j
@Controller
@RequestMapping("catalog")
public class CatalogController {

	private String version;
	private final CatalogService catalogService;

	private String getVersion() {
		log.info("Current APP_VERSION: {}", this.version);
		return this.version;
	}

	private void setVersion(String newVersion) {
		this.version = newVersion;
		log.info("Setting APP_VERSION to: {}", this.version);
	}

	@Autowired
	public CatalogController(CatalogService catalogService) {
		this.catalogService = catalogService;
		this.version = System.getenv("APP_VERSION");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView item(@PathVariable("id") Long id) {
		log.info("In CatalogController.item() with id: {}", id);
		return new ModelAndView("item", "item", catalogService.getOne(id));
	}

	@RequestMapping("/list.html")
	public ModelAndView itemList() {
		log.info("In CatalogController.itemList()");
		return new ModelAndView("itemlist", "items", catalogService.findAll());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.GET)
	public ModelAndView add() {
		log.info("In CatalogController.add()");
		return new ModelAndView("item", "item", new ItemDTO());
	}

	@RequestMapping(value = "/form.html", method = RequestMethod.POST)
	public ModelAndView post(ItemDTO item) {
		log.info("In CatalogController.post() for itemDto: {}", item);
		item = catalogService.save(item);
		return new ModelAndView("itemSuccess");
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.PUT)
	public ModelAndView put(@PathVariable("id") Long id, ItemDTO item) {
		log.info("In CatalogController.put() for itemId: {} and itemDto: {}", id, item);
		item.setItemId(id);
		catalogService.save(item);
		return new ModelAndView("itemSuccess");
	}

	@RequestMapping(value = "/searchForm.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView searchForm() {
		log.info("In CatalogController.searchForm()");
		return new ModelAndView("itemSearchForm");
	}

	@RequestMapping(value = "/searchByName.html", produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView search(@RequestParam("query") String query) {
		log.info("In CatalogController.search() with query: {}", query);
		return new ModelAndView("itemlist", "items",
			catalogService.search(query));
	}

	@RequestMapping(value = "/{id}.html", method = RequestMethod.DELETE)
	public ModelAndView delete(@PathVariable("id") Long id) {
		log.info("In CatalogController.delete() with id: {}", id);
		catalogService.delete(id);
		return new ModelAndView("itemSuccess");
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
		return new ModelAndView("itemSuccess");
	}
	
	@RequestMapping(value = "/health", method = RequestMethod.GET)
	@ResponseBody
	public String getHealth() {

		Date dateNow = Calendar.getInstance().getTime();
		String health = "{ \"health\":[{\"service\":\"catalog-service\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
		return health;
	}
}
