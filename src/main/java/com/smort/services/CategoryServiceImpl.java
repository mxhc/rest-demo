package com.smort.services;

import com.smort.api.v1.mapper.CategoryMapper;
import com.smort.api.v1.model.CategoryDTO;
import com.smort.controllers.v1.CategoryController;
import com.smort.domain.Category;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .map(this::addUrl)
                .collect(Collectors.toList());
    }

    @Override
    public Category findByName(String categoryName) {
        return Optional.ofNullable(categoryRepository.findByName(categoryName)).orElseThrow(()-> new ResourceNotFoundException("No such category"));
    }

    private CategoryDTO addUrl(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryUrl(CategoryController.BASE_URL + "/" + categoryDTO.getName());
        return categoryDTO;
    }

}
