package com.smort.services;

import com.smort.api.v1.model.ProductDTO;
import com.smort.domain.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO findById(Long id);

    ProductDTO createNewProduct(ProductDTO productDTO);

    ProductDTO saveProductByDTO(Long id, ProductDTO productDTO);

    ProductDTO patchProduct(Long id, ProductDTO productDTO);

    void deleteProductById(Long id);

    List<ProductDTO> convertListToDto(List<Product> products);



}
