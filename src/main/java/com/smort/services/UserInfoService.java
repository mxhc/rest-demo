package com.smort.services;

import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.RolesEnum;

import java.util.List;

public interface UserInfoService {
    List<UserInfoDTO> getAllUsers();

    UserInfoDTO createNewUser(UserInfoDTO userInfoDTO);

    UserInfoDTO setRole(RolesEnum role, Long id);
}
