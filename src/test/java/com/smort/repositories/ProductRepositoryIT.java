package com.smort.repositories;

import com.smort.domain.Category;
import com.smort.domain.Product;
import com.smort.domain.Vendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolationException;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

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
        entityManager.flush();

    }

}