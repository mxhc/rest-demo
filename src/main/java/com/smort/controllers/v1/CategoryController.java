package com.smort.controllers.v1;

import com.smort.api.v1.model.CategoryListDTO;
import com.smort.api.v1.model.ProductListDTO;
import com.smort.services.CategoryService;
import com.smort.services.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(CategoryController.BASE_URL)
public class CategoryController {

    public static final String BASE_URL = "/api/v1/categories";
    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @ApiOperation(value = "This will get a list of categories", notes = "Collection of Categories")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CategoryListDTO getAllCategories() {

        return new CategoryListDTO(categoryService.getAllCategories());
    }

    @ApiOperation(value = "This will get a list of products by category", notes = "Collection of Products")
    @GetMapping("/{name}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO getListOfProductsByCategory(@PathVariable String name) {
        return new ProductListDTO(productService.convertListToDto(categoryService.findByName(name).getProducts()));
    }

}
