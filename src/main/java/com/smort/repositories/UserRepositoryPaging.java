package com.smort.repositories;

import com.smort.domain.UserInfo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryPaging extends PagingAndSortingRepository<UserInfo, Long> {
}
