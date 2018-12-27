package com.smort.controllers.v1;

import com.smort.api.v1.model.VendorDTO;
import com.smort.api.v1.model.VendorListDTO;
import com.smort.services.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VendorController.BASE_URL)
public class VendorController {

    public static final String BASE_URL = "/api/v1/vendors";

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.getAllVendors());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createNewVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.createNewVendor(vendorDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendor(@RequestBody VendorDTO vendorDTO, @PathVariable Long id) {
        return vendorService.saveVendorByDTO(id, vendorDTO);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO patchVendor(@RequestBody VendorDTO vendorDTO, @PathVariable Long id) {
        return vendorService.patchVendor(id, vendorDTO);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendorById(id);
    }



}
