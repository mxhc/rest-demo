package com.smort.controllers.v1;

import com.smort.api.v1.model.ProductDTO;
import com.smort.api.v1.model.VendorDTO;
import com.smort.api.v1.model.VendorListDTO;
import com.smort.domain.Product;
import com.smort.domain.Vendor;
import com.smort.services.ProductService;
import com.smort.services.VendorService;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static com.smort.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {VendorController.class})
//@WithMockUser(username = "admin", roles={"ADMIN"})
@ActiveProfiles("no-security")
public class VendorControllerTest {

    @MockBean //provided by Spring Context
    VendorService vendorService;

    @MockBean
    ProductService productService;

    @Autowired
    MockMvc mockMvc; //provided by Spring Context

    VendorDTO vendorDTO_1;
    VendorDTO vendorDTO_2;

    @Before
    public void setUp() throws Exception {
        vendorDTO_1 = new VendorDTO("Vendor 1", VendorController.BASE_URL + "/1");
        vendorDTO_2 = new VendorDTO("Vendor 2", VendorController.BASE_URL + "/2");
    }

    @Test
    public void getVendorList() throws Exception {
        VendorListDTO vendorListDTO = new VendorListDTO(Arrays.asList(vendorDTO_1, vendorDTO_2));

        given(vendorService.getAllVendors()).willReturn(vendorListDTO.getVendors());

        mockMvc.perform(get(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {

        given(vendorService.findById(anyLong())).willReturn(vendorDTO_1);

        mockMvc.perform(get(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())));
    }

    @Test
    public void createNewVendor() throws Exception {

        given(vendorService.createNewVendor(vendorDTO_1)).willReturn(vendorDTO_1);

        mockMvc.perform(post(VendorController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateVendor() throws Exception {

        given(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO_1);

        mockMvc.perform(put(VendorController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(vendorDTO_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())));
    }

    @Test
    public void patchVendor() throws Exception {
        given(vendorService.saveVendorByDTO(anyLong(), any(VendorDTO.class))).willReturn(vendorDTO_1);

        mockMvc.perform(patch(VendorController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(vendorDTO_1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(vendorDTO_1.getName())));
    }

    @Test
    public void deleteVendor() throws Exception {
        mockMvc.perform(delete(VendorController.BASE_URL + "/1"))
                .andExpect(status().isOk());

        then(vendorService).should().deleteVendorById(anyLong());

    }

    @Test
    public void findProductsByVendor() throws Exception {
        Vendor vendor = new Vendor();
        vendor.setName("Radovan");
        vendor.setId(1L);
        vendor.setProducts(Arrays.asList(new Product(), new Product(), new Product()));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Girice");

        List<ProductDTO> productDTOS = Arrays.asList(productDTO, new ProductDTO(), new ProductDTO());

        given(vendorService.findVendorById(anyLong())).willReturn(vendor);
        given(productService.convertListToDto(anyList())).willReturn(productDTOS);

        mockMvc.perform(get(VendorController.BASE_URL + "/1/products")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products", hasSize(3)))
                .andExpect(jsonPath("$.products[0].name", equalTo("Girice")));

    }

    @Test
    public void addProductToVendor() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ananas");
        productDTO.setPrice(55.3);
        productDTO.setCategoryUrl("/api/v1/categories/Fruits");
        productDTO.setVendorUrl("/api/v1/vendors/2");
        productDTO.setProductUrl("/api/v1/products/19");

        given(productService.createNewProduct(productDTO)).willReturn(productDTO);

        mockMvc.perform(post(VendorController.BASE_URL + "/2/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Ananas")));

    }

    @Test
    public void failedValidation() throws Exception {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("A");
        productDTO.setPrice(-55.3);
        productDTO.setCategoryUrl("1/categories/Fruits");
        productDTO.setVendorUrl("/api/vendors/2");
        productDTO.setProductUrl("/api/v1/products/19");

        mockMvc.perform(post(VendorController.BASE_URL + "/2/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations", notNullValue()))
                .andExpect(jsonPath("$.violations", hasSize(4)));
    }



}