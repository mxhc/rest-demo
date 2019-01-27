package com.smort.services;

import com.smort.api.v1.mapper.ProductMapper;
import com.smort.api.v1.model.ProductDTO;
import com.smort.domain.File;
import com.smort.domain.Product;
import com.smort.domain.ProductPhoto;
import com.smort.error.FileStorageException;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CategoryRepository;
import com.smort.repositories.FileRepository;
import com.smort.repositories.ProductRepository;
import com.smort.repositories.VendorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;
    private final FileRepository fileRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, VendorRepository vendorRepository, CategoryRepository categoryRepository, FileRepository fileRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
        this.fileRepository = fileRepository;
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
        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product with id: " + id + " not found"));

        ProductDTO productDTO = convertToDTOAndAddUrls(product);

        return productDTO;

    }

    @Transactional
    @Override
    public ProductDTO createNewProduct(ProductDTO productDTO) {

        Product product = convertToProductAndSetVendorAndCategory(productDTO);

        Product savedProduct = productRepository.save(product);

        ProductDTO returnDto = convertToDTOAndAddUrls(savedProduct);

        returnDto.setProductUrl(UrlBuilder.getProductUrl(savedProduct.getId()));

        return returnDto;
    }

    @Transactional
    @Override
    public ProductDTO saveProductByDTO(Long id, ProductDTO productDTO) {
        Product product = convertToProductAndSetVendorAndCategory(productDTO);

        product.setId(id);

        Product savedProduct = productRepository.save(product);

        return convertToDTOAndAddUrls(savedProduct);
    }

    @Transactional
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
                product.setVendor(vendorRepository.findById(extractVendorIdFromUrl(productDTO)).orElseThrow(()-> new ResourceNotFoundException("Vendor with id: " + extractVendorIdFromUrl(productDTO) + " not found")));
            }

            if (productDTO.getCategoryUrl() != null) {
                product.setCategory(categoryRepository.findByName(extractCategoryNameFromUrl(productDTO)));
            }

            Product savedProduct = productRepository.save(product);

            ProductDTO returnDTO = convertToDTOAndAddUrls(savedProduct);

            return returnDTO;

        }).orElseThrow(()-> new ResourceNotFoundException("Product with id: " + id + " not found"));
    }

    @Transactional
    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> convertListToDto(List<Product> products) {
        return products.stream()
                .map(product -> {
                    ProductDTO productDTO = convertToDTOAndAddProductUrl(product);
                    return productDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ProductDTO uploadPhoto(Long id, MultipartFile file) throws IOException {

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product with id: " + id + " not found"));

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.contains("..")) {
            throw new FileStorageException("Filename contains invalid path sequence " + fileName);
        }

        File dbFile = new File(fileName, file.getContentType(), file.getBytes());

        File savedFile = fileRepository.save(dbFile);

        ProductPhoto productPhoto;

        if (product.getProductPhoto() == null) {
            productPhoto = new ProductPhoto();
            productPhoto.setProduct(product);
            productPhoto.setPhoto(savedFile);

            product.setProductPhoto(productPhoto);
        } else {
            productPhoto = product.getProductPhoto();
            productPhoto.setPhoto(savedFile);
        }

        Product savedProduct = productRepository.save(product);

        ProductDTO productDTO = convertToDTOAndAddUrls(savedProduct);

        return productDTO;

    }

    @Override
    public File getImageByProductId(Long id) {

        Product product = productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Product with id: " + id + " not found"));

        File dbFile = fileRepository.findById(product.getProductPhoto().getPhoto().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Product photo with product id: " + id + " not found"));

        return dbFile;
    }

    private ProductDTO convertToDTOAndAddUrls(Product product) {
        ProductDTO productDTO = productMapper.productToProductDTO(product);

        productDTO.setVendorUrl(UrlBuilder.getVendorUrl(product.getVendor().getId()));
        productDTO.setCategoryUrl(UrlBuilder.getCategoryUrl(product.getCategory().getName()));

        if (product.getProductPhoto() != null) {
            productDTO.setPhotoUrl(UrlBuilder.getPhotoUrl(product.getId()));
        }

        return productDTO;
    }

    private Product convertToProductAndSetVendorAndCategory(ProductDTO productDTO) {
        Long vendorId = extractVendorIdFromUrl(productDTO);

        String categoryName = extractCategoryNameFromUrl(productDTO);

        Product product = productMapper.productDTOToProduct(productDTO);

        product.setVendor(vendorRepository.findById(vendorId).orElseThrow(()-> new ResourceNotFoundException("Vendor with id: " + vendorId + " not found")));
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

        productDTO.setProductUrl(UrlBuilder.getProductUrl(product.getId()));

        return productDTO;
    }

}
