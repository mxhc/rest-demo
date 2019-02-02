package com.smort.services;

import com.smort.api.v1.mapper.CustomerMapper;
import com.smort.api.v1.model.CustomerDTO;
import com.smort.api.v1.model.CustomerListDTO;
import com.smort.api.v1.model.MetaDTO;
import com.smort.domain.Customer;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.CustomerRepository;
import com.smort.repositories.CustomerRepositoryPaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final CustomerRepositoryPaging customerRepositoryPaging;

    public CustomerServiceImpl(CustomerMapper customerMapper, CustomerRepository customerRepository, CustomerRepositoryPaging customerRepositoryPaging) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.customerRepositoryPaging = customerRepositoryPaging;
    }



    @Override
    public CustomerListDTO getAllCustomersMeta() {

        List<CustomerDTO> customers = customerRepository.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(UrlBuilder.getCustomerUrl(customer.getId()));
                    return customerDTO;
                })
                .collect(Collectors.toList());

        MetaDTO metaDTO = new MetaDTO();
        metaDTO.setCount((long) customers.size());

        CustomerListDTO customerListDTO = new CustomerListDTO();
        customerListDTO.setCustomers(customers);
        customerListDTO.setMeta(metaDTO);

        return customerListDTO;
    }

    @Override
    public CustomerListDTO getAllCustomersPaginated(Integer page, int limit) {

        CustomerListDTO customerListDTO = new CustomerListDTO();

        Pageable pageableRequest = PageRequest.of(page - 1, limit);

        Page<Customer> customersPage = customerRepositoryPaging.findAll(pageableRequest);

        List<CustomerDTO> listOfDTOs = customersPage.getContent().stream().map(customer -> {
            CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
            customerDTO.setCustomerUrl(UrlBuilder.getCustomerUrl(customer.getId()));
            return customerDTO;
        }).collect(Collectors.toList());

        Long customersCount = customerRepository.count();

        MetaDTO metaDTO = new MetaDTO();
        metaDTO.setCount(customersCount);
        metaDTO.setLimit(limit);
        metaDTO.setPage(page);
        metaDTO.setNextUrl(UrlBuilder.getNextCustomersPageUrl(page, limit));

        customerListDTO.setCustomers(listOfDTOs);
        customerListDTO.setMeta(metaDTO);

        return customerListDTO;
    }

    @Override
    public CustomerDTO findById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO)
                .map(customerDTO -> {
                    customerDTO.setCustomerUrl(UrlBuilder.getCustomerUrl(id));
                    return customerDTO;
                }).orElseThrow(()-> new ResourceNotFoundException("Customer with id: " + id + " not found"));
    }

    @Transactional
    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {

        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);

        Customer savedCustomer = customerRepository.save(customer);

        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);

        returnDTO.setCustomerUrl(UrlBuilder.getCustomerUrl(savedCustomer.getId()));

        return returnDTO;

    }

    @Transactional
    public CustomerDTO saveAndReturnDTO(Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);

        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);

        returnDTO.setCustomerUrl(UrlBuilder.getCustomerUrl(savedCustomer.getId()));

        return returnDTO;

    }

    @Transactional
    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {

        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);

        customer.setId(id);

        return saveAndReturnDTO(customer);
    }

    @Transactional
    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(customer -> {

            if (customerDTO.getFirstname() != null) {
                customer.setFirstname(customerDTO.getFirstname());
            }

            if (customerDTO.getLastname() != null) {
                customer.setLastname(customerDTO.getLastname());
            }

            CustomerDTO returnDto = customerMapper.customerToCustomerDTO(customerRepository.save(customer));

            returnDto.setCustomerUrl(UrlBuilder.getCustomerUrl(id));

            return returnDto; }).orElseThrow(()-> new ResourceNotFoundException("Customer with id: " + id + " not found"));

    }

    @Transactional
    @Override
    public void deleteCustomerById(Long id) {
        // todo error handling if id is not found
        customerRepository.deleteById(id);
    }



}
