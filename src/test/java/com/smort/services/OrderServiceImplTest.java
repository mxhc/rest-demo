package com.smort.services;

import com.smort.api.v1.mapper.OrderMapper;
import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderItemDTO;
import com.smort.api.v1.model.OrderItemListDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.controllers.v1.CustomerController;
import com.smort.controllers.v1.OrderController;
import com.smort.controllers.v1.ProductController;
import com.smort.domain.*;
import com.smort.error.OrderStateException;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

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
        assertEquals(orderDTO.getCustomerUrl(), UrlBuilder.getCustomerUrl(ID));

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
        Customer c1 = getCustomer1();

        Long customerId = c1.getId();
        Long oId1 = c1.getOrders().get(0).getId();
        Long oId2 = c1.getOrders().get(1).getId();


        Order o1 = c1.getOrders().get(0);
        Order o2 = c1.getOrders().get(1);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.ofNullable(c1));

        OrderListDTO orderListDTO = orderService.getOrdersByCustomer(1L);

        assertEquals(o1.getState(), orderListDTO.getOrders().get(0).getState());
        assertEquals(o2.getState(), orderListDTO.getOrders().get(1).getState());
        assertEquals(OrderController.BASE_URL + "/" + oId1, orderListDTO.getOrders().get(0).getOrderUrl());
        assertEquals(OrderController.BASE_URL + "/" + oId2, orderListDTO.getOrders().get(1).getOrderUrl());
        assertEquals(c1.getOrders().size(), orderListDTO.getOrders().size());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getOrderById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(1L, customer, OrderStatus.ORDERED);

        OrderItem oi1 = new OrderItem();
        oi1.setId(10L);
        oi1.setQuantity(10);
        oi1.setPrice(5.0);
        oi1.setProduct(getProduct1());
        oi1.setOrder(order);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        order.setItems(Arrays.asList(oi1, oi2));

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        OrderDTO orderDTO = orderService.getOrderById(1L);

        verify(orderRepository, times(1)).findById(anyLong());

        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(order.getUpdated(), orderDTO.getUpdated());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/cancel", orderDTO.getActions().get(0).getUrl());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/deliver", orderDTO.getActions().get(1).getUrl());
        assertEquals("POST", orderDTO.getActions().get(1).getMethod());
        assertEquals(OrderStatus.ORDERED, orderDTO.getState());
        assertEquals(CustomerController.BASE_URL + "/" + order.getCustomer().getId(), orderDTO.getCustomerUrl());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/items/", orderDTO.getItemsUrl());
        assertEquals(Double.valueOf(100.0), orderDTO.getTotal());
    }

    @Test
    public void addItemToOrder() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(1L, customer, OrderStatus.CREATED);

        OrderItem oi1 = new OrderItem();
        oi1.setId(10L);
        oi1.setQuantity(10);
        oi1.setPrice(5.0);
        oi1.setProduct(getProduct1());
        oi1.setOrder(order);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderUrl(OrderController.BASE_URL + "/" + order.getId());
        orderItemDTO.setPrice(oi2.getPrice());
        orderItemDTO.setQuantity(oi2.getQuantity());
        orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + oi2.getProduct().getId());

        order.setItems(Arrays.asList(oi1));

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProduct2()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(oi2);

        OrderItemDTO returnDTO = orderService.addItemToOrder(order.getId(), orderItemDTO);

        assertEquals(returnDTO.getPrice(), oi2.getPrice());
        assertEquals(returnDTO.getQuantity(), oi2.getQuantity());
        assertEquals(returnDTO.getOrderUrl(), OrderController.BASE_URL + "/" + order.getId());

    }

    @Test(expected = OrderStateException.class)
    public void addItemToOrderInvalidState() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(1L, customer, OrderStatus.CANCELED);

        OrderItem oi1 = new OrderItem();
        oi1.setId(10L);
        oi1.setQuantity(10);
        oi1.setPrice(5.0);
        oi1.setProduct(getProduct1());
        oi1.setOrder(order);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderUrl(OrderController.BASE_URL + "/" + order.getId());
        orderItemDTO.setPrice(oi2.getPrice());
        orderItemDTO.setQuantity(oi2.getQuantity());
        orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + oi2.getProduct().getId());

        order.setItems(Arrays.asList(oi1));

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProduct2()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(oi2);

        OrderItemDTO returnDTO = orderService.addItemToOrder(order.getId(), orderItemDTO);

    }

    @Test(expected = OrderStateException.class)
    public void addItemToOrderInvalidStateRecieved() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(1L, customer, OrderStatus.RECEIVED);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderUrl(OrderController.BASE_URL + "/" + order.getId());
        orderItemDTO.setPrice(oi2.getPrice());
        orderItemDTO.setQuantity(oi2.getQuantity());
        orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + oi2.getProduct().getId());


        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProduct2()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(oi2);

        OrderItemDTO returnDTO = orderService.addItemToOrder(order.getId(), orderItemDTO);
    }

    @Test(expected = OrderStateException.class)
    public void addItemToOrderInvalidStatePurchased() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(1L, customer, OrderStatus.ORDERED);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setOrderUrl(OrderController.BASE_URL + "/" + order.getId());
        orderItemDTO.setPrice(oi2.getPrice());
        orderItemDTO.setQuantity(oi2.getQuantity());
        orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + oi2.getProduct().getId());


        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getProduct2()));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(oi2);

        OrderItemDTO returnDTO = orderService.addItemToOrder(order.getId(), orderItemDTO);
    }

    @Test
    public void getListOfItems() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CREATED);

        OrderItem oi1 = new OrderItem();
        oi1.setId(10L);
        oi1.setQuantity(10);
        oi1.setPrice(5.0);
        oi1.setProduct(getProduct1());
        oi1.setOrder(order);

        OrderItem oi2 = new OrderItem();
        oi2.setId(15L);
        oi2.setQuantity(5);
        oi2.setPrice(10.0);
        oi2.setProduct(getProduct2());
        oi2.setOrder(order);

        order.setItems(Arrays.asList(oi1, oi2));

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));

        OrderItemListDTO orderItemListDTO = orderService.getListOfItems(110L);

        assertEquals(2, orderItemListDTO.getItems().size());
        assertEquals(oi1.getQuantity(), orderItemListDTO.getItems().get(0).getQuantity());
        assertEquals(oi2.getQuantity(), orderItemListDTO.getItems().get(1).getQuantity());
        assertEquals(OrderController.BASE_URL + "/" + order.getId(), orderItemListDTO.getOrderUrl());
        assertEquals(ProductController.BASE_URL + "/" + oi2.getProduct().getId(), orderItemListDTO.getItems().get(1).getProductUrl());
    }

    @Test(expected = OrderStateException.class)
    public void purchaseAction() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CREATED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.purchaseAction(110L);

        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(OrderStatus.ORDERED, orderDTO.getState());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/cancel", orderDTO.getActions().get(0).getUrl());
        assertEquals(CustomerController.BASE_URL + "/" + order.getCustomer().getId(), orderDTO.getCustomerUrl());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/items/", orderDTO.getItemsUrl());

        order.setState(OrderStatus.ORDERED);

        orderDTO = orderService.purchaseAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void purchaseActionCanceled() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CANCELED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.purchaseAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void purchaseActionDelivered() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.RECEIVED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.purchaseAction(110L);
    }



    @Test
    public void cancelAction() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CREATED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.cancelAction(110L);

        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(OrderStatus.CANCELED, orderDTO.getState());
        assertEquals(CustomerController.BASE_URL + "/" + order.getCustomer().getId(), orderDTO.getCustomerUrl());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/items/", orderDTO.getItemsUrl());
    }

    @Test(expected = OrderStateException.class)
    public void cancelActionDelivered() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.RECEIVED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.cancelAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void cancelActionCanceled() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CANCELED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.cancelAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void cancelActionOrdered() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.ORDERED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.cancelAction(110L);
    }

    @Test
    public void deliverAction() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.ORDERED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.deliverAction(110L);

        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(OrderStatus.RECEIVED, orderDTO.getState());
        assertEquals(CustomerController.BASE_URL + "/" + order.getCustomer().getId(), orderDTO.getCustomerUrl());
        assertEquals(OrderController.BASE_URL + "/" + order.getId() + "/items/", orderDTO.getItemsUrl());
    }

    @Test(expected = OrderStateException.class)
    public void deliverActionDelivered() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.RECEIVED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.deliverAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void deliverActionCanceled() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CANCELED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.deliverAction(110L);
    }

    @Test(expected = OrderStateException.class)
    public void deliverActionCreated() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CREATED);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(order));
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO orderDTO = orderService.deliverAction(110L);
    }

    @Test
    public void getItemFromOrder() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        Order order = getOrderByCustomer(110L, customer, OrderStatus.CREATED);

        OrderItem oi1 = new OrderItem();
        oi1.setId(10L);
        oi1.setQuantity(10);
        oi1.setPrice(5.0);
        oi1.setProduct(getProduct1());
        oi1.setOrder(order);

        when(orderItemRepository.findByIdAndOrderId(anyLong(), anyLong())).thenReturn(oi1);

        OrderItemDTO orderItemDTO = orderService.getItemFromOrder(order.getId(), oi1.getId());

        assertEquals(ProductController.BASE_URL + "/" + oi1.getProduct().getId(), orderItemDTO.getProductUrl());
        assertEquals(OrderController.BASE_URL + "/" + oi1.getOrder().getId(), orderItemDTO.getOrderUrl());
        assertEquals(OrderController.BASE_URL + "/" + oi1.getOrder().getId() + "/items/" + oi1.getId(), orderItemDTO.getItemUrl());

    }

    @Test
    public void deleteItemFromOrder() {

        OrderItem orderItem = new OrderItem();

        when(orderItemRepository.findByIdAndOrderId(anyLong(), anyLong())).thenReturn(orderItem);

        orderService.deleteItemFromOrder(anyLong(), anyLong());

        then(orderItemRepository).should().delete(orderItem);

    }

    private Customer getCustomer1() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setFirstname("Milojko");
        customer.setLastname("Pantic");

        customer.setOrders(Arrays.asList(getOrderByCustomer(11L, customer, OrderStatus.CREATED), getOrderByCustomer(25L, customer, OrderStatus.RECEIVED)));

        return customer;
    }

    private Customer getCustomer2() {
        Customer customer = new Customer();
        customer.setId(65L);
        customer.setFirstname("Oliver");
        customer.setLastname("Mlakar");
        customer.setOrders(Arrays.asList(getOrderByCustomer(1L, customer, OrderStatus.ORDERED), getOrderByCustomer(2L, customer, OrderStatus.RECEIVED)));

        return customer;
    }

    private Order getOrderByCustomer(Long id, Customer customer, OrderStatus state) {
        Order order = new Order();
        order.setId(id);
        order.setState(state);
        order.setCreated(LocalDateTime.now());
        order.setUpdated(LocalDateTime.now());
        order.setCustomer(customer);

        return order;
    }

    private Product getProduct1() {
        Product product = new Product();
        product.setName("Banane");
        product.setCategory(getCategory1());
        product.setVendor(getVendor1());
        product.setPrice(555.55);
        product.setId(10L);
        return product;
    }

    private Product getProduct2() {
        Product product = new Product();
        product.setName("Jabuke");
        product.setCategory(getCategory2());
        product.setVendor(getVendor2());
        product.setPrice(65.55);
        product.setId(1002L);
        return product;
    }

    private Vendor getVendor1() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Buba");
        return vendor;
    }

    private Vendor getVendor2() {
        Vendor vendor = new Vendor();
        vendor.setId(3L);
        vendor.setName("Sveze d.o.o.");
        return vendor;
    }

    private Category getCategory1() {
        Category category = new Category();
        category.setName("Voce");
        category.setId(5L);
        return category;
    }

    private Category getCategory2() {
        Category category = new Category();
        category.setName("Povrce");
        category.setId(3L);
        return category;
    }

}