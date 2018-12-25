package com.smort.services;

import com.smort.api.v1.mapper.CustomerMapper;
import com.smort.api.v1.model.CustomerDTO;
import com.smort.domain.Customer;
import com.smort.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CustomerServiceImplTest {

    public static final long ID = 2L;
    public static final String FIRST_NAME = "Milojko";
    public static final String LAST_NAME = "Pantic";
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);

    }

    @Test
    public void getAllCustomers() {

        //given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        // when
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        // then
        assertEquals(3, customerDTOS.size());

    }

    @Test
    public void findById() {

        // given
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);
        Optional<Customer> optionalCustomer = Optional.ofNullable(customer);

        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);

        // when
        CustomerDTO customerDTO = customerService.findById(ID);

        // then
        assertEquals(FIRST_NAME, customerDTO.getFirstname());
        assertEquals(LAST_NAME, customerDTO.getLastname());

    }

    @Test
    public void createNewCustomer() throws Exception {

        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Milojko");

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstname(customerDTO.getFirstname());
        savedCustomer.setLastname(customerDTO.getLastname());
        savedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // when
        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);

        // then
        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals("/api/v1/customer/1", savedDTO.getCustomerUrl());

    }

}