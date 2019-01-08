package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @ApiModelProperty(value = "Product Name", required = true)
    private String name;
    @ApiModelProperty(value = "Product Price", required = true)
    private Double price;
    @JsonProperty("category_url")
    private String categoryUrl;
    @JsonProperty("vendor_url")
    private String vendorUrl;


}
