package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    @NotBlank(message = "{product.name.blank}")
    @Size(min = 2, message = "{product.name.minsize}")
    @ApiModelProperty(value = "Product Name", required = true, example = "Jabuke", position = 0)
    private String name;

    @PositiveOrZero(message = "{product.price.negative}")
    @ApiModelProperty(value = "Product Price", required = true, example = "510.32", position = 1)
    private Double price;

    @Pattern(regexp = "^\\/api\\/v1\\/categories\\/[0-9a-zA-z]*", message = "{product.price.category.url}")
    @ApiModelProperty(example = "/api/v1/categories/Fruits", required = true, position = 2)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("category_url")
    private String categoryUrl;

    @Pattern(regexp = "^\\/api\\/v1\\/vendors\\/[0-9]*", message = "{product.price.vendor.url}")
    @ApiModelProperty(example = "/api/v1/vendors/2", required = true, position = 3)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("vendor_url")
    private String vendorUrl;

    @ApiModelProperty(example = "/api/v1/products/101", readOnly = true, position = 4)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "product_url")
    private String productUrl;

    @ApiModelProperty(example = "/api/v1/products/101/photo", readOnly = true, position = 5)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(value = "photo_url")
    private String photoUrl;

    public ProductDTO(String name, Double price, String productUrl) {
        this.name = name;
        this.price = price;
        this.productUrl = productUrl;
    }
}
