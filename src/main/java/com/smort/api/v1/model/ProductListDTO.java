package com.smort.api.v1.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductsMetaDTO meta;

    private List<ProductDTO> products;

    public ProductListDTO(List<ProductDTO> products) {
        this.products = products;
    }
}
