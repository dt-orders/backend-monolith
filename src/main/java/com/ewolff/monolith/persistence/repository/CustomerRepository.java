package com.ewolff.monolith.persistence.repository;

import java.util.List;

import com.ewolff.monolith.persistence.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "customer", path = "customer")
public interface CustomerRepository extends
		PagingAndSortingRepository<Customer, Long> {

	List<Customer> findByName(@Param("name") String name);

}
