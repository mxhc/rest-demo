package com.smort.services;

import com.smort.api.v1.model.CategoryDTO;
import com.smort.domain.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    Category findByName(String categoryName);

}
