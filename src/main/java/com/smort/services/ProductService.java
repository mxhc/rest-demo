package com.smort.services;

import com.smort.api.v1.model.ProductDTO;
import com.smort.api.v1.model.ProductListDTO;
import com.smort.domain.File;
import com.smort.domain.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDTO findById(Long id);

    ProductDTO createNewProduct(ProductDTO productDTO);

    ProductDTO saveProductByDTO(Long id, ProductDTO productDTO);

    ProductDTO patchProduct(Long id, ProductDTO productDTO);

    void deleteProductById(Long id);

    List<ProductDTO> convertListToDto(List<Product> products);

    ProductDTO uploadPhoto(Long id, MultipartFile productPhoto) throws IOException;

    File getImageByProductId(Long id);

    ProductListDTO getAllProductsPaginated(Integer page, int limit);

    ProductListDTO getAllProductsMeta();

}
