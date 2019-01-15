package com.smort.repositories;

import com.smort.domain.Category;
import com.smort.domain.Product;
import com.smort.domain.Vendor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test(expected = ConstraintViolationException.class)
    public void testInvalidInput() {
        Product product = new Product();
        product.setName("L");

        Category category = new Category();
        category.setId(1L);
        category.setName("Fruits");
        Vendor vendor = new Vendor();
        vendor.setId(2L);
        vendor.setName("Buba");

        product.setCategory(category);
        product.setVendor(vendor);

        productRepository.save(product);
        // nece baciti exception sve dok se ne uradi flush
        entityManager.flush();
    }

    @Test
    public void testSaveAndDelete() {
        Vendor vendor = new Vendor();
        vendor.setName("Pijacar");

        Vendor savedVendor = vendorRepository.save(vendor);

        Category category = new Category();
        category.setName("Voce");

        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Kokice");
        product.setPrice(156.32);

        product.setVendor(savedVendor);
        product.setCategory(savedCategory);

        vendor.setProducts(Arrays.asList(product));

        Product savedProduct = productRepository.save(product);

        Assert.assertEquals(product.getName(), savedProduct.getName());
        Assert.assertEquals(category.getName(), savedCategory.getName());
        Assert.assertEquals(vendor.getName(), savedVendor.getName());

        productRepository.delete(savedProduct);
        categoryRepository.delete(savedCategory);
        vendorRepository.delete(savedVendor);

        Assert.assertEquals(productRepository.count(), 0);
        Assert.assertEquals(categoryRepository.count(), 0);
        Assert.assertEquals(vendorRepository.count(), 0);
    }

    @Test
    public void update() {
        Vendor vendor = new Vendor();
        vendor.setName("Pijacar");

        Category category = new Category();
        category.setName("Voce");

        Category savedCategory = categoryRepository.save(category);

        Product product = new Product();
        product.setName("Kokice");
        product.setPrice(156.32);

        Vendor savedVendor = vendorRepository.save(vendor);

        product.setVendor(savedVendor);
        product.setCategory(savedCategory);

        Product savedProduct = productRepository.save(product);

        String updatedName = "Update";

        savedVendor.setName(updatedName);
        savedCategory.setName(updatedName);
        savedProduct.setName(updatedName);

        savedCategory = categoryRepository.save(savedCategory);
        savedProduct = productRepository.save(savedProduct);
        savedVendor = vendorRepository.save(savedVendor);

        Assert.assertEquals(updatedName, savedProduct.getName());
        Assert.assertEquals(updatedName, savedCategory.getName());
        Assert.assertEquals(updatedName, savedVendor.getName());

    }
}