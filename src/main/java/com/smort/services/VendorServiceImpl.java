package com.smort.services;

import com.smort.api.v1.mapper.VendorMapper;
import com.smort.api.v1.model.VendorDTO;
import com.smort.domain.Vendor;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.VendorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
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
                    vendorDTO.setVendorUrl(UrlBuilder.getVendorUrl(vendor.getId()));
                    return vendorDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO findById(Long id) {
        return vendorRepository.findById(id)
                .map(vendorMapper::vendorToVendorDTO)
                .map(vendorDTO -> {
                    vendorDTO.setVendorUrl(UrlBuilder.getVendorUrl(id));
                    return vendorDTO;
                })
                .orElseThrow(()-> new ResourceNotFoundException("Vendor with id: " + id + " not found"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {

        Vendor vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);

        Vendor savedVendor = vendorRepository.save(vendor);

        VendorDTO returnDto = vendorMapper.vendorToVendorDTO(savedVendor);

        returnDto.setVendorUrl(UrlBuilder.getVendorUrl(savedVendor.getId()));

        return returnDto;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public VendorDTO saveVendorByDTO(Long id, VendorDTO vendorDTO) {

        Vendor vendor = vendorMapper.vendorDTOtoVendor(vendorDTO);

        vendor.setId(id);

        return saveAndReturnDto(vendor);
    }

    @Transactional
    public VendorDTO saveAndReturnDto(Vendor vendor) {

        Vendor savedVendor = vendorRepository.save(vendor);

        VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);

        returnDTO.setVendorUrl(UrlBuilder.getVendorUrl(savedVendor.getId()));

        return returnDTO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {

        //todo if more properties, add more if statements

        return vendorRepository.findById(id).map(vendor ->  {

            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }

            VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(vendorRepository.save(vendor));

            returnDTO.setVendorUrl(UrlBuilder.getVendorUrl(id));

            return returnDTO;

        }).orElseThrow(()-> new ResourceNotFoundException("Vendor with id: " + id + " not found"));

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

    @Override
    public Vendor findVendorById(Long id) {
        return vendorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Vendor with id: " + id + " not found"));
    }

}
