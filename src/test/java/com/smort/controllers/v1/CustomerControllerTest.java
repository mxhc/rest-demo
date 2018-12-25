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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

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
        c1.setFirstName(FIRST_NAME);
        c1.setLastName(LAST_NAME);

        CustomerDTO c2 = new CustomerDTO();
        c2.setCustomerUrl("/api/v1/customers/2");
        c2.setFirstName("Ivan");
        c2.setLastName("Trajovic");

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
        c1.setFirstName(FIRST_NAME);
        c1.setLastName(LAST_NAME);

        when(customerService.findById(anyLong())).thenReturn(c1);

        mockMvc.perform(get("/api/v1/customers/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(FIRST_NAME)));


    }
}