package com.smort.repositories;

import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findOneByUserAndRole(UserInfo userInfo, RolesEnum role);

}
