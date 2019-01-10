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

@Api(description = "This is Vendor Controller")
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

    @ApiOperation(value = "List all the Vendors", notes = "Collection of Vendors")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.getAllVendors());
    }

    @ApiOperation(value = "Get a vendor by id", notes = "Vendor of products")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findById(id);
    }

    @ApiOperation(value = "Create a vendor", notes = "Vendor of products")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createNewVendor( @ApiParam("Vendor information for new Vendor to be created") @RequestBody VendorDTO vendorDTO) {
        return vendorService.createNewVendor(vendorDTO);
    }

    @ApiOperation(value = "Replace a vendor by new data")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendor( @ApiParam("Vendor information for Vendor to be edited") @RequestBody VendorDTO vendorDTO, @PathVariable Long id) {
        return vendorService.saveVendorByDTO(id, vendorDTO);
    }

    // todo calling save by dto, should call patchVendor
    @ApiOperation(value = "Update a vendor")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PatchMapping({"/{id}"})
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO patchVendor(@PathVariable Long id, @ApiParam("Vendor information for Vendor to be patched") @RequestBody VendorDTO vendorDTO){
        return vendorService.saveVendorByDTO(id, vendorDTO);
    }

    @ApiOperation(value = "Delete a vendor")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
    }

    @ApiOperation(value = "Get the products of a vendor", notes = "Collection of Products")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/{id}/products")
    @ResponseStatus(HttpStatus.OK)
    public ProductListDTO findProductsByVendor(@PathVariable Long id){
        return new ProductListDTO(productService.convertListToDto(vendorService.findVendorById(id).getProducts()));
    }

    @ApiOperation(value = "Add a product to a vendor")
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/{id}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO addProductToVendor(@ApiParam("Product information for new Product to be added to Vendor") @RequestBody ProductDTO productDTO, @PathVariable Long id) {
        return null;
    }
}


