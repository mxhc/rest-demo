package com.smort.bootstrap;

import com.smort.domain.Category;
import com.smort.domain.Customer;
import com.smort.domain.Vendor;
import com.smort.repositories.CategoryRepository;
import com.smort.repositories.CustomerRepository;
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

    public Bootstrap(CategoryRepository categoryRepository,
                     CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;

    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        loadCustomers();
        loadVendors();

    }

    private void loadCategories() {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
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
        Vendor v1 = new Vendor();
        v1.setName("Buba");

        Vendor v2 = new Vendor();
        v2.setName("Sveze Voce d.o.o.");

        Vendor v3 = new Vendor();
        v3.setName("KiloIpo");

        vendorRepository.save(v1);
        vendorRepository.save(v2);
        vendorRepository.save(v3);

        log.warn("Vendors loaded = " + vendorRepository.count());

    }
}
