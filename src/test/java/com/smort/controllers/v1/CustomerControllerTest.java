package com.smort.controllers.v1;

import com.smort.api.v1.model.CustomerDTO;
import com.smort.controllers.RestResponseEntityExceptionHandler;
import com.smort.error.ResourceNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

    }

    @Test
    public void getAllCustomers() throws Exception {
        // given
        CustomerDTO c1 = new CustomerDTO();
        c1.setCustomerUrl(CustomerController.BASE_URL + "/1");
        c1.setFirstname(FIRST_NAME);
        c1.setLastname(LAST_NAME);

        CustomerDTO c2 = new CustomerDTO();
        c2.setCustomerUrl(CustomerController.BASE_URL + "/2");
        c2.setFirstname("Ivan");
        c2.setLastname("Trajovic");

        List<CustomerDTO> customers = Arrays.asList(c1, c2);

        // when
        when(customerService.getAllCustomers()).thenReturn(customers);

        // then
        mockMvc.perform(get(CustomerController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));


    }

    @Test
    public void getCustomerById() throws Exception {

        CustomerDTO c1 = new CustomerDTO();
        c1.setCustomerUrl(CustomerController.BASE_URL + "/1");
        c1.setFirstname(FIRST_NAME);
        c1.setLastname(LAST_NAME);

        when(customerService.findById(anyLong())).thenReturn(c1);

        mockMvc.perform(get(CustomerController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON))
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
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.createNewCustomer(customer)).thenReturn(returnDTO);

        mockMvc.perform(post(CustomerController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname", equalTo("Ivan")))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));

    }

    @Test
    public void testUpdateCustomer() throws Exception {

        // given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname("Ivan");
        customer.setLastname("Trajkovic");

        CustomerDTO returnDto = new CustomerDTO();
        returnDto.setFirstname(customer.getFirstname());
        returnDto.setLastname(customer.getLastname());
        returnDto.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(returnDto);

        // when/then
        mockMvc.perform(put(CustomerController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Ivan")))
                .andExpect(jsonPath("$.lastname", equalTo("Trajkovic")))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void testPatchCustomer() throws Exception {

        // given
        CustomerDTO customer = new CustomerDTO();
        customer.setFirstname("Fred");
        customer.setLastname("Flintstone");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstname(customer.getFirstname());
        returnDTO.setLastname("Flintstone");
        returnDTO.setCustomerUrl(CustomerController.BASE_URL + "/1");

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", equalTo("Fred")))
                .andExpect(jsonPath("$.lastname", equalTo("Flintstone")))
                .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/1")));
    }

    @Test
    public void testDeleteCustomer() throws Exception {

        mockMvc.perform(delete(CustomerController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomerById(anyLong());

    }

    @Test
    public void testNotFoundException() throws Exception {

        when(customerService.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.BASE_URL + "/222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void failedNameValidationTest() throws Exception {
        CustomerDTO invalidCustomer = new CustomerDTO();
        invalidCustomer.setFirstname("A");

        mockMvc.perform(post(CustomerController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        invalidCustomer.setFirstname("Ana");
        invalidCustomer.setLastname("");

        mockMvc.perform(post(CustomerController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(put(CustomerController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidCustomer)))
                .andExpect(status().isBadRequest());

    }



}