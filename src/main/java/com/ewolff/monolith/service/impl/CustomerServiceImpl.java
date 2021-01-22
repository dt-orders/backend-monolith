package com.ewolff.monolith.service.impl;

import com.ewolff.monolith.dto.CustomerDTO;
import com.ewolff.monolith.dto.ItemDTO;
import com.ewolff.monolith.persistence.domain.Customer;
import com.ewolff.monolith.persistence.domain.Item;
import com.ewolff.monolith.persistence.repository.CustomerRepository;
import com.ewolff.monolith.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepo;

    public CustomerServiceImpl(CustomerRepository customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Boolean isValidCustomerId(Long customerId) {
        if (customerRepo.findById(customerId) != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public Collection<CustomerDTO> findAll() {
        List<CustomerDTO> dtoList = new ArrayList<>();
        for (Customer customer : customerRepo.findAll()) {
            dtoList.add(new CustomerDTO(customer.getId(), customer.getFirstname(), customer.getName(), customer.getEmail(),
                    customer.getStreet(), customer.getCity()));
        }
        return dtoList;
    }

    @Override
    public CustomerDTO getOne(long customerId) {
        CustomerDTO dto = null;
        Optional<Customer> customerOpt = customerRepo.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            dto = new CustomerDTO(customer.getId(), customer.getFirstname(), customer.getName(), customer.getEmail(),
                    customer.getStreet(), customer.getCity());
        }
        return dto;
    }

    @Override
    public CustomerDTO save(CustomerDTO dto) {

        Customer x = new Customer(dto.getFirstname(), dto.getName(), dto.getEmail(), dto.getStreet(),
                dto.getCity());
        x.setId(dto.getCustomerId());

        Customer c = customerRepo.save(x);

        return new CustomerDTO(c.getCustomerId(), c.getFirstname(), c.getName(), c.getEmail(), c.getStreet(), c.getCity());
    }

    @Override
    public void delete(long id) {
        if (customerRepo.findById(id) != null) {
            customerRepo.deleteById(id);
        }
    }
}
