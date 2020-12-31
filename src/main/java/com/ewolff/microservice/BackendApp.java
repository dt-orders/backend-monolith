package com.ewolff.microservice;

//RJAHN
import com.ewolff.microservice.customer.Customer;
import com.ewolff.microservice.customer.CustomerRepository;
import com.ewolff.microservice.catalog.Item;
import com.ewolff.microservice.catalog.ItemRepository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.IOException;
import java.util.Iterator;

//RJAHN
//this was in the OrderApp, but the other two Apps used @EnableAutoConfiguration
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//@SpringBootApplication

@ComponentScan
@EnableAutoConfiguration
@Component
public class BackendApp {

	private final CustomerRepository customerRepository;
	private final ItemRepository itemRepository;

	@Autowired
	public BackendApp(CustomerRepository customerRepository, ItemRepository itemRepository) {
		this.customerRepository = customerRepository;
		this.itemRepository = itemRepository;

	}

	@PostConstruct
	public void generateTestData() {

		// Catalog Data
		itemRepository.save(new Item("iPod", 42.0));
		itemRepository.save(new Item("iPod touch", 21.0));
		itemRepository.save(new Item("iPod nano", 1.0));
		itemRepository.save(new Item("Apple TV", 100.0));

		// Customer Data
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	for (int it=0; it< 100; it++) {
	            HttpGet httpget = new HttpGet("https://randomuser.me/api/");
	
	            //System.out.println("Executing request " + httpget.getRequestLine());
	
	            // Create a custom response handler
	            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
	
	            	@Override
	                public String handleResponse(
	                        final HttpResponse response) throws ClientProtocolException, IOException {
	                    int status = response.getStatusLine().getStatusCode();
	                    if (status >= 200 && status < 300) {
	                        HttpEntity entity = response.getEntity();
	                        return entity != null ? EntityUtils.toString(entity) : null;
	                    } else {
	                        throw new ClientProtocolException("Unexpected response status: " + status);
	                    }
	                }
	
	            };
	            String responseBody = httpclient.execute(httpget, responseHandler);
	        		JSONParser parser = new JSONParser();
	        		JSONObject obj = (JSONObject) parser.parse(responseBody);
	        		JSONArray result = (JSONArray) obj.get("results");
	        		Iterator i = result.iterator();
	        		while (i.hasNext()) {
	        			JSONObject user = (JSONObject) i.next();
	        			JSONObject name = (JSONObject) user.get("name");
	        			String fName = ((String)name.get("first"));
	        			String lName = ((String)name.get("last"));
						JSONObject address = (JSONObject) user.get("location");
						String street = address.get("street").toString();
						String city = address.get("city").toString();
	        			System.out.println(fName + " " +lName + " " + fName + "." + lName + "@gmail.com" + " " + street + " " + city);
						customerRepository.save(new Customer(fName, lName,"aa@gmail.com", street, city));
	        		}
	        	}
        }
        	catch (Exception e) {
        		e.printStackTrace();
        	}
        finally {
        	try {
        		httpclient.close();
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        	}
        }
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApp.class, args);
	}

}