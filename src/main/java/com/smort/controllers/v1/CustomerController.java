package com.smort.controllers.v1;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;
import com.smort.services.CustomerService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(description = "Customer Controller")
@Validated
@RestController
@RequestMapping(CustomerController.BASE_URL)
public class CustomerController {

    public static final String BASE_URL = "/api/v1/customers";

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @ApiOperation(value = "Get paginated list of customers", notes = "Collection of customers")
    @GetMapping("/paginated")
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDTO getPaginatedListOfCustomers(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return customerService.getAllCustomersPaginated(page, limit);
    }

    @ApiOperation(value = "${controller.customer.get.list}", notes = "${controller.customer.get.list.notes}")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomerListDTO getAllCustomers()  {
        return customerService.getAllCustomersMeta();
    }

    @ApiOperation(value = "${controller.customer.get}", notes = "${controller.customer.get.notes}")
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @ApiOperation(value = "${controller.customer.post}", notes = "${controller.customer.post.notes}")
    @PostMapping
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createNewCustomer(@ApiParam("${controller.customer.post.param}") @Valid @RequestBody CustomerDTO customerDTO) {
        return customerService.createNewCustomer(customerDTO);
    }

    // todo proveri da li postoji
    @ApiOperation(value = "${controller.customer.put}", notes = "${controller.customer.put.notes}")
    @PutMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO updateCustomer(@ApiParam("${controller.customer.put.param}") @Valid @RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        return customerService.saveCustomerByDTO(id, customerDTO);
    }

    @ApiOperation(value = "${controller.customer.patch}")
    @PatchMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO patchCustomer(@ApiParam("${controller.customer.patch.param}") @Valid @RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        return customerService.patchCustomer(id, customerDTO);
    }

    @ApiOperation(value = "${controller.customer.delete}")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomerById(id);
    }


}
