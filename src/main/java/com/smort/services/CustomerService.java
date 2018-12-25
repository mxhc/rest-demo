package com.smort.services;

import com.smort.api.v1.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO findById(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

}
