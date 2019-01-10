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

    @ApiModelProperty(value = "Product Name", required = true, example = "Jabuke", position = 0)
    private String name;

    @ApiModelProperty(value = "Product Price", required = true, example = "510.32", position = 1)
    private Double price;



    @ApiModelProperty(example = "/shop/categories/Fruits", required = true, position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category_url")
    private String categoryUrl;

    @ApiModelProperty(example = "/shop/vendors/2", required = true, position = 3)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("vendor_url")
    private String vendorUrl;

    @ApiModelProperty(example = "/api/v1/products/101", readOnly = true, position = 4)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "product_url")
    private String productUrl;

    public ProductDTO(String name, Double price, String productUrl) {
        this.name = name;
        this.price = price;
        this.productUrl = productUrl;
    }
}
