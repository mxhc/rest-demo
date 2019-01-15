package com.smort.controllers.v1;

import com.smort.api.v1.model.ActionDTO;
import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderItemDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.controllers.RestResponseEntityExceptionHandler;
import com.smort.domain.OrderStatus;
import com.smort.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends AbstractRestControllerTest {

    public static final Long ID = 1L;
    public static final LocalDateTime CREATED = LocalDateTime.now();
    public static final LocalDateTime UPDATED = LocalDateTime.now();

    public static final long CUSTOMER_ID = 2L;
    public static final String FIRST_NAME = "Milojko";
    public static final String LAST_NAME = "Pantic";

    @Mock
    OrderService orderService;

    @InjectMocks
    OrderController orderController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }



    @Test
    public void getAllOrders() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(OrderStatus.CREATED);
        orderDTO.setCreated(CREATED);
        orderDTO.setOrderUrl(OrderController.BASE_URL + "/" + ID);

        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setState(OrderStatus.RECEIVED);
        orderDTO2.setCreated(CREATED);
        orderDTO2.setOrderUrl(OrderController.BASE_URL + "/" + 2L);

        List<OrderDTO> orders = Arrays.asList(orderDTO, orderDTO2);

        when(orderService.getAllOrders(null)).thenReturn(orders);

        mockMvc.perform(get(OrderController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[1].order_url", equalTo(OrderController.BASE_URL + "/" + 2L)))
                .andExpect(jsonPath("$.orders", hasSize(2)));
    }

    @Test
    public void createNewOrder() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(OrderStatus.CREATED);
        orderDTO.setCreated(CREATED);
        orderDTO.setUpdated(UPDATED);
        orderDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + CUSTOMER_ID);
        orderDTO.setItemsUrl(OrderController.BASE_URL + "/" + ID + "/items/");
        orderDTO.setActions(Arrays.asList(new ActionDTO("/api/v1/orders/" + ID + "/purchase", "POST")));

        when(orderService.createNewOrder(CUSTOMER_ID)).thenReturn(orderDTO);

        mockMvc.perform(post(OrderController.BASE_URL + "/customers/" + CUSTOMER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.state", equalTo("CREATED")))
                    .andExpect(jsonPath("$.customer_url", equalTo(CustomerController.BASE_URL + "/" + CUSTOMER_ID)))
                    .andExpect(jsonPath("$.items_url", equalTo(OrderController.BASE_URL + "/" + ID + "/items/")))
                    .andExpect(jsonPath("$.actions", hasSize(1)));
    }

    @Test
    public void createNewOrderInvalidId() throws Exception {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(OrderStatus.CREATED);
        orderDTO.setCreated(CREATED);
        orderDTO.setUpdated(UPDATED);
        orderDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + CUSTOMER_ID);
        orderDTO.setItemsUrl(OrderController.BASE_URL + "/" + ID + "/items/");
        orderDTO.setActions(Arrays.asList(new ActionDTO("/api/v1/orders/" + ID + "/purchase", "POST")));

        when(orderService.createNewOrder(CUSTOMER_ID)).thenReturn(orderDTO);

        mockMvc.perform(post(OrderController.BASE_URL + "/customers/" + FIRST_NAME)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private OrderDTO getOrderDto() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(OrderStatus.CREATED);
        orderDTO.setCreated(CREATED);
        orderDTO.setUpdated(UPDATED);
        orderDTO.setCustomerUrl(CustomerController.BASE_URL + "/" + CUSTOMER_ID);
        orderDTO.setItemsUrl(OrderController.BASE_URL + "/" + ID + "/items/");
        orderDTO.setActions(Arrays.asList(new ActionDTO("/api/v1/orders/" + ID + "/purchase", "POST")));
        orderDTO.setOrderUrl(OrderController.BASE_URL + "/" + ID);
        orderDTO.setTotal(110.12);
        return orderDTO;
    }

    @Test
    public void getOrdersByCustomer() throws Exception {
        OrderDTO orderDTO = getOrderDto();

        OrderDTO orderDTO1 = getOrderDto();
        orderDTO1.setOrderUrl(OrderController.BASE_URL + "/" + 2L);
        orderDTO1.setItemsUrl(OrderController.BASE_URL + "/" + 2L + "/items/");

        OrderListDTO orderListDTO = new OrderListDTO(Arrays.asList(orderDTO, orderDTO1));

        when(orderService.getOrdersByCustomer(CUSTOMER_ID)).thenReturn(orderListDTO);

        mockMvc.perform(get(OrderController.BASE_URL + "/customers/" + CUSTOMER_ID)
                            .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.orders", hasSize(2)))
                        .andExpect(jsonPath("$.orders[1].state", equalTo("CREATED")))
                        .andExpect(jsonPath("$.orders[1].order_url", equalTo(OrderController.BASE_URL + "/" + 2L)));
    }

    @Test
    public void deleteOrder() throws Exception {

        mockMvc.perform(delete(OrderController.BASE_URL + "/" + ID)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(orderService, times(1)).deleteOrder(anyLong());

    }

    @Test
    public void getOrderById() throws Exception {

        OrderDTO orderDTO = getOrderDto();

        when(orderService.getOrderById(anyLong())).thenReturn(orderDTO);

        mockMvc.perform(get(OrderController.BASE_URL + "/" + ID)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", notNullValue()));

    }

    private OrderItemDTO getItemDto() {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + 5L);
        orderItemDTO.setQuantity(10);
        orderItemDTO.setPrice(25.5);
        orderItemDTO.setOrderUrl(getOrderDto().getOrderUrl());
        orderItemDTO.setItemUrl(getOrderDto().getItemsUrl());
        return orderItemDTO;
    }

    @Test
    public void addItemToOrder() throws Exception {

        OrderItemDTO orderItemDTO = getItemDto();

        when(orderService.addItemToOrder(anyLong(), any(OrderItemDTO.class))).thenReturn(orderItemDTO);

        mockMvc.perform(post(OrderController.BASE_URL + "/" + ID + "/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(orderItemDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.price", equalTo(getItemDto().getPrice())));

    }

    @Test
    public void getListOfItems() {
    }

    @Test
    public void purchase() {
    }

    @Test
    public void cancel() {
    }

    @Test
    public void deliver() {
    }

    @Test
    public void getItemFromOrder() {
    }

    @Test
    public void deleteItemFromOrder() {
    }
}