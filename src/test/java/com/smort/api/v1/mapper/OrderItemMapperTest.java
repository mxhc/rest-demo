package com.smort.api.v1.mapper;

import com.smort.api.v1.model.OrderItemDTO;
import com.smort.domain.OrderItem;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderItemMapperTest {

    private static final Double PRICE = 50.5;
    private static final Integer QUANTITY = 10;

    OrderItemMapper orderItemMapper = OrderItemMapper.INSTANCE;

    @Test
    public void orderItemToOrderItemDTO() {

        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setPrice(PRICE);
        orderItem.setQuantity(QUANTITY);

        OrderItemDTO orderItemDTO = orderItemMapper.orderItemToOrderItemDTO(orderItem);

        assertEquals(orderItem.getPrice(), orderItemDTO.getPrice());
        assertEquals(orderItem.getQuantity(), orderItemDTO.getQuantity());

    }

    @Test
    public void orderItemDTOToOrder() {

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setPrice(PRICE);
        orderItemDTO.setQuantity(QUANTITY);

        OrderItem orderItem = orderItemMapper.orderItemDTOToOrder(orderItemDTO);

        assertEquals(orderItem.getPrice(), orderItemDTO.getPrice());
        assertEquals(orderItem.getQuantity(), orderItemDTO.getQuantity());
    }
}