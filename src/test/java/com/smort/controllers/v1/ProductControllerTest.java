package com.smort.controllers.v1;

import com.smort.api.v1.model.ProductDTO;
import com.smort.api.v1.model.ProductListDTO;
import com.smort.services.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.smort.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {ProductController.class})
public class ProductControllerTest {

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc;

    ProductDTO productDTO1;
    ProductDTO productDTO2;

    @Before
    public void setUp() throws Exception {
        productDTO1 = new ProductDTO("Krompir", 250.3, CategoryController.BASE_URL + "/voce", VendorController.BASE_URL + "/1");
        productDTO2 = new ProductDTO("Lesnik", 50.1, CategoryController.BASE_URL + "/plodovi", VendorController.BASE_URL + "/2");
    }

    @Test
    public void getAllProducts() throws Exception {
        ProductListDTO productListDTO = new ProductListDTO(Arrays.asList(productDTO1, productDTO2));

        given(productService.getAllProducts()).willReturn(productListDTO.getProducts());

        mockMvc.perform(get(ProductController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(2)));
    }

    @Test
    public void getProductById() throws Exception {

        given(productService.findById(anyLong())).willReturn(productDTO1);

        mockMvc.perform(get(ProductController.BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO1.getName())))
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/1")));

    }

    @Test
    public void createNewProduct() throws Exception {
        given(productService.createNewProduct(productDTO1)).willReturn(productDTO1);

        mockMvc.perform(post(ProductController.BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJsonString(productDTO1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(productDTO1.getName())));

    }

    @Test
    public void updateProduct() throws Exception {
        given(productService.saveProductByDTO(anyLong(), any(ProductDTO.class))).willReturn(productDTO1);

        mockMvc.perform(put(ProductController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(productDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO1.getName())));
    }

    @Test
    public void patchProduct() throws Exception {
        given(productService.patchProduct(anyLong(), any(ProductDTO.class))).willReturn(productDTO1);

        mockMvc.perform(patch(ProductController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(productDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO1.getName())));

    }

    @Test
    public void deleteProduct() throws Exception {
        mockMvc.perform(delete(ProductController.BASE_URL + "/1"))
                .andExpect(status().isOk());

        then(productService).should().deleteProductById(anyLong());
    }





















}