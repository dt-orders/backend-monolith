package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.persistence.domain.Customer;

import java.util.Collection;

public interface CustomerService {

    Boolean isValidCustomerId(Long customerId);

    Collection<CustomerDTO> findAll();

    CustomerDTO getOne(Long customerId);

    CustomerDTO save(CustomerDTO customer);

    void delete(Long id);

}
