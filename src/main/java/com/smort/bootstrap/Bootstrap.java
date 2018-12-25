package com.smort.bootstrap;

import com.smort.domain.Category;
import com.smort.domain.Customer;
import com.smort.repositories.CategoryRepository;
import com.smort.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;

    public Bootstrap(CategoryRepository categoryRepository, CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        loadCustomers();

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
}
