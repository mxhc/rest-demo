package com.smort.api.v1.mapper;

import com.smort.api.v1.model.OrderItemDTO;
import com.smort.domain.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    OrderItem orderItemDTOToOrder(OrderItemDTO orderItemDTO);

}
