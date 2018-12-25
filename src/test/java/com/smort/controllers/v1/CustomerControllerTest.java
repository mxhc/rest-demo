package com.smort.controllers.v1;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.services.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends AbstractRestControllerTest {

    public static final Long ID = 1L;
    public static final String FIRST_NAME = "Milojko";
    public static final String LAST_NAME = "Pantic";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();

    }

    @Test
    public void getAllCustomers() throws Exception {
        // given
        CustomerDTO c1 = new CustomerDTO();
        c1.setCustomerUrl("/api/v1/customers/1");
        c1.setFirstname(FIRST_NAME);
        c1.setLastname(LAST_NAME);

        CustomerDTO c2 = new CustomerDTO();
        c2.setCustomerUrl("/api/v1/customers/2");
        c2.setFirstname("Ivan");
        c2.setLastname("Trajovic");

        List<CustomerDTO> customers = Arrays.asList(c1, c2);

        // when
        when(customerService.getAllCustomers()).thenReturn(customers);

        // then
        mockMvc.perform(get("/api/v1/customers/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));


    }

    @Test
    public void getCustomerById() throws Exception {

        CustomerDTO c1 = new CustomerDTO();
        c1.setCustomerUrl("/api/v1/customers/1");
        c1.setFirstname(FIRST_NAME);
        c1.setLastname(LAST_NAME);

        when(customerService.findById(anyLong())).thenReturn(c1);

        mockMvc.perform(get("/api/v1/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo(FIRST_NAME)));

    }

    @Test
    public void createNewCustomer() throws Exception {

        // given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname("Ivan");
        customer.setLastname("Trajovic");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(customer.getFirstname());
        returnDTO.setLastname(customer.getLastname());
        returnDTO.setCustomerUrl("/api/v1/customers/1");

        when(customerService.createNewCustomer(customer)).thenReturn(returnDTO);

        mockMvc.perform(post("/api/v1/customers/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo("Ivan")))
                .andExpect(jsonPath("$.customer_url", equalTo("/api/v1/customers/1")));

    }

}