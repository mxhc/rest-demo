package com.smort.repositories;

import com.smort.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepositoryPaging extends PagingAndSortingRepository<Product, Long> {
}
