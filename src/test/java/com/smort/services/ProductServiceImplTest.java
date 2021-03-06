package com.smort.services;

import com.smort.api.v1.mapper.ProductMapper;
import com.smort.api.v1.model.ProductDTO;
import com.smort.controllers.v1.CategoryController;
import com.smort.controllers.v1.ProductController;
import com.smort.controllers.v1.VendorController;
import com.smort.domain.Category;
import com.smort.domain.Product;
import com.smort.domain.Vendor;
import com.smort.repositories.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    public static final Long ID = 2L;
    public static final String NAME = "Krompir";
    public static final Double PRICE = 27.3;

    ProductService productService;

    @Mock
    ProductRepository productRepository;
    @Mock
    VendorRepository vendorRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileRepository fileRepository;
    @Mock
    ProductRepositoryPaging productRepositoryPaging;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        productService = new ProductServiceImpl(productRepository, ProductMapper.INSTANCE, vendorRepository, categoryRepository, fileRepository, productRepositoryPaging);

    }

    @Test
    public void getAllProducts() {

        // given
        List<Product> products = Arrays.asList(new Product(1L, NAME, PRICE, getVendor(), getCategory()), new Product(ID, NAME, PRICE, getVendor(), getCategory()), new Product(3L, NAME, PRICE, getVendor(), getCategory()));

        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductDTO> productDTOS = productService.getAllProductsMeta().getProducts();

        // then
        assertEquals(3, productDTOS.size());

    }

    @Test
    public void findById() {

        Product product = new Product();
        product.setId(ID);
        product.setName(NAME);
        product.setPrice(PRICE);
        product.setVendor(getVendor());
        product.setCategory(getCategory());
        Optional<Product> optionalProduct = Optional.ofNullable(product);

        when(productRepository.findById(anyLong())).thenReturn(optionalProduct);

        // when
        ProductDTO productDTO = productService.findById(ID);

        // then
        assertEquals(NAME, productDTO.getName());
        assertEquals(PRICE, productDTO.getPrice());
        assertEquals(VendorController.BASE_URL + "/" + product.getVendor().getId(), productDTO.getVendorUrl());
        assertEquals(CategoryController.BASE_URL + "/" + product.getCategory().getName(), productDTO.getCategoryUrl());

    }

    @Test
    public void createNewProduct() {

        // given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(NAME);
        productDTO.setPrice(PRICE);
        productDTO.setVendorUrl(VendorController.BASE_URL + "/" + getVendor().getId());
        productDTO.setCategoryUrl(CategoryController.BASE_URL + "/" + getCategory().getName());

        Product savedProduct = new Product();
        savedProduct.setName(productDTO.getName());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setVendor(getVendor());
        savedProduct.setCategory(getCategory());
        savedProduct.setId(ID);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getVendor()));
        when(categoryRepository.findByName(anyString())).thenReturn(getCategory());

        // when
        ProductDTO savedDto = productService.createNewProduct(productDTO);

        // then
        assertEquals(productDTO.getName(), savedDto.getName());
        assertEquals(productDTO.getPrice(), savedDto.getPrice());
        assertEquals(productDTO.getCategoryUrl(), savedDto.getCategoryUrl());
        assertEquals(productDTO.getVendorUrl(), savedDto.getVendorUrl());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void saveProductByDTO() {

        // given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(NAME);
        productDTO.setPrice(PRICE);
        productDTO.setVendorUrl(VendorController.BASE_URL + "/" + getVendor().getId());
        productDTO.setCategoryUrl(CategoryController.BASE_URL + "/" + getCategory().getName());

        Product savedProduct = new Product();
        savedProduct.setName(productDTO.getName());
        savedProduct.setPrice(productDTO.getPrice());
        savedProduct.setVendor(getVendor());
        savedProduct.setCategory(getCategory());
        savedProduct.setId(ID);

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getVendor()));
        when(categoryRepository.findByName(anyString())).thenReturn(getCategory());

        // when
        ProductDTO savedDto = productService.saveProductByDTO(1L, productDTO);

        // then
        assertEquals(productDTO.getName(), savedDto.getName());
        assertEquals(productDTO.getPrice(), savedDto.getPrice());
        assertEquals(productDTO.getCategoryUrl(), savedDto.getCategoryUrl());
        assertEquals(productDTO.getVendorUrl(), savedDto.getVendorUrl());

        verify(productRepository, times(1)).save(any(Product.class));

    }

    @Test
    public void patchProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(NAME);
        productDTO.setPrice(311.25);
        productDTO.setVendorUrl(VendorController.BASE_URL + "/" + getVendor().getId());
        productDTO.setCategoryUrl(CategoryController.BASE_URL + "/" + getCategory().getName());

        Product savedProduct = new Product();
        savedProduct.setName(productDTO.getName());
        savedProduct.setPrice(PRICE);
        savedProduct.setVendor(getVendor());
        savedProduct.setCategory(getCategory());
        savedProduct.setId(ID);

        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(savedProduct));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.ofNullable(getVendor()));
        when(categoryRepository.findByName(anyString())).thenReturn(getCategory());

        ProductDTO savedDto = productService.patchProduct(ID, productDTO);

        assertEquals(productDTO.getVendorUrl(), savedDto.getVendorUrl());
        assertEquals(productDTO.getPrice(), savedDto.getPrice());
        assertEquals(productDTO.getCategoryUrl(), savedDto.getCategoryUrl());
        assertEquals(productDTO.getName(), savedDto.getName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(productRepository, times(1)).findById(anyLong());
        verify(vendorRepository, times(1)).findById(anyLong());
        verify(categoryRepository, times(1)).findByName(anyString());

    }

    @Test
    public void deleteProductById() {
        productService.deleteProductById(ID);

        verify(productRepository, times(1)).deleteById(anyLong());
    }

    public Vendor getVendor() {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName("Buba");

        return vendor;
    }

    public Category getCategory() {
        Category category = new Category();
        category.setName("Voce");

        return category;
    }

    @Test
    public void convertListToDto() {

        // given
        Product product = new Product();
        product.setName("Banane");
        product.setCategory(getCategory());
        product.setVendor(getVendor());
        product.setPrice(PRICE);
        product.setId(ID);

        List<Product> products = Arrays.asList(new Product(), product, new Product(), new Product());

        List<ProductDTO> productDTOS = productService.convertListToDto(products);

        assertEquals(products.get(1).getName(),productDTOS.get(1).getName());
        assertEquals(products.get(1).getPrice(), productDTOS.get(1).getPrice());
        assertEquals(ProductController.BASE_URL + "/" + ID, productDTOS.get(1).getProductUrl());


    }
}