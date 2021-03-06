package com.smort.services;

import com.smort.api.v1.mapper.OrderMapper;
import com.smort.api.v1.model.*;
import com.smort.controllers.v1.ProductController;
import com.smort.domain.*;
import com.smort.error.OrderStateException;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.OrderItemRepository;
import com.smort.repositories.OrderRepository;
import com.smort.repositories.ProductRepository;
import com.smort.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper, ProductRepository productRepository, OrderItemRepository orderItemRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }


    @Override
    public List<OrderDTO> getAllOrders(OrderStatus orderStatus) {

        List<OrderDTO> orderDTOS;

        if(orderStatus != null) {

            orderDTOS = orderRepository.findAllByState(orderStatus)
                    .stream()
                    .map(order -> {
                        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
                        orderDTO.setUpdated(null);
                        orderDTO.setOrderUrl(UrlBuilder.getOrderUrl(order.getId()));
                        return orderDTO;
                    }).collect(Collectors.toList());

        } else {
            orderDTOS = orderRepository.findAll()
                    .stream()
                    .map(order -> {
                        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
                        orderDTO.setUpdated(null);
                        orderDTO.setOrderUrl(UrlBuilder.getOrderUrl(order.getId()));
                        return orderDTO;
                    }).collect(Collectors.toList());
        }
        return orderDTOS;
    }


    @Override
    public OrderDTO findById(Long id) {

        Order order = orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + id + " not found"));

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);

        orderDTO.setUserUrl(UrlBuilder.getUserUrl(order.getUser().getId()));
        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(id));

        List<ActionDTO> actionDTOS = new ArrayList<>();

        switch (orderDTO.getState()) {
            case CREATED:
                actionDTOS.add(createAction("purchase", id));
                break;
            case ORDERED:
                actionDTOS.add(createAction("cancel", id));
                actionDTOS.add(createAction("deliver", id));
                break;
            case CANCELED:
                break;
        }

        orderDTO.setActions(actionDTOS);

        return orderDTO;
    }


    @Transactional
    @Override
    public OrderDTO createNewOrder(Long userId) {

        Order order = new Order();
        order.setUser(userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with id: " + userId + " not found")));

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setUserUrl(UrlBuilder.getUserUrl(userId));
        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(savedOrder.getId()));

        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setUrl(UrlBuilder.getOrderUrl(savedOrder.getId()) + "/purchase");
        actionDTO.setMethod("POST");

        List<ActionDTO> actionDTOS = Arrays.asList(actionDTO);

        orderDTO.setActions(actionDTOS);

        return orderDTO;
    }


    @Transactional
    @Override
    public void deleteOrder(Long id) {
        orderRepository.delete(orderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + id + " not found")));
    }


    @Override
    public OrderListDTO getOrdersByUser(Long userId) {
        UserInfo user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with id: " + userId + " not found"));

        List<Order> orders = user.getOrders();

        return new OrderListDTO(orders.stream().map(order -> {
            OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);
            orderDTO.setOrderUrl(UrlBuilder.getOrderUrl(order.getId()));
            orderDTO.setUpdated(null);
            return orderDTO;
        }).collect(Collectors.toList()));

    }


    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);

        orderDTO.setTotal(getOrderTotal(order));

        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(orderId));
        orderDTO.setUserUrl(UrlBuilder.getUserUrl(order.getUser().getId()));

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


    @Transactional
    @Override
    public OrderItemDTO addItemToOrder(Long orderId, OrderItemDTO orderItemDTO) {

        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        notEqualThrowsException("Must be in CREATED state to add items", OrderStatus.CREATED, order);

        String[] tempArray = orderItemDTO.getProductUrl().split("/");

        Long productId = Long.valueOf(tempArray[tempArray.length-1]);

        Product product = productRepository.findById(productId).orElseThrow(()-> new ResourceNotFoundException("Product with id: " + productId + " not found"));

        OrderItem orderItem = orderMapper.orderItemDTOToOrderItem(orderItemDTO);
        orderItem.setProduct(product);

        orderItem.setOrder(order);

        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        OrderItemDTO returnDTO = orderMapper.orderItemToOrderItemDTO(savedOrderItem);

        returnDTO.setProductUrl(orderItemDTO.getProductUrl());
        returnDTO.setItemUrl(UrlBuilder.getItemsUrl(orderId) + savedOrderItem.getId());
        returnDTO.setOrderUrl(UrlBuilder.getOrderUrl(orderId));

        return returnDTO;
    }


    @Override
    public OrderItemListDTO getListOfItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        OrderItemListDTO orderItemListDTO = new OrderItemListDTO(order.getItems().stream().map(orderItem -> {
            OrderItemDTO orderItemDTO = orderMapper.orderItemToOrderItemDTO(orderItem);
            orderItemDTO.setItemUrl(UrlBuilder.getItemsUrl(orderId) + orderItem.getId());
            orderItemDTO.setProductUrl(ProductController.BASE_URL + "/" + orderItem.getProduct().getId());
            return orderItemDTO;
        }).collect(Collectors.toList()));

        orderItemListDTO.setOrderUrl(UrlBuilder.getOrderUrl(orderId));

        return orderItemListDTO;

    }


    @Transactional
    @Override
    public OrderDTO purchaseAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        equalThrowsException("Order already delivered", OrderStatus.RECEIVED, order);

        notEqualThrowsException("Must be in CREATED state to be purchased", OrderStatus.CREATED, order);

        order.setState(OrderStatus.ORDERED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setUserUrl(UrlBuilder.getUserUrl(order.getUser().getId()));
        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(orderId));
        orderDTO.setActions(Arrays.asList(createAction("cancel", orderId)));

        return orderDTO;
    }


    @Transactional
    @Override
    public OrderDTO cancelAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        equalThrowsException("Order already delivered", OrderStatus.RECEIVED, order);

        notEqualThrowsException("Must be in CREATED state to be canceled", OrderStatus.CREATED, order);

        order.setState(OrderStatus.CANCELED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setUserUrl(UrlBuilder.getUserUrl(order.getUser().getId()));
        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(orderId));

        return orderDTO;
    }


    @Transactional
    @Override
    public OrderDTO deliverAction(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("Order with id: " + orderId + " not found"));

        notEqualThrowsException("Must be in ORDERED state to be delivered", OrderStatus.ORDERED, order);

        equalThrowsException("Order already delivered", OrderStatus.RECEIVED, order);

        order.setState(OrderStatus.RECEIVED);

        Order savedOrder = orderRepository.save(order);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(savedOrder);

        orderDTO.setUserUrl(UrlBuilder.getUserUrl(order.getUser().getId()));
        orderDTO.setItemsUrl(UrlBuilder.getItemsUrl(orderId));

        return orderDTO;
    }


    @Override
    public OrderItemDTO getItemFromOrder(Long oid, Long iid) {

        OrderItem orderItem = Optional.ofNullable(orderItemRepository.findByIdAndOrderId(iid, oid))
                .orElseThrow(()-> new ResourceNotFoundException("OrderItem with oid: " + oid + " and iid " + iid + " not found"));

        OrderItemDTO orderItemDTO = orderMapper.orderItemToOrderItemDTO(orderItem);

        orderItemDTO.setProductUrl(UrlBuilder.getProductUrl(orderItem.getProduct().getId()));
        orderItemDTO.setOrderUrl(UrlBuilder.getOrderUrl(oid));
        orderItemDTO.setItemUrl(UrlBuilder.getItemsUrl(oid) + iid);

        return orderItemDTO;
    }


    @Transactional
    @Override
    public void deleteItemFromOrder(Long oid, Long iid) {
        OrderItem orderItem = Optional.ofNullable(orderItemRepository.findByIdAndOrderId(iid, oid)).orElseThrow(()-> new ResourceNotFoundException("OrderItem with oid: " + oid + " and iid " + iid + " not found"));

        orderItemRepository.delete(orderItem);
    }

    private boolean notEqualThrowsException(String message, OrderStatus orderStatus, Order order) {
        if (!order.getState().equals(orderStatus)) {
            throw new OrderStateException(message);
        }
        return true;
    }

    private boolean equalThrowsException(String message, OrderStatus orderStatus, Order order) {
        if (order.getState().equals(orderStatus)) {
            throw new OrderStateException(message);
        }
        return false;
    }

    private ActionDTO createAction(String action, Long id) {

        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setMethod("POST");
        actionDTO.setUrl(UrlBuilder.getOrderUrl(id) + "/" + action);

        return actionDTO;
    }

    private Double getOrderTotal(Order order) {
        Double total = 0.0;
        for(OrderItem o: order.getItems()) {
            total += o.getQuantity() * o.getPrice();
        }
        return total;
    }


}
