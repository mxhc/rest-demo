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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static com.smort.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("no-security")
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
        productDTO1 = new ProductDTO();
        productDTO1.setName("Krompir");
        productDTO1.setPrice(250.3);
        productDTO1.setProductUrl(ProductController.BASE_URL + "/1");
        productDTO1.setVendorUrl(VendorController.BASE_URL + "/1");
        productDTO1.setCategoryUrl(CategoryController.BASE_URL + "/plodovi");

        productDTO2 = new ProductDTO();
        productDTO2.setName("Lesnik");
        productDTO2.setPrice(50.1);
        productDTO2.setProductUrl(ProductController.BASE_URL + "/2");
        productDTO2.setVendorUrl(VendorController.BASE_URL + "/2");
        productDTO2.setCategoryUrl(CategoryController.BASE_URL + "/nesto");

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
                .andExpect(jsonPath("$.vendor_url", equalTo(VendorController.BASE_URL + "/1")))
                .andExpect(jsonPath("$.product_url", equalTo(ProductController.BASE_URL + "/1")));

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

    @Test
    public void failedValidationNameAndPrice() throws Exception {
        ProductDTO invalidProductDTO = new ProductDTO();
        invalidProductDTO.setName("Zbadj");
        invalidProductDTO.setPrice(-20.1);


        mockMvc.perform(post(ProductController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(invalidProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].message", notNullValue()))
                .andExpect(jsonPath("$.violations", notNullValue()));;

        invalidProductDTO.setPrice(20.0);
        invalidProductDTO.setName("r");

        mockMvc.perform(post(ProductController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].message", notNullValue()))
                .andExpect(jsonPath("$.violations", notNullValue()));
    }

    @Test
    public void failedValidationForCategoryAndVendorUrls() throws Exception {
        ProductDTO invalidProductDTO = new ProductDTO();
        invalidProductDTO.setCategoryUrl("fff/12");

        mockMvc.perform(post(ProductController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].message", notNullValue()))
                .andExpect(jsonPath("$.violations", notNullValue()));;

        invalidProductDTO.setProductUrl(null);
        invalidProductDTO.setVendorUrl("mmmm/ffff");

        mockMvc.perform(post(ProductController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(invalidProductDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].message", notNullValue()))
                .andExpect(jsonPath("$.violations", notNullValue()));;
    }






















}