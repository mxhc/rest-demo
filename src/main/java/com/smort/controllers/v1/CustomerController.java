package com.smort.controllers.v1;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;
import com.smort.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<CustomerListDTO> getAllCustomers()  {
        return new ResponseEntity<CustomerListDTO>(new CustomerListDTO(customerService.getAllCustomers()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        return new ResponseEntity<CustomerDTO>(customerService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createNewCustomer(@RequestBody CustomerDTO customerDTO) {
        return new ResponseEntity<CustomerDTO>(customerService.createNewCustomer(customerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateNewCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        return new ResponseEntity<CustomerDTO>(customerService.saveCustomerByDTO(id, customerDTO), HttpStatus.OK);
    }


}
