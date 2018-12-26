package com.smort.services;

import com.smort.api.v1.mapper.VendorMapper;
import com.smort.api.v1.model.VendorDTO;
import com.smort.controllers.v1.VendorController;
import com.smort.domain.Vendor;
import com.smort.repositories.VendorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendorUrl(getVendorUrl(vendor.getId()));
                    return vendorDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO findById(Long id) {
        return vendorRepository.findById(id)
                .map(vendorMapper::vendorToVendorDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {

        Vendor vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);

        Vendor savedVendor = vendorRepository.save(vendor);

        VendorDTO returnDto = vendorMapper.vendorToVendorDTO(savedVendor);

        returnDto.setVendorUrl(getVendorUrl(savedVendor.getId()));

        return returnDto;
    }

    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {

        Vendor vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);

        vendor.setId(id);

        return saveAndReturnDto(vendor);
    }

    private VendorDTO saveAndReturnDto(Vendor vendor) {

        Vendor savedVendor = vendorRepository.save(vendor);

        VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);

        returnDTO.setVendorUrl(getVendorUrl(savedVendor.getId()));

        return returnDTO;
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {

        return vendorRepository.findById(id).map(vendor ->  {

            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }

            VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(vendorRepository.save(vendor));

            returnDTO.setVendorUrl(getVendorUrl(id));

            return returnDTO;

        }).orElseThrow(ResourceNotFoundException::new);

    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

    private String getVendorUrl(Long id) {
        return VendorController.BASE_URL + "/" + id;
    }
}