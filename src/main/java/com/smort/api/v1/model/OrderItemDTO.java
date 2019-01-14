package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

    @ApiModelProperty(example = "50", value = "Item quantity")
    @NotNull(message = "{item.quantity.null}")
    @Positive(message = "{item.quantity.negative}")
    private Integer quantity;

    @ApiModelProperty(example = "650.35", value = "Item price")
    @NotNull(message = "{item.price.null}")
    @Positive(message = "{item.price.negative}")
    private Double price;

    @ApiModelProperty(example = "/api/v1/shop/orders/342", value = "Order url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "order_url")
    private String orderUrl;

    @ApiModelProperty(example = "/api/v1/products/101", value = "Product url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "product_url")
    private String productUrl;

    @ApiModelProperty(example = "/api/v1/shop/orders/342/items/31", value = "Item url")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "item_url")
    private String itemUrl;

}
