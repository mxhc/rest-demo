package com.smort.services;

import com.smort.api.v1.mapper.CategoryMapper;
import com.smort.api.v1.model.CategoryDTO;
import com.smort.domain.Category;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class CategoryServiceImplTest {

    public static final Long ID = 2L;
    public static final String NAME = "Fresh";

    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        categoryService = new CategoryServiceImpl(CategoryMapper.INSTANCE, categoryRepository);
    }

    @Test
    public void getAllCategories() {

        // given
        List<Category> categories = Arrays.asList(new Category(), new Category(), new Category());

        when(categoryRepository.findAll()).thenReturn(categories);

        // when
        List<CategoryDTO> categoryDTOS = categoryService.getAllCategories();

        // then
        assertEquals(3, categoryDTOS.size());

    }

    @Test
    public void findByName() {
        // given
        Category category = new Category();
        category.setName(NAME);
        category.setId(ID);

        when(categoryRepository.findByName(anyString())).thenReturn(category);

        Category returnedCategory = categoryService.findByName(NAME);

        // when
        assertEquals(NAME, returnedCategory.getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByNameNotFound() {
        given(categoryRepository.findByName(anyString())).willReturn(null);

        Category category = categoryService.findByName("Stojko");

        then(categoryRepository).should(times(1)).findByName(anyString());
    }
}