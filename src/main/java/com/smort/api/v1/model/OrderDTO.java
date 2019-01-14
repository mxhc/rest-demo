package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smort.controllers.v1.CustomerController;
import com.smort.controllers.v1.OrderController;
import com.smort.domain.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    @ApiModelProperty(value = "Status of Order")
    OrderStatus state;

    @ApiModelProperty(value = "Date created", readOnly = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime created;

    @ApiModelProperty(value = "Date updated", readOnly = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime updated;

    @ApiModelProperty(value = "Customer url", hidden = true, example = CustomerController.BASE_URL + "/10")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "customer_url")
    private String customerUrl;

    @ApiModelProperty(value = "Items url", hidden = true, example = OrderController.BASE_URL + "/10/items/14")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "items_url")
    private String itemsUrl;

    @ApiModelProperty(value = "Order url", readOnly = true, example = OrderController.BASE_URL + "/21")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order_url")
    private String orderUrl;


    @ApiModelProperty(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ActionDTO> actions;
}
