package com.ewolff.monolith.config;

import com.ewolff.monolith.persistence.repository.CustomerRepository;
import com.ewolff.monolith.persistence.repository.ItemRepository;
import com.ewolff.monolith.persistence.repository.OrderRepository;
import com.ewolff.monolith.service.CatalogService;
import com.ewolff.monolith.service.CustomerService;
import com.ewolff.monolith.service.OrderService;
import com.ewolff.monolith.service.impl.CatalogServiceImpl;
import com.ewolff.monolith.service.impl.CustomerServiceImpl;
import com.ewolff.monolith.service.impl.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CustomerService customerService(CustomerRepository customerRepo) {
        return new CustomerServiceImpl(customerRepo);
    }

    @Bean
    public CatalogService catalogService(ItemRepository itemRepo) {
        return new CatalogServiceImpl(itemRepo);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepo,
                                     CustomerService customerService, CatalogService catalogService) {
        return new OrderServiceImpl(orderRepo, customerService, catalogService);
    }


}
