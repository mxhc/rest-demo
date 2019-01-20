package com.smort.services;

import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.RolesEnum;

import java.util.List;

public interface UserInfoService {
    List<UserInfoDTO> getAllUsers();

    UserInfoDTO createNewUser(UserInfoDTO userInfoDTO);

    UserInfoDTO setRole(RolesEnum role, Long id);

    UserInfoDTO activateUser(Long id);

    void deleteUser(Long id);

    UserInfoDTO editUser(UserInfoDTO userInfoDTO, Long id);

    UserInfoDTO getUserById(Long id);

    UserInfoDTO revokeRole(Long id, RolesEnum role);

    UserInfoDTO resetPassword(Long id, String newPassword);
}
