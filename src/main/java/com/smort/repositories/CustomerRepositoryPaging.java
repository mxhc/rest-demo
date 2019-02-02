package com.smort.repositories;

import com.smort.domain.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerRepositoryPaging extends PagingAndSortingRepository<Customer, Long> {
}
