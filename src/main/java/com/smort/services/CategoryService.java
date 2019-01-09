package com.smort.services;

import com.smort.api.v1.model.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    CategoryDTO getListOfProductsByCategory(String name);

}
