package com.smort.services;

import com.smort.controllers.v1.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlBuilderTest {

    @Test
    public void getProductUrl() {
        String url = UrlBuilder.getProductUrl(5L);
        assertEquals(ProductController.BASE_URL + "/" + 5, url);
    }

    @Test
    public void getOrderUrl() {
        String url = UrlBuilder.getOrderUrl(3L);
        assertEquals(OrderController.BASE_URL + "/" + 3, url);
    }

    @Test
    public void getVendorUrl() {
        String url = UrlBuilder.getVendorUrl(100001L);
        assertEquals(VendorController.BASE_URL + "/" + 100001, url);
    }

    @Test
    public void getCategoryUrl() {
        String url = UrlBuilder.getCategoryUrl("Fruits");
        assertEquals(CategoryController.BASE_URL + "/Fruits", url);
    }

    @Test
    public void getCustomerUrl() {
        String url = UrlBuilder.getCustomerUrl(1234L);
        assertEquals(CustomerController.BASE_URL + "/" + 1234, url);
    }

    @Test
    public void getItemsUrl() {
        String url = UrlBuilder.getItemsUrl(3L);
        assertEquals(OrderController.BASE_URL + "/" + 3 + "/items/", url);
    }
}