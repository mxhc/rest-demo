package com.smort.services;

import com.smort.api.v1.model.PasswordDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.api.v1.model.UserListDTO;
import com.smort.domain.RolesEnum;

public interface UserInfoService {

    UserListDTO getAllUsersMeta();

    UserListDTO getAllUsersPaginated(Integer page, int limit);

    UserInfoDTO createNewUser(UserInfoDTO userInfoDTO);

    UserInfoDTO setRole(RolesEnum role, Long id);

    UserInfoDTO activateUser(Long id);

    void deleteUserById(Long id);

    UserInfoDTO editUser(UserInfoDTO userInfoDTO, Long id);

    UserInfoDTO getUserById(Long id);

    UserInfoDTO revokeRole(Long id, RolesEnum role);

    UserInfoDTO resetPassword(Long id, PasswordDTO passwordDTO);
}
