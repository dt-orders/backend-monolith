package com.ewolff.monolith.service;

import com.ewolff.monolith.dto.CustomerDTO;

import java.util.Collection;

public interface CustomerService {

    Boolean isValidCustomerId(Long customerId);

    Collection<CustomerDTO> findAll();

    CustomerDTO getOne(long customerId);
}
