package com.smort.controllers.v1;

import com.smort.api.v1.model.ProductDTO;
import com.smort.api.v1.model.ProductListDTO;
import com.smort.api.v1.model.VendorDTO;
import com.smort.api.v1.model.VendorListDTO;
import com.smort.services.ProductService;
import com.smort.services.VendorService;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(description = "${controller.vendor.title}")
@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorService vendorService;
    private final ProductService productService;

    public VendorController(VendorService vendorService, ProductService productService) {
        this.vendorService = vendorService;
        this.productService = productService;
    }

    @ApiOperation(value = "${controller.vendor.get.list}", notes = "${controller.vendor.get.list.notes}")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.getAllVendors());
    }

    @ApiOperation(value = "${controller.vendor.get}", notes = "${controller.vendor.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findById(id);
    }

    @ApiOperation(value = "${controller.vendor.post}", notes = "${controller.vendor.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createNewVendor( @ApiParam("${controller.vendor.post.param}") @RequestBody VendorDTO vendorDTO) {
        return vendorService.createNewVendor(vendorDTO);
    }

    @ApiOperation(value = "${controller.vendor.put}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendor( @ApiParam("${controller.vendor.put.param}") @RequestBody VendorDTO vendorDTO, @PathVariable Long id) {
        return vendorService.saveVendorByDTO(id, vendorDTO);
    }

    // todo calling save by dto, should call patchVendor
    @ApiOperation(value = "${controller.vendor.patch}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PatchMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO patchVendor(@PathVariable Long id, @ApiParam("${controller.vendor.patch.param}") @RequestBody VendorDTO vendorDTO){
        return vendorService.saveVendorByDTO(id, vendorDTO);
    }

    @ApiOperation(value = "${controller.vendor.delete}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
    }

    @ApiOperation(value = "${controller.vendor.get.products}", notes = "${controller.vendor.get.products.notes}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{id}/products")
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO findProductsByVendor(@PathVariable Long id){
        return new ProductListDTO(productService.convertListToDto(vendorService.findVendorById(id).getProducts()));
    }

    @ApiOperation(value = "${controller.vendor.post.product}")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO addProductToVendor(@ApiParam(value = "${controller.vendor.post.product.param}") @RequestBody ProductDTO productDTO, @PathVariable Long id) {
        return null;
    }
}


