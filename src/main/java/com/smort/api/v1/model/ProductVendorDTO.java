package com.smort.api.v1.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVendorDTO {

    @ApiModelProperty(value = "Product Name", required = true, example = "Jabuke", position = 0)
    private String name;

    @ApiModelProperty(value = "Product Price", required = true, example = "510.32", position = 1)
    private Double price;

    @ApiModelProperty(value = "Category Name", required = true, example = "Fruits", position = 2)
    private String categoryName;

}
