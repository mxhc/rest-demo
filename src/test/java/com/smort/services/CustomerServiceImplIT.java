package com.smort.services;

//import com.smort.bootstrap.Bootstrap;
//import com.smort.repositories.*;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static junit.framework.TestCase.assertEquals;
//import static junit.framework.TestCase.assertNotNull;
//import static org.hamcrest.core.IsEqual.equalTo;
//import static org.hamcrest.core.IsNot.not;
//import static org.junit.Assert.assertThat;

//@RunWith(SpringRunner.class)
//@DataJpaTest
public class CustomerServiceImplIT {
//
//    @Autowired
//    CustomerRepository customerRepository;
//
//    @Autowired
//    CustomerRepositoryPaging customerRepositoryPaging;
//
//    @Autowired
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    VendorRepository vendorRepository;
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Autowired
//    OrderRepository orderRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    CustomerService customerService;
//
//    @Before
//    public void setUp() throws Exception {
//
//        System.out.println("Loading Customer Data");
//        System.out.println(customerRepository.findAll().size());
//
//        // set up data for testing
//        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository, productRepository, orderRepository, userRepository);
//        bootstrap.run(); //  load data
//
//        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository, customerRepositoryPaging);
//
//    }
//
//    @Test
//    public void patchCustomerUpdateFirstName() throws Exception {
//
//        String updatedName = "UpdatedName";
//        long id = getCustomerIdValue();
//
//        Customer originalCustomer = customerRepository.getOne(id);
//        assertNotNull(originalCustomer);
//
//        // save original names
//        String originalFirstName = originalCustomer.getFirstName();
//        String originalLastName = originalCustomer.getLastName();
//
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setFirstname(updatedName);
//
//        customerService.patchCustomer(id, customerDTO);
//
//        Customer updatedCustomer = customerRepository.findById(id).get();
//
//        assertNotNull(updatedCustomer);
//        assertEquals(updatedName, updatedCustomer.getFirstName());
//        assertThat(originalFirstName, not(equalTo(updatedCustomer.getFirstName())));
//        assertThat(originalLastName, equalTo(updatedCustomer.getLastName()));
//
//
//    }
//
//    @Test
//    public void patchCustomerUpdateLastName() throws Exception {
//        String updatedName = "UpdatedName";
//        long id = getCustomerIdValue();
//
//        Customer originalCustomer = customerRepository.getOne(id);
//        assertNotNull(originalCustomer);
//
//        //save original first/last name
//        String originalFirstName = originalCustomer.getFirstName();
//        String originalLastName = originalCustomer.getLastName();
//
//        CustomerDTO customerDTO = new CustomerDTO();
//        customerDTO.setLastname(updatedName);
//
//        customerService.patchCustomer(id, customerDTO);
//
//        Customer updatedCustomer = customerRepository.findById(id).get();
//
//        assertNotNull(updatedCustomer);
//        assertEquals(updatedName, updatedCustomer.getLastName());
//        assertThat(originalFirstName, equalTo(updatedCustomer.getFirstName()));
//        assertThat(originalLastName, not(equalTo(updatedCustomer.getLastName())));
//    }
//
//    private Long getCustomerIdValue() {
//        List<Customer> customers = customerRepository.findAll();
//
//        System.out.println("Customers found:` " + customers.size());
//
//        return customers.get(0).getId();
//    }
//
//    //todo create validation tests
}
