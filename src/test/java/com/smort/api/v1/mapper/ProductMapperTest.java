package com.smort.api.v1.mapper;

import com.smort.api.v1.model.ProductDTO;
import com.smort.domain.Product;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProductMapperTest {

    public static final Long ID = 1L;
    public static final String NAME = "Pomorandze";
    public static final Double PRICE = 60.56;

    ProductMapper productMapper = ProductMapper.INSTANCE;

    @Test
    public void productToProductDTO() {
        // given
        Product product = new Product();
        product.setId(ID);
        product.setName(NAME);
        product.setPrice(PRICE);

        // when
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());

    }

    @Test
    public void productDTOToProduct() {

        // given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(NAME);
        productDTO.setPrice(PRICE);

        // when
        Product product = productMapper.productDTOToProduct(productDTO);

        // then
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());

    }
}