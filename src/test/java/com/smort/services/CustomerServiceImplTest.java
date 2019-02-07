package com.smort.services;

import com.smort.api.v1.mapper.CustomerMapper;
import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;
import com.smort.controllers.v1.CustomerController;
import com.smort.domain.Customer;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.CustomerRepositoryPaging;
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
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    public static final long ID = 2L;
    public static final String FIRST_NAME = "Milojko";
    public static final String LAST_NAME = "Pantic";
    CustomerService customerService;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerRepositoryPaging customerRepositoryPaging;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository, customerRepositoryPaging);

    }

    @Test
    public void getAllCustomers() {

        //given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customers);

        // when
        CustomerListDTO customerListDTO = customerService.getAllCustomersMeta();

        // then
        assertEquals(3, customerListDTO.getCustomers().size());

    }

    @Test
    public void findById() {

        // given
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstName(FIRST_NAME);
        customer.setLastName(LAST_NAME);
        Optional<Customer> optionalCustomer = Optional.ofNullable(customer);

        when(customerRepository.findById(anyLong())).thenReturn(optionalCustomer);

        // when
        CustomerDTO customerDTO = customerService.findById(ID);

        // then
        assertEquals(FIRST_NAME, customerDTO.getFirstname());
        assertEquals(LAST_NAME, customerDTO.getLastname());
        assertEquals(CustomerController.BASE_URL + "/" + ID, customerDTO.getCustomerUrl());

    }

    @Test
    public void createNewCustomer() throws Exception {

        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Milojko");

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstname());
        savedCustomer.setLastName(customerDTO.getLastname());
        savedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // when
        CustomerDTO savedDTO = customerService.createNewCustomer(customerDTO);

        // then
        assertEquals(customerDTO.getFirstname(), savedDTO.getFirstname());
        assertEquals(CustomerController.BASE_URL + "/1", savedDTO.getCustomerUrl());

    }

    @Test
    public void saveCustomerByDTO() throws Exception {

        // given
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstname("Milojko");

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(customerDTO.getFirstname());
        savedCustomer.setLastName(customerDTO.getLastname());
        savedCustomer.setId(1L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // when
        CustomerDTO savedDto = customerService.saveCustomerByDTO(1L, customerDTO);

        // then
        assertEquals(customerDTO.getFirstname(), savedDto.getFirstname());
        assertEquals(CustomerController.BASE_URL + "/1", savedDto.getCustomerUrl());


    }

    @Test
    public void deleteCustomerById() {

        Long id = 1L;

        Customer savedCustomer = new Customer();
        savedCustomer.setFirstName(FIRST_NAME);
        savedCustomer.setLastName(LAST_NAME);
        savedCustomer.setId(1L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(savedCustomer));

        customerService.deleteCustomerById(id);

        verify(customerRepository, times(1)).deleteById(anyLong());

    }
}