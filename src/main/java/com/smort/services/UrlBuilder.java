package com.smort.services;

import com.smort.controllers.v1.*;

public final class UrlBuilder {

    public static String getProductUrl(Long id) {
        return ProductController.BASE_URL + "/" + id;
    }

    public static String getOrderUrl(Long id) {
        return OrderController.BASE_URL + "/" + id;
    }

    public static String getVendorUrl(Long id) {
        return VendorController.BASE_URL + "/" + id;
    }

    public static String getCategoryUrl(String categoryName) {
        return CategoryController.BASE_URL + "/" + categoryName;
    }

    public static String getCustomerUrl(Long id) {
        return CustomerController.BASE_URL + "/" + id;
    }

    public static String getItemsUrl(Long orderId) {
        return OrderController.BASE_URL + "/" + orderId + "/items/";
    }

    public static String getUserUrl(Long userId) {
        return UserController.BASE_URL + "/" + userId;
    }

    public static String getFileUri(String fileId) {
        return FileController.BASE_URL + "/" + fileId;
    }

    public static String getPhotoUrl(Long id) {
        return ProductController.BASE_URL + "/" + id + "/photo";
    }

    public static String getNextProductsPageUrl(Integer page, Integer limit) {
        return ProductController.BASE_URL + "/?page=" + (page + 1) + "&limit=" + limit;
    }

    public static String getNextCustomersPageUrl(Integer page, Integer limit) {
        return CustomerController.BASE_URL + "/?page=" + (page + 1) + "&limit=" + limit;
    }

    public static String getNextUsersPageUrl(Integer page, Integer limit) {
        return UserController.BASE_URL + "/?page=" + (page + 1) + "&limit=" + limit;
    }
}
