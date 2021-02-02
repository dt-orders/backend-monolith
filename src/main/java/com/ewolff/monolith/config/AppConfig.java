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
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public CustomerService customerService(CustomerRepository customerRepo, ModelMapper mapper) {
        return new CustomerServiceImpl(customerRepo, mapper);
    }

    @Bean
    public CatalogService catalogService(ItemRepository itemRepo, ModelMapper mapper) {
        return new CatalogServiceImpl(itemRepo, mapper);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepo,
                                     CustomerService customerService, CatalogService catalogService, ModelMapper mapper) {
        return new OrderServiceImpl(orderRepo, customerService, catalogService, mapper);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }

}
