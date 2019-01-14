package com.smort.services;

import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderItemDTO;
import com.smort.api.v1.model.OrderItemListDTO;
import com.smort.api.v1.model.OrderListDTO;
import com.smort.domain.OrderStatus;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders(OrderStatus orderStatus);

    OrderDTO findById(Long id);

    OrderDTO createNewOrder(Long customerId);

    void deleteOrder(Long id);

    OrderListDTO getOrdersByCustomer(Long customerId);

    OrderDTO getOrderById(Long orderId);

    OrderItemDTO addItemToOrder(Long orderId, OrderItemDTO orderItemDTO);

    OrderItemListDTO getListOfItems(Long orderId);

    OrderDTO purchaseAction(Long orderId);

    OrderDTO cancelAction(Long orderId);

    OrderDTO deliverAction(Long orderId);
}
