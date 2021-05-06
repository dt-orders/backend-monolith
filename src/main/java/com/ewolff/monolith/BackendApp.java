package com.ewolff.monolith;

//RJAHN
import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.repository.CustomerRepository;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.ItemRepository;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
        	for (int it=1; it<= 100; it++) {
	            HttpGet httpget = new HttpGet("https://randomuser.me/api/?nat=us");
	
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
	                        // sometimes get unicode data for a customer that causes errors
							//throw new ClientProtocolException("Unexpected response status: " + status);
							System.out.println("Unexpected response status: " + status);
							return null;
	                    }
	                }
	
	            };

				String responseBody = httpclient.execute(httpget, responseHandler);
				if (responseBody != null) {
					log.info("Adding customer: {}", it);
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
						log.info("fName: {}, lName: {}, email: {}.{}@gmail.com ,street: {}, city: {}", fName, lName, fName, lName, street, city);
						customerRepository.save(new Customer(fName, lName, "aa@gmail.com", street, city));
					}
				}
				else {
					log.info("Skipping customer: {}", it);
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