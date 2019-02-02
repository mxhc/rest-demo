package com.smort.controllers.v1;

import com.smort.api.v1.model.ProductDTO;
import com.smort.api.v1.model.ProductListDTO;
import com.smort.domain.File;
import com.smort.services.ProductService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@Api(description = "Product Controller")
@Validated
@RestController
@RequestMapping(ProductController.BASE_URL)
public class ProductController {

    public static final String BASE_URL = "/api/v1/products";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Get paginated list of products", notes = "Collection of Products")
    @GetMapping("/paginated")
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO getPaginatedListOfUsers(@RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return productService.getAllProductsPaginated(page, limit);
    }

    @ApiOperation(value = "${controller.product.get.list}", notes = "${controller.product.get.list.notes}")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO getAllProducts() {
        return productService.getAllProductsMeta();
    }

    @ApiOperation(value = "${controller.product.get}", notes = "${controller.product.get.notes}")
    @GetMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @ApiOperation(value = "${controller.product.post}", notes = "${controller.product.post.notes}")
    @PostMapping
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createNewProduct(@ApiParam("${controller.product.post.param}") @Valid @RequestBody ProductDTO productDTO) {
        return productService.createNewProduct(productDTO);
    }

    @ApiOperation(value = "${controller.product.put}")
    @PutMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO updateProduct(@PathVariable Long id, @ApiParam("${controller.product.put.param}") @Valid @RequestBody ProductDTO productDTO) {
        return productService.saveProductByDTO(id, productDTO);
    }

    @ApiOperation(value = "${controller.product.patch}")
    @PatchMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO patchProduct(@PathVariable Long id, @ApiParam("${controller.product.patch.param}") @Valid @RequestBody ProductDTO productDTO) {
        return productService.patchProduct(id, productDTO);
    }

    @ApiOperation(value = "${controller.product.delete}")
    @DeleteMapping("/{id}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
    }


    @ApiOperation(value = "Upload a product photo")
    @PutMapping("/{id}/photo")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO uploadProductPhoto(@PathVariable Long id, @RequestParam MultipartFile productPhoto) throws IOException {
        return productService.uploadPhoto(id, productPhoto);
    }

    @ApiOperation(value = "Get a photo of a product")
    @GetMapping(value = "/{id}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getProductPhoto(@PathVariable Long id) {

        File dbFile = productService.getImageByProductId(id);

        return ResponseEntity
                .ok()
                .contentType(MediaType.valueOf(dbFile.getFileType()))
                .body(dbFile.getData());

    }
}

