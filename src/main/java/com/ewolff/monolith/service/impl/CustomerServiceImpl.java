package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.CustomerRepository;
import com.ewolff.monolith.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepo;

    private final ModelMapper mapper;

    public CustomerServiceImpl(CustomerRepository customerRepo, ModelMapper mapper) {
        this.customerRepo = customerRepo;
        this.mapper = mapper;
    }

    @Override
    public Boolean isValidCustomerId(Long customerId) {
        log.info("Calling CustomerService.isValidCustomerId() for id: {}", customerId);
        if (customerRepo.findById(customerId) != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Collection<CustomerDTO> findAll() {
        log.info("Calling CustomerService.findAll()");
        Iterable<Customer> customers = customerRepo.findAll();
        List<CustomerDTO> dtoList = StreamSupport.stream(customers.spliterator(), false)
                .map(customer -> mapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
        return dtoList;
   }

    @Override
    public CustomerDTO getOne(Long customerId) {
        log.info("Calling CustomerService.getOne(): {}", customerId);
        CustomerDTO dto = null;
        Optional<Customer> customerOpt = customerRepo.findById(customerId);
        if (customerOpt.isPresent()) {
            dto = mapper.map(customerOpt.get(), CustomerDTO.class);
        }
        return dto;
    }

    @Override
    public CustomerDTO save(CustomerDTO dto) {
        log.info("Calling CustomerService.save() with customerDto: {}", dto);
        Customer x = mapper.map(dto, Customer.class);
        x.setId(dto.getCustomerId());
        Customer c = customerRepo.save(x);
        return new CustomerDTO(c.getCustomerId(), c.getFirstname(), c.getName(), c.getEmail(), c.getStreet(), c.getCity());
    }

    @Override
    public void delete(Long id) {
        log.info("Calling CustomerService.delete() for id: {}", id);
        if (customerRepo.findById(id) != null) {
            customerRepo.deleteById(id);
        }
    }
}
