package com.ewolff.monolith.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Calendar;
import java.util.Date; 
import java.io.*;

@Slf4j
@Controller
@RequestMapping("")
public class BackendController {

	private String version;

	public String getVersion() {
		log.info("BackendController Current APP_VERSION: {}", this.version);
		return this.version;
	}

	public void setVersion(String newVersion) {
		this.version = newVersion;
		log.info("BackendController Setting APP_VERSION to: {}", this.version);
	}

	public void slowMeDown() throws InterruptedException {
		System.out.println("Doing a fake slowdown");
		Integer sleepTime = Integer.valueOf(System.getenv("SLEEP_TIME"));
		Thread.sleep(sleepTime);
	}

	public void throwException() throws Exception {
		System.out.println("Throwing fake exception");
		throw new Exception("Throwing fake exception");
	}

	@Autowired
	public BackendController() {
		this.version = System.getenv("APP_VERSION");
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
	 @ResponseBody
	 public String webSetVersion(@PathVariable("version") String newVersion) {
		 this.setVersion(newVersion);
		 return "Set version to: " + newVersion;
	 }
	 
	 @RequestMapping(value = "/health", method = RequestMethod.GET)
	 @ResponseBody
	 public String getHealth() {
 
		 Date dateNow = Calendar.getInstance().getTime();
		 String health = "{ \"health\":[{\"service\":\"backend-monolith\",\"status\":\"OK\",\"date\":\"" + dateNow + "\" }]}";
		 return health;
	 }
	
	@RequestMapping(value = "/manifest", method = RequestMethod.GET)
	@ResponseBody
	public String getManifest() {
		 File file = new File("/MANIFEST"); 
		 String manifest = "MANIFEST file not found";
		 try {
			 BufferedReader br = new BufferedReader(new FileReader(file));
			 String line = br.readLine();
			 manifest = line + "<BR>";
			 while ((line = br.readLine()) != null) {
				manifest = manifest + line + "<BR>";
			 }
			 br.close();
		 }
		 catch(Exception e) {
			manifest = e.getMessage();
		 }
		 return manifest;
	}
}
