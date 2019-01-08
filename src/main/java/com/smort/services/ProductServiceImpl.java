package com.smort.services;

import com.smort.api.v1.mapper.ProductMapper;
import com.smort.api.v1.model.ProductDTO;
import com.smort.controllers.v1.CategoryController;
import com.smort.controllers.v1.ProductController;
import com.smort.controllers.v1.VendorController;
import com.smort.domain.Product;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CategoryRepository;
import com.smort.repositories.ProductRepository;
import com.smort.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> {
                    ProductDTO productDTO = convertToDTOAndAddProductUrl(product);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        ProductDTO productDTO = convertToDTOAndAddUrls(product);

        return productDTO;

    }

    @Override
    public ProductDTO createNewProduct(ProductDTO productDTO) {

        Product product = convertToProductAndSetVendorAndCategory(productDTO);

        Product savedProduct = productRepository.save(product);

        ProductDTO returnDto = convertToDTOAndAddUrls(savedProduct);

        return returnDto;
    }

    @Override
    public ProductDTO saveProductByDTO(Long id, ProductDTO productDTO) {
        Product product = convertToProductAndSetVendorAndCategory(productDTO);

        product.setId(id);

        Product savedProduct = productRepository.save(product);

        return convertToDTOAndAddUrls(savedProduct);
    }


    @Override
    public ProductDTO patchProduct(Long id, ProductDTO productDTO) {
        return productRepository.findById(id).map(product -> {
            if (productDTO.getName() != null) {
                product.setName(productDTO.getName());
            }

            if (productDTO.getPrice() != null) {
                product.setPrice(productDTO.getPrice());
            }

            if (productDTO.getVendorUrl() != null) {
                product.setVendor(vendorRepository.findById(extractVendorIdFromUrl(productDTO)).orElseThrow(ResourceNotFoundException::new));
            }

            if (productDTO.getCategoryUrl() != null) {
                product.setCategory(categoryRepository.findByName(extractCategoryNameFromUrl(productDTO)));
            }

            Product savedProduct = productRepository.save(product);

            ProductDTO returnDTO = convertToDTOAndAddUrls(savedProduct);

            return returnDTO;

        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    private String getVendorUrl(Long id) {
        return VendorController.BASE_URL + "/" + id;
    }

    private String getCategoryUrl(String categoryName) {
        return CategoryController.BASE_URL + "/" + categoryName;
    }

    private ProductDTO convertToDTOAndAddUrls(Product product) {
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        productDTO.setVendorUrl(getVendorUrl(product.getVendor().getId()));
        productDTO.setCategoryUrl(getCategoryUrl(product.getCategory().getName()));

        return productDTO;
    }

    private Product convertToProductAndSetVendorAndCategory(ProductDTO productDTO) {
        Long vendorId = extractVendorIdFromUrl(productDTO);

        String categoryName = extractCategoryNameFromUrl(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);

        product.setVendor(vendorRepository.findById(vendorId).orElseThrow(ResourceNotFoundException::new));
        product.setCategory(categoryRepository.findByName(categoryName));

        return product;
    }

    private String extractCategoryNameFromUrl(ProductDTO productDTO) {
        String[] tempArray;
        tempArray = productDTO.getCategoryUrl().split("/");
        return tempArray[tempArray.length - 1];
    }

    private Long extractVendorIdFromUrl(ProductDTO productDTO) {
        String[] tempArray = productDTO.getVendorUrl().split("/");
        return Long.valueOf(tempArray[tempArray.length - 1]);
    }

    private ProductDTO convertToDTOAndAddProductUrl(Product product) {
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        productDTO.setProductUrl(ProductController.BASE_URL + "/" + product.getId());

        return productDTO;
    }

}
