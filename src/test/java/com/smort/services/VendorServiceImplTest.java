package com.smort.services;

import com.smort.api.v1.mapper.VendorMapper;
import com.smort.api.v1.model.VendorDTO;
import com.smort.controllers.v1.VendorController;
import com.smort.domain.Vendor;
import com.smort.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class VendorServiceImplTest {

    public static final Long ID_1 = 1L;
    public static final String NAME_1 = "Buba";
    public static final Long ID_2 = 2L;
    public static final String NAME_2 = "Buba";

    VendorService vendorService;

    @Mock
    VendorRepository vendorRepository;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);

    }

    @Test
    public void getAllVendors() {

        // given
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());

        when(vendorRepository.findAll()).thenReturn(vendors);

        // when
        List<VendorDTO> vendorDTOS = vendorService.getAllVendors();

        // then
        assertEquals(3, vendorDTOS.size());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void getVendorByIdNotFound() throws Exception {
        //given
        //mockito BBD syntax since mockito 1.10.0
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        VendorDTO vendorDTO = vendorService.findById(1L);

        //then
        then(vendorRepository).should(times(1)).findById(anyLong());

    }

    @Test
    public void findById() {

        // given
        Vendor vendor = new Vendor();
        vendor.setId(ID_1);
        vendor.setName(NAME_1);
        Optional<Vendor> optionalVendor = Optional.ofNullable(vendor);

        when(vendorRepository.findById(anyLong())).thenReturn(optionalVendor);

        // when
        VendorDTO vendorDTO = vendorService.findById(ID_1);

        // then
        assertEquals(NAME_1, vendorDTO.getName());
    }

    @Test
    public void findByIdBDD() throws Exception {

        // given
        Vendor vendor = getVendor1();

        // mockito behavior driven development
        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));

        // when
        VendorDTO vendorDTO = vendorService.findById(1L);

        // then
        then(vendorRepository).should(times(1)).findById(anyLong());

        // junit assert
        assertThat(vendorDTO.getName(), is(equalTo(NAME_1)));

    }

    @Test
    public void createNewVendor() {

        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME_1);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        // when
        VendorDTO savedDTO = vendorService.createNewVendor(vendorDTO);

        // then
        assertEquals(vendorDTO.getName(), savedDTO.getName());
        assertEquals(VendorController.BASE_URL + "/1", savedDTO.getVendorUrl());

    }

    @Test
    public void saveVendorByDTO() {

        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME_1);

        Vendor savedVendor = new Vendor();
        savedVendor.setName(vendorDTO.getName());
        savedVendor.setId(1L);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        // when
        VendorDTO savedDto = vendorService.saveVendorByDTO(1L, vendorDTO);

        // then
        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals(VendorController.BASE_URL + "/1", savedDto.getVendorUrl());

    }

    @Test
    public void patchVendor() {

        // given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME_1);

        Vendor vendor = getVendor1();

        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor));
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendor);

        // when
        VendorDTO savedVendorDTO = vendorService.patchVendor(ID_1, vendorDTO);

        //then
        // 'should' defaults to times = 1
        then(vendorRepository).should().save(any(Vendor.class));
        then(vendorRepository).should(times(1)).findById(anyLong());
        assertThat(savedVendorDTO.getVendorUrl(), containsString("1"));
    }

    @Test
    public void deleteVendorById() {

        //when
        vendorService.deleteVendorById(1L);

        //then
        then(vendorRepository).should().deleteById(anyLong());

    }

    private Vendor getVendor1() {
        Vendor vendor = new Vendor();
        vendor.setName(NAME_1);
        vendor.setId(ID_1);
        return vendor;
    }

    private Vendor getVendor2() {
        Vendor vendor = new Vendor();
        vendor.setName(NAME_2);
        vendor.setId(ID_2);
        return vendor;
    }



}