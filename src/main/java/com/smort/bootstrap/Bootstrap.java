package com.smort.bootstrap;

import com.smort.domain.*;
import com.smort.error.FileStorageException;
import com.smort.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Profile("h2")
@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    Vendor v1;
    Vendor v2;
    Vendor v3;

    Category fruits;
    Category dried;
    Category fresh;
    Category exotic;
    Category nuts;

    UserInfo c1;
    UserInfo c2;

    public Bootstrap(CategoryRepository categoryRepository,
                     VendorRepository vendorRepository, ProductRepository productRepository, OrderRepository orderRepository, UserRepository userRepository, FileRepository fileRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadUsers();
        loadCategories();
        loadUsers2();
        loadVendors();
        loadProducts();
        loadOrders();
    }

    private void loadUsers() {

        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("mxhc");
        userInfo.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo.setEnabled(true);
        userInfo.setEmail("mxhc@email.com");
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

        UserInfo userInfo1 = new UserInfo();
        userInfo1.setUserName("user");
        userInfo1.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo1.setEnabled(true);
        userInfo1.setEmail("user@email.com");
        userInfo1.setCountry("Denmark");
        userInfo1.setFirstName("Bjorn");
        userInfo1.setLastName("Olafssen");

        Role role = new Role(RolesEnum.ROLE_USER);
        userInfo1.addRole(role);

        userRepository.save(userInfo1);

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setUserName("admin");
        userInfo2.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo2.setEnabled(true);
        userInfo2.setEmail("admin@email.com");
        userInfo2.setCountry("Srbija");
        userInfo2.setFirstName("Ivan");
        userInfo2.setLastName("Bjelic");

        Role role5 = new Role(RolesEnum.ROLE_USER);
        userInfo2.addRole(role5);

        Role role6 = new Role(RolesEnum.ROLE_ADMIN);
        userInfo2.addRole(role6);

        userRepository.save(userInfo2);

        UserInfo userInfo3 = new UserInfo();
        userInfo3.setUserName("user2");
        userInfo3.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        userInfo3.setEnabled(true);
        userInfo3.setEmail("user2@email.com");
        userInfo3.setCountry("Denmark");
        userInfo3.setFirstName("Bjorn");
        userInfo3.setLastName("Olafssen");

        Role role7 = new Role(RolesEnum.ROLE_USER);
        userInfo3.addRole(role7);

        userRepository.save(userInfo3);

    }

    private void loadOrders() {


        Order order = new Order();


        Product p1 = new Product();
        p1.setName("Ananas");
        p1.setPrice(56.3);
        p1.setVendor(v1);
        p1.setCategory(fresh);

        p1 = addPhoto(p1.getName() + ".jpg", p1);

        p1 = productRepository.save(p1);

        Product p2 = new Product();
        p2.setName("Krastavac");
        p2.setPrice(112.0);
        p2.setVendor(v2);
        p2.setCategory(fruits);

        p2 = addPhoto(p2.getName() + ".jpg", p2);

        p2 = productRepository.save(p2);

        Product p3 = new Product();
        p3.setName("Papaja");
        p3.setPrice(56.3);
        p3.setVendor(v3);
        p3.setCategory(fruits);

        p3 = addPhoto(p3.getName() + ".jpg", p3);

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

        order.setUser(c1);

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

        order1.setUser(c2);

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

        order2.setUser(c2);

        orderRepository.save(order2);


        log.warn("Orders loaded = " + orderRepository.count());

    }

    private Product addPhoto(String fileName, Product product) {

        FileToMultipart ftm = new FileToMultipart();
        MultipartFile multipartFile = null;
        File dbFile = null;
        try {
            multipartFile = ftm.convertToMultipart(fileName);
            if (fileName.contains("..")) {
                throw new FileStorageException("Filename contains invalid path sequence " + fileName);
            }
            dbFile = new File(fileName, multipartFile.getContentType(), multipartFile.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        File savedFile = fileRepository.save(dbFile);

        ProductPhoto productPhoto;
        productPhoto = new ProductPhoto();
        productPhoto.setProduct(product);
        productPhoto.setPhoto(savedFile);
        product.setProductPhoto(productPhoto);

        return product;

    }

    private void loadProducts() {

        List<Product> products = new ArrayList<>();

        Product p1 = new Product();
        p1.setName("Krompir");
        p1.setPrice(56.3);
        p1.setVendor(v1);
        p1.setCategory(fresh);

        p1 = addPhoto(p1.getName() + ".jpg", p1);

        Product p2 = new Product();
        p2.setName("Banane");
        p2.setPrice(112.0);
        p2.setVendor(v2);
        p2.setCategory(fruits);

        p2 = addPhoto(p2.getName() + ".jpg", p2);

        Product p3 = new Product();
        p3.setName("Jabuke");
        p3.setPrice(56.3);
        p3.setVendor(v3);
        p3.setCategory(fruits);

        p3 = addPhoto(p3.getName() + ".jpg", p3);

        Product p4 = new Product();
        p4.setName("Lešnik");
        p4.setPrice(1150.0);
        p4.setVendor(v2);
        p4.setCategory(nuts);

        p4 = addPhoto(p4.getName() + ".jpg", p4);

        Product p5 = new Product();
        p5.setName("Suvo Grožđe");
        p5.setPrice(150.32);
        p5.setVendor(v1);
        p5.setCategory(dried);

        p5 = addPhoto(p5.getName() + ".jpg", p5);

        Product p6 = new Product();
        p6.setName("Jagode");
        p6.setPrice(150.32);
        p6.setVendor(v1);
        p6.setCategory(fresh);

        p6 = addPhoto(p6.getName() + ".jpg", p6);

        Product p7 = new Product();
        p7.setName("Maline");
        p7.setPrice(820.0);
        p7.setVendor(v2);
        p7.setCategory(fresh);

        p7 = addPhoto(p7.getName() + ".jpg", p7);

        Product p8 = new Product();
        p8.setName("Orah");
        p8.setPrice(1200.12);
        p8.setVendor(v3);
        p8.setCategory(nuts);

        p8 = addPhoto(p8.getName() + ".jpg", p8);

        Product p9 = new Product();
        p9.setName("Suve Banane");
        p9.setPrice(650.0);
        p9.setVendor(v2);
        p9.setCategory(dried);

        p9 = addPhoto(p9.getName() + ".jpg", p9);

        Product p10 = new Product();
        p10.setName("Crni Luk");
        p10.setPrice(75.2);
        p10.setVendor(v2);
        p10.setCategory(fresh);

        p10 = addPhoto(p10.getName() + ".jpg", p10);

        Product p12 = new Product();
        p12.setName("Beli Luk");
        p12.setPrice(354.7);
        p12.setVendor(v1);
        p12.setCategory(fresh);

        p12 = addPhoto(p12.getName() + ".jpg", p12);

        Product p13 = new Product();
        p13.setName("Kruške");
        p13.setPrice(354.9);
        p13.setVendor(v2);
        p13.setCategory(fruits);

        p13 = addPhoto(p13.getName() + ".jpg", p13);

        Product p14 = new Product();
        p14.setName("Kupine");
        p14.setPrice(1500.32);
        p14.setVendor(v3);
        p14.setCategory(fresh);

        p14 = addPhoto(p14.getName() + ".jpg", p14);

        Product p15 = new Product();
        p15.setName("Mandarine");
        p15.setPrice(151.2);
        p15.setVendor(v1);
        p15.setCategory(fresh);

        p15 = addPhoto(p15.getName() + ".jpg", p15);

        Product p16 = new Product();
        p16.setName("Mango");
        p16.setPrice(750.32);
        p16.setVendor(v3);
        p16.setCategory(exotic);

        p16 = addPhoto(p16.getName() + ".jpg", p16);

        Product p17 = new Product();
        p17.setName("Vlašac");
        p17.setPrice(150.32);
        p17.setVendor(v1);
        p17.setCategory(fresh);

        p17 = addPhoto(p17.getName() + ".jpg", p17);

        Product p18 = new Product();
        p18.setName("Breskve");
        p18.setPrice(741.1);
        p18.setVendor(v2);
        p18.setCategory(fruits);

        p18 = addPhoto(p18.getName() + ".jpg", p18);

        products.addAll(Arrays.asList(
                p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p12, p13, p14, p15, p16, p17, p18));

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

    private void loadUsers2() {
        c1 = new UserInfo();
        c1.setFirstName("Milojko");
        c1.setLastName("Pantic");
        c1.setUserName("milojko");
        c1.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        c1.setEnabled(true);
        c1.setEmail("milojko@email.com");
        c1.setCountry("Srbija");

        c2 = new UserInfo();
        c2.setFirstName("Milan");
        c2.setLastName("Petrovic");
        c2.setUserName("milan");
        c2.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        c2.setEnabled(true);
        c2.setEmail("milan@email.com");
        c2.setCountry("Srbija");

        UserInfo c3 = new UserInfo();
        c3.setFirstName("Inga");
        c3.setLastName("Zica");
        c3.setUserName("inga");
        c3.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        c3.setEnabled(true);
        c3.setEmail("inga@email.com");
        c3.setCountry("Srbija");

        UserInfo c4 = new UserInfo();
        c4.setFirstName("David");
        c4.setLastName("Zica");
        c4.setUserName("david");
        c4.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        c4.setEnabled(true);
        c4.setEmail("david@email.com");
        c4.setCountry("Srbija");

        UserInfo c5 = new UserInfo();
        c5.setFirstName("Janko");
        c5.setLastName("Petrovic");
        c5.setUserName("janko");
        c5.setPassword(new BCryptPasswordEncoder().encode("xxx"));
        c5.setEnabled(true);
        c5.setEmail("janko@email.com");
        c5.setCountry("Srbija");

        c1 = userRepository.save(c1);
        c2 = userRepository.save(c2);
        userRepository.save(c3);
        userRepository.save(c4);
        userRepository.save(c5);

        log.warn("Users loaded = " + userRepository.count());
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
