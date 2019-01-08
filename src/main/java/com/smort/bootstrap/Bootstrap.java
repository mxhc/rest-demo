package com.smort.bootstrap;

import com.smort.domain.Category;
import com.smort.domain.Customer;
import com.smort.domain.Product;
import com.smort.domain.Vendor;
import com.smort.repositories.CategoryRepository;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.ProductRepository;
import com.smort.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;
    private ProductRepository productRepository;

    Vendor v1;
    Vendor v2;
    Vendor v3;

    Category fruits;
    Category dried;
    Category fresh;
    Category exotic;
    Category nuts;



    public Bootstrap(CategoryRepository categoryRepository,
                     CustomerRepository customerRepository,
                     VendorRepository vendorRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        loadCustomers();
        loadVendors();
        loadProducts();

    }

    private void loadProducts() {
        Product p1 = new Product();
        p1.setName("Krompir");
        p1.setPrice(56.3);
        p1.setVendor(v1);
        p1.setCategory(fresh);

        Product p2 = new Product();
        p2.setName("Banane");
        p2.setPrice(112.0);
        p2.setVendor(v2);
        p2.setCategory(fruits);

        Product p3 = new Product();
        p3.setName("Jabuke");
        p3.setPrice(56.3);
        p3.setVendor(v3);
        p3.setCategory(fruits);

        Product p4 = new Product();
        p4.setName("Lesnik");
        p4.setPrice(1150.0);
        p4.setVendor(v2);
        p4.setCategory(nuts);

        Product p5 = new Product();
        p5.setName("Suvo Grozdje");
        p5.setPrice(150.32);
        p5.setVendor(v1);
        p5.setCategory(fresh);

        productRepository.save(p1);
        productRepository.save(p2);
        productRepository.save(p3);
        productRepository.save(p4);
        productRepository.save(p5);

        log.warn("Products Loaded = " + productRepository.count());


    }

    private void loadCategories() {
        fruits = new Category();
        fruits.setName("Fruits");

        dried = new Category();
        dried.setName("Dried");

        fresh = new Category();
        fresh.setName("Fresh");

        exotic = new Category();
        exotic.setName("Exotic");

        nuts = new Category();
        nuts.setName("Nuts");

        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);

        log.warn("Data Loaded = " + categoryRepository.count());
    }

    private void loadCustomers() {
        Customer c1 = new Customer();
        c1.setFirstname("Milojko");
        c1.setLastname("Pantic");

        Customer c2 = new Customer();
        c2.setFirstname("Milan");
        c2.setLastname("Petrovic");

        Customer c3 = new Customer();
        c3.setFirstname("Inga");
        c3.setLastname("Zica");

        Customer c4 = new Customer();
        c4.setFirstname("David");
        c4.setLastname("Zica");

        Customer c5 = new Customer();
        c5.setFirstname("Janko");
        c5.setLastname("Petrovic");

        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
        customerRepository.save(c4);
        customerRepository.save(c5);

        log.warn("Customers loaded = " + customerRepository.count());
    }

    private void loadVendors() {
        v1 = new Vendor();
        v1.setName("Buba");

        v2 = new Vendor();
        v2.setName("Sveze Voce d.o.o.");

        v3 = new Vendor();
        v3.setName("KiloIpo");

        vendorRepository.save(v1);
        vendorRepository.save(v2);
        vendorRepository.save(v3);

        log.warn("Vendors loaded = " + vendorRepository.count());

    }
}
