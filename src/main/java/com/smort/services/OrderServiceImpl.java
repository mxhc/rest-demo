package com.smort.services;

import com.smort.api.v1.mapper.OrderMapper;
import com.smort.api.v1.model.*;
import com.smort.controllers.v1.OrderController;
import com.smort.controllers.v1.ProductController;
import com.smort.domain.*;
import com.smort.error.OrderStateException;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.OrderItemRepository;
import com.smort.repositories.OrderRepository;
import com.smort.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, CustomerRepository customerRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
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

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);

        int total = 0;

        for(OrderItem o: order.getItems()) {
            total += o.getQuantity() * o.getPrice();
        }

        orderDTO.setTotal(total);

        orderDTO.setItemsUrl(getItemsUrl(orderId));
        orderDTO.setCustomerUrl(CustomerServiceImpl.getCustomerUrl(order.getCustomer().getId()));

        List<ActionDTO> actionDTOS = new ArrayList<>();

        switch (orderDTO.getState()) {
            case CREATED:
                actionDTOS.add(createAction("purchase", orderId));
                break;
            case ORDERED:
                actionDTOS.add(createAction("cancel", orderId));
                actionDTOS.add(createAction("deliver", orderId));
                break;
            case CANCELED:
                break;
        }

        orderDTO.setActions(actionDTOS);

        return orderDTO;
    }

    private ActionDTO createAction(String action, Long id) {

        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setMethod("POST");
        actionDTO.setUrl(getOrderUrl(id) + "/" + action);

        return actionDTO;
    }

    @Override
    public OrderItemDTO addItemToOrder(Long orderId, OrderItemDTO orderItemDTO) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        Long productId = Long.valueOf(orderItemDTO.getProductUrl().split("/")[4]);

        Product product = productRepository.findById(productId).orElseThrow(ResourceNotFoundException::new);

        OrderItem orderItem = orderMapper.orderItemDTOToOrderItem(orderItemDTO);
        orderItem.setProduct(product);

        orderItem.setOrder(order);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        OrderItemDTO returnDTO = orderMapper.orderItemToOrderItemDTO(savedOrderItem);

        returnDTO.setProductUrl(orderItemDTO.getProductUrl());
        returnDTO.setItemUrl(getItemsUrl(orderId) + savedOrderItem.getId());
        returnDTO.setOrderUrl(getOrderUrl(orderId));

        return returnDTO;
    }

    @Override
    public OrderItemListDTO getListOfItema(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        OrderItemListDTO orderItemListDTO = new OrderItemListDTO(order.getItems().stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = orderMapper.orderItemToOrderItemDTO(orderItem);
            orderItemDTO.setItemUrl(getItemsUrl(orderId) + "/" + orderItem.getId());
            orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + orderItem.getProduct().getId());
            return orderItemDTO;
        }).collect(Collectors.toList()));

        orderItemListDTO.setOrderUrl(getOrderUrl(orderId));

        return orderItemListDTO;

    }

    @Override
    public OrderDTO purchaseAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        if (order.getState() == OrderStatus.RECEIVED) {
            throw new OrderStateException("Order already delivered");
        }

        if (order.getState() == OrderStatus.ORDERED || order.getState() == OrderStatus.CANCELED) {
            throw new OrderStateException("Must be in CREATED state to be purchased");
        }


        order.setState(OrderStatus.ORDERED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setCustomerUrl(CustomerServiceImpl.getCustomerUrl(order.getCustomer().getId()));
        orderDTO.setItemsUrl(getItemsUrl(orderId));
        orderDTO.setActions(Arrays.asList(createAction("cancel", orderId)));

        return orderDTO;
    }

    @Override
    public OrderDTO cancelAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        if (order.getState() == OrderStatus.RECEIVED) {
            throw new OrderStateException("Order already delivered");
        }

        if (order.getState() == OrderStatus.CREATED || order.getState() == OrderStatus.CANCELED) {
            throw new OrderStateException("Must be in ORDERED state to be canceled");
        }


        order.setState(OrderStatus.CANCELED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setCustomerUrl(CustomerServiceImpl.getCustomerUrl(order.getCustomer().getId()));
        orderDTO.setItemsUrl(getItemsUrl(orderId));

        return orderDTO;
    }

    @Override
    public OrderDTO deliverAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);

        if (order.getState() == OrderStatus.CANCELED || order.getState() == OrderStatus.CREATED) {
            throw new OrderStateException("Must be in ORDERED state to be delivered");
        }

        if (order.getState() == OrderStatus.RECEIVED) {
            throw new OrderStateException("Order already delivered");
        }

        order.setState(OrderStatus.RECEIVED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setCustomerUrl(CustomerServiceImpl.getCustomerUrl(order.getCustomer().getId()));
        orderDTO.setItemsUrl(getItemsUrl(orderId));

        return orderDTO;
    }


}
