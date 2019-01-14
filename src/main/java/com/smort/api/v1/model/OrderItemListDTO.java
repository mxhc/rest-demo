package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class OrderItemListDTO {

    @JsonProperty(value = "order_url")
    private String orderUrl;

    private List<OrderItemDTO> items;

    public OrderItemListDTO(List<OrderItemDTO> items) {
        this.items = items;
    }

}
