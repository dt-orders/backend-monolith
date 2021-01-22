package com.ewolff.monolith.web;

import com.ewolff.monolith.persistence.domain.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;

@Configuration
class OrderSpringRestDataConfig extends RepositoryRestConfigurerAdapter {

//	@Bean
//	public RepositoryRestConfigurer repositoryRestConfigurer() {
//
//		return new RepositoryRestConfigurerAdapter() {
//			@Override
//			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//				config.exposeIdsFor(Order.class, ItemDTO.class, CustomerDTO.class);
//			}
//		};
//	}

}
