package com.smort.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderListDTO {

    private List<OrderDTO> orders;

}
