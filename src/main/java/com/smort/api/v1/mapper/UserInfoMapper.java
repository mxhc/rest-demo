package com.smort.api.v1.mapper;

import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.Role;
import com.smort.domain.UserInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserInfoMapper {

    UserInfoMapper INSTANCE = Mappers.getMapper(UserInfoMapper.class);

    UserInfoDTO userInfoToUserInfoDTO(UserInfo userInfo);

    UserInfo userInfoDTOToUserInfo(UserInfoDTO userInfoDTO);

    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);


}
