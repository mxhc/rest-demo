package com.smort.controllers.v1;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;
import com.smort.services.CustomerService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(description = "This is Customer Controller")
@RestController
@RequestMapping(CustomerController.BASE_URL)
public class CustomerController {

    public static final String BASE_URL = "/api/v1/customers";

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "This will get a list of customers", notes = "Collection of Customers")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDTO getAllCustomers()  {
        return new CustomerListDTO(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createNewCustomer(@ApiParam("Customer information for new customer to be created") @RequestBody CustomerDTO customerDTO) {
        return customerService.createNewCustomer(customerDTO);
    }

    @PutMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(@ApiParam("Customer information for customer to be edited")@RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        return customerService.saveCustomerByDTO(id, customerDTO);
    }

    @PatchMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO patchCustomer(@ApiParam("Customer information for new customer to be patched")@RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        return customerService.patchCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }


}
