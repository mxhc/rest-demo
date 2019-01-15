package com.smort.services;

import com.smort.api.v1.mapper.OrderMapper;
import com.smort.api.v1.model.OrderDTO;
import com.smort.domain.Customer;
import com.smort.domain.Order;
import com.smort.domain.OrderStatus;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.OrderItemRepository;
import com.smort.repositories.OrderRepository;
import com.smort.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class OrderServiceImplTest {

    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderItemRepository orderItemRepository;

    public static final long ID = 2L;
    public static final String FIRST_NAME = "Milojko";
    public static final String LAST_NAME = "Pantic";

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        orderService = new OrderServiceImpl(orderRepository, OrderMapper.INSTANCE,
                customerRepository, productRepository, orderItemRepository);

    }

    @Test
    public void getAllOrders() {

        List<Order> orders = Arrays.asList(new Order(), new Order(), new Order());

        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderDTO> orderDTOS = orderService.getAllOrders(null);

        assertEquals(3, orderDTOS.size());

    }

    @Test
    public void findById() {
        final LocalDateTime created = LocalDateTime.now();
        final LocalDateTime updated = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        Order order = new Order();
        order.setId(1L);
        order.setState(OrderStatus.ORDERED);
        order.setCreated(created);
        order.setUpdated(updated);
        order.setCustomer(customer);
        Optional<Order> optionalOrder = Optional.ofNullable(order);

        when(orderRepository.findById(anyLong())).thenReturn(optionalOrder);

        OrderDTO orderDTO = orderService.findById(1L);

        assertEquals(OrderStatus.ORDERED, orderDTO.getState());
        assertEquals("/api/v1/customers/2", orderDTO.getCustomerUrl());
        assertEquals("/api/v1/orders/1/items/", orderDTO.getItemsUrl());
        assertEquals("/api/v1/orders/1/cancel", orderDTO.getActions().get(0).getUrl());
        assertEquals(2, orderDTO.getActions().size());
    }

    @Test
    public void createNewOrder() {

        final LocalDateTime created = LocalDateTime.now();
        final LocalDateTime updated = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        Order order = new Order();
        order.setId(1L);
        order.setState(OrderStatus.ORDERED);
        order.setCreated(created);
        order.setUpdated(updated);
        order.setCustomer(customer);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderDTO orderDTO = orderService.createNewOrder(ID);

        assertEquals(orderDTO.getState(), order.getState());
        assertEquals(orderDTO.getCreated(), order.getCreated());
        assertEquals(orderDTO.getCustomerUrl(), CustomerServiceImpl.getCustomerUrl(ID));

    }

    @Test
    public void deleteOrder() {

        final LocalDateTime created = LocalDateTime.now();
        final LocalDateTime updated = LocalDateTime.now();

        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname(FIRST_NAME);
        customer.setLastname(LAST_NAME);

        Order order = new Order();
        order.setId(1L);
        order.setState(OrderStatus.ORDERED);
        order.setCreated(created);
        order.setUpdated(updated);
        order.setCustomer(customer);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        orderService.deleteOrder(ID);

        Mockito.verify(orderRepository, times(1)).delete(any(Order.class));

    }

    @Test
    public void getOrdersByCustomer() {



    }

    @Test
    public void getOrderById() {
    }

    @Test
    public void addItemToOrder() {
    }

    @Test
    public void getListOfItems() {
    }

    @Test
    public void purchaseAction() {
    }

    @Test
    public void cancelAction() {
    }

    @Test
    public void deliverAction() {
    }

    @Test
    public void getItemFromOrder() {
    }

    @Test
    public void deleteItemFromOrder() {
    }
}