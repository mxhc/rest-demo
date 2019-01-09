package com.smort.controllers.v1;

import com.smort.api.v1.model.CategoryDTO;
import com.smort.api.v1.model.ProductDTO;
import com.smort.controllers.RestResponseEntityExceptionHandler;
import com.smort.domain.Category;
import com.smort.domain.Product;
import com.smort.services.CategoryService;
import com.smort.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {

    public static final String NAME = "Milojko";

    @Mock
    CategoryService categoryService;

    @Mock
    ProductService productService;

    @InjectMocks
    CategoryController categoryController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testListCategories() throws Exception {
        // given
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setName(NAME);

        CategoryDTO categoryDTO2 = new CategoryDTO();

        categoryDTO.setName("Perica");

        List<CategoryDTO> categories = Arrays.asList(categoryDTO, categoryDTO2);

        // when
        when(categoryService.getAllCategories()).thenReturn(categories);

        // then
        mockMvc.perform(get(CategoryController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(2)));

    }


    @Test
    public void getListOfProductsByCategory() throws Exception {

        // given
        Category category = new Category();
        category.setName(NAME);
        category.setId(1L);

        Product product = new Product();
        product.setName("Banane");

        List<Product> products = Arrays.asList(new Product(), product, new Product(), new Product());
        category.setProducts(products);

        List<ProductDTO> productDTOS = Arrays.asList(new ProductDTO(), new ProductDTO(), new ProductDTO(), new ProductDTO());

        given(categoryService.findByName(anyString())).willReturn(category);
        given(productService.convertListToDto(anyList())).willReturn(productDTOS);

        mockMvc.perform(get(CategoryController.BASE_URL + "/" + NAME)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(4)));

    }
}






























