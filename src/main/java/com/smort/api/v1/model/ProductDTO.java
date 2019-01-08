package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @ApiModelProperty(value = "Product Name", required = true, example = "Jabuke")
    private String name;

    @ApiModelProperty(value = "Product Price", required = true, example = "510.32")
    private Double price;

    @ApiModelProperty(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("product_url")
    private String productUrl;

    @ApiModelProperty(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category_url")
    private String categoryUrl;

    @ApiModelProperty(hidden = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("vendor_url")
    private String vendorUrl;

    public ProductDTO(String name, Double price, String productUrl) {
        this.name = name;
        this.price = price;
        this.productUrl = productUrl;
    }
}
