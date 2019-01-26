package com.smort.bootstrap;

import com.smort.domain.*;
import com.smort.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("h2")
@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private UserRepository userRepository;

    Vendor v1;
    Vendor v2;
    Vendor v3;

    Category fruits;
    Category dried;
    Category fresh;
    Category exotic;
    Category nuts;

    Customer c1;
    Customer c2;

    public Bootstrap(CategoryRepository categoryRepository,
                     CustomerRepository customerRepository,
                     VendorRepository vendorRepository, ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        loadCategories();
        loadCustomers();
        loadVendors();
        loadProducts();
        loadOrders();
        loadUsers();

    }

    private void loadUsers() {

        UserInfo userInfo = new UserInfo();
        userInfo.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo.setEnabled(true);
        userInfo.setEmail("email@email.com");
        userInfo.setUserName("mxhc");
        userInfo.setCountry("India");
        userInfo.setFirstName("Mile");
        userInfo.setLastName("Karapandza");

        Role r1 = new Role(RolesEnum.ROLE_USER);
        userInfo.addRole(r1);

        Role r2 = new Role(RolesEnum.ROLE_ADMIN);
        userInfo.addRole(r2);

        Role r3 = new Role(RolesEnum.ROLE_SUPERADMIN);
        userInfo.addRole(r3);

        userRepository.save(userInfo);

        UserInfo userInfo1 =new UserInfo();
        userInfo1.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo1.setEnabled(true);
        userInfo1.setEmail("nekiemail@email.com");
        userInfo1.setUserName("user");
        userInfo1.setCountry("Denmark");
        userInfo1.setFirstName("Bjorn");
        userInfo1.setLastName("Olafssen");

        Role role = new Role(RolesEnum.ROLE_USER);
        userInfo1.addRole(role);

        userRepository.save(userInfo1);

    }

    private void loadOrders() {


        Order order = new Order();


        Product p1 = new Product();
        p1.setName("Ananas");
        p1.setPrice(56.3);
        p1.setVendor(v1);
        p1.setCategory(fresh);

        p1 = productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Krastavac");
        p2.setPrice(112.0);
        p2.setVendor(v2);
        p2.setCategory(fruits);

        p2 = productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Papaja");
        p3.setPrice(56.3);
        p3.setVendor(v3);
        p3.setCategory(fruits);

        p3 = productRepository.save(p3);

        OrderItem oi1 = new OrderItem();

        oi1.setProduct(p1);
        oi1.setPrice(22.15);
        oi1.setQuantity(5);


        OrderItem oi2 = new OrderItem();

        oi2.setProduct(p2);
        oi2.setPrice(221.35);
        oi2.setQuantity(5);


        order.addOrderItem(oi1);
        order.addOrderItem(oi2);

        order.setCustomer(c1);

        order = orderRepository.save(order);

        // changing status to ordered
        order.setState(OrderStatus.ORDERED);
        orderRepository.save(order);



        Order order1 = new Order();

        OrderItem oi4 = new OrderItem();
        oi4.setProduct(p3);
        oi4.setPrice(221.15);
        oi4.setQuantity(15);

        OrderItem oi3 = new OrderItem();
        oi3.setProduct(p1);
        oi3.setPrice(224.15);
        oi3.setQuantity(45);

        order1.addOrderItem(oi4);
        order1.addOrderItem(oi3);

        order1.setCustomer(c2);

        orderRepository.save(order1);


        Order order2 = new Order();

        OrderItem oi5 = new OrderItem();
        oi5.setProduct(p3);
        oi5.setPrice(1568.2);
        oi5.setQuantity(1);

        OrderItem oi6 = new OrderItem();
        oi6.setProduct(p1);
        oi6.setPrice(12224.15);
        oi6.setQuantity(45);

        OrderItem oi7 = new OrderItem();
        oi7.setProduct(p2);
        oi7.setPrice(2224.15);
        oi7.setQuantity(5);

        order2.addOrderItem(oi5);
        order2.addOrderItem(oi6);
        order2.addOrderItem(oi7);

        order2.setCustomer(c2);

        orderRepository.save(order2);


        log.warn("Orders loaded = " + orderRepository.count());

    }

    private void loadProducts() {

        List<Product> products = new ArrayList<>();

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
        p5.setCategory(dried);

        Product p6 = new Product();
        p6.setName("Jagode");
        p6.setPrice(150.32);
        p6.setVendor(v1);
        p6.setCategory(fresh);

        Product p7 = new Product();
        p7.setName("Maline");
        p7.setPrice(820.0);
        p7.setVendor(v2);
        p7.setCategory(fresh);

        Product p8 = new Product();
        p8.setName("Orah");
        p8.setPrice(1200.12);
        p8.setVendor(v3);
        p8.setCategory(nuts);

        Product p9 = new Product();
        p9.setName("Suve Banane");
        p9.setPrice(650.0);
        p9.setVendor(v2);
        p9.setCategory(dried);

        Product p10 = new Product();
        p10.setName("Crni Luk");
        p10.setPrice(75.2);
        p10.setVendor(v2);
        p10.setCategory(fresh);

        Product p11 = new Product();
        p11.setName("Ananas");
        p11.setPrice(756.25);
        p11.setVendor(v3);
        p11.setCategory(exotic);

        Product p12 = new Product();
        p12.setName("Beli Luk");
        p12.setPrice(354.7);
        p12.setVendor(v1);
        p12.setCategory(fresh);

        Product p13 = new Product();
        p13.setName("Kruske");
        p13.setPrice(354.9);
        p13.setVendor(v2);
        p13.setCategory(fruits);


        Product p14 = new Product();
        p14.setName("Kupine");
        p14.setPrice(1500.32);
        p14.setVendor(v3);
        p14.setCategory(fresh);

        Product p15 = new Product();
        p15.setName("Mandarine");
        p15.setPrice(151.2);
        p15.setVendor(v1);
        p15.setCategory(fresh);

        Product p16 = new Product();
        p16.setName("Mango");
        p16.setPrice(750.32);
        p16.setVendor(v3);
        p16.setCategory(exotic);

        Product p17 = new Product();
        p17.setName("Vlasac");
        p17.setPrice(150.32);
        p17.setVendor(v1);
        p17.setCategory(fresh);

        Product p18 = new Product();
        p18.setName("Breskve");
        p18.setPrice(741.1);
        p18.setVendor(v2);
        p18.setCategory(fruits);

        products.addAll(Arrays.asList(
                p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18));

        productRepository.saveAll(products);


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
        c1 = new Customer();
        c1.setFirstname("Milojko");
        c1.setLastname("Pantic");

        c2 = new Customer();
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

        c1 = customerRepository.save(c1);
        c2 = customerRepository.save(c2);
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
