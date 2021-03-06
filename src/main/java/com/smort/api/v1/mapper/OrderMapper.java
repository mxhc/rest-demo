package com.smort.api.v1.mapper;

import com.smort.api.v1.model.OrderDTO;
import com.smort.api.v1.model.OrderItemDTO;
import com.smort.domain.Order;
import com.smort.domain.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderDTO orderToOrderDTO(Order order);

    Order orderDTOToOrder(OrderDTO orderDTO);

    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);
}
