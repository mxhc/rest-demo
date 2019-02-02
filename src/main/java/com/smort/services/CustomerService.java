package com.smort.services;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;

public interface CustomerService {

    CustomerDTO findById(Long id);

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);

    CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO);

    CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO);

    void deleteCustomerById(Long id);

    CustomerListDTO getAllCustomersMeta();

    CustomerListDTO getAllCustomersPaginated(Integer page, int limit);
}
