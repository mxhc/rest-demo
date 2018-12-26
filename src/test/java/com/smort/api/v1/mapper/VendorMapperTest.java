package com.smort.api.v1.mapper;


import com.smort.api.v1.model.VendorDTO;
import com.smort.domain.Vendor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VendorMapperTest {

    public static final Long ID = 1L;
    public static final String NAME = "Milojkova Pekara";

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void vendorToVendorDTO() {

        // given
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(NAME);

        // when
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        // then
        assertEquals(vendor.getName(), vendorDTO.getName());

    }

    @Test
    public void vendorDTOtoVendor() {

        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        // when
        Vendor vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);

        // then
        assertEquals(vendorDTO.getName(), vendor.getName());

    }
}