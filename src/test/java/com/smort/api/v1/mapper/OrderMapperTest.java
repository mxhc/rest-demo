package com.smort.api.v1.mapper;

import com.smort.api.v1.model.OrderDTO;
import com.smort.domain.Order;
import com.smort.domain.OrderStatus;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class OrderMapperTest {

    public static final Long ID = 1L;
    public static final OrderStatus STATE = OrderStatus.CREATED;
    public static final LocalDateTime CREATED = LocalDateTime.now();
    public static final LocalDateTime UPDATED = LocalDateTime.now();

    OrderMapper orderMapper = OrderMapper.INSTANCE;

    @Test
    public void orderToOrderDTO() {
        Order order = new Order();
        order.setId(ID);
        order.setState(STATE);
        order.setCreated(CREATED);
        order.setUpdated(UPDATED);

        OrderDTO orderDTO = orderMapper.orderToOrderDTO(order);

        assertEquals(order.getState(), orderDTO.getState());
        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(order.getUpdated(), orderDTO.getUpdated());

    }

    @Test
    public void orderDTOToOrder() {

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setState(STATE);
        orderDTO.setCreated(CREATED);
        orderDTO.setUpdated(UPDATED);

        Order order = orderMapper.orderDTOToOrder(orderDTO);

        assertEquals(order.getState(), orderDTO.getState());
        assertEquals(order.getCreated(), orderDTO.getCreated());
        assertEquals(order.getUpdated(), orderDTO.getUpdated());

    }
}