package com.smort.services;

import com.smort.api.v1.model.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO findById(Long id);

    OrderDTO createNewOrder(Long customerId);



}
