package com.smort.repositories;

import com.smort.domain.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryPaging extends PagingAndSortingRepository<Product, Long> {
}
