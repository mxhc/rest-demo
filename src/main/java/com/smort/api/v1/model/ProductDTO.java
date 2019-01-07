package com.smort.api.v1.model;

import lombok.Data;

@Data
public class ProductDTO {

    private String name;
    private Double price;
    private String categoryUrl;
    private String vendorUrl;


}
