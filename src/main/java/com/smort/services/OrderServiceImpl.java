package com.smort.services;

import com.smort.api.v1.mapper.OrderMapper;
import com.smort.api.v1.model.ActionDTO;
import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.controllers.v1.OrderController;
import com.smort.domain.Customer;
import com.smort.domain.Order;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> {
                    OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
                    orderDTO.setUpdated(null);
                    orderDTO.setOrderUrl(getOrderUrl(order.getId()));
                    return orderDTO;
                }).collect(Collectors.toList());
    }

    private String getOrderUrl(Long id) {
        return OrderController.BASE_URL + "/" + id;
    }

    private String getItemsUrl(Long orderId) {
        return OrderController.BASE_URL + "/" + orderId + "/items/";
    }

    @Override
    public OrderDTO findById(Long id) {
        return null;
    }

    @Override
    public OrderDTO createNewOrder(Long customerId) {

        Order order = new Order();
        order.setCustomer(customerRepository.findById(customerId).orElseThrow(ResourceNotFoundException::new));

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setCustomerUrl(CustomerServiceImpl.getCustomerUrl(customerId));
        orderDTO.setItemsUrl(getItemsUrl(savedOrder.getId()));

        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setUrl(getOrderUrl(savedOrder.getId()) + "/purchase");
        actionDTO.setMethod("POST");

        List<ActionDTO> actionDTOS = Arrays.asList(actionDTO);

        orderDTO.setActions(actionDTOS);

        return orderDTO;
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.delete(orderRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    @Override
    public OrderListDTO getOrdersByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(ResourceNotFoundException::new);

        List<Order> orders = customer.getOrders();

        return new OrderListDTO(orders.stream().map(order -> {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            orderDTO.setOrderUrl(getOrderUrl(order.getId()));
            orderDTO.setUpdated(null);
            return orderDTO;
        }).collect(Collectors.toList()));

    }

}
