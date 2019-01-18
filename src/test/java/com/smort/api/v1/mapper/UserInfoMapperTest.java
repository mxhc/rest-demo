package com.smort.api.v1.mapper;

import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.controllers.v1.AbstractRestControllerTest;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class UserInfoMapperTest extends AbstractRestControllerTest {

    public static final Long ID = 1L;
    public static final String USER_NAME = "xxx";
    public static final List<RoleDTO> ROLES_DTO = Arrays.asList(new RoleDTO(RolesEnum.ROLE_ADMIN), new RoleDTO(RolesEnum.ROLE_USER));
    public static final List<Role> ROLES = Arrays.asList(new Role(RolesEnum.ROLE_ADMIN), new Role(RolesEnum.ROLE_USER));
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String COUNTRY = "Uzbekistan";
    public static final String EMAIL = "johndoe@google.com";

    @Test
    public void userToUserDTO() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(ID);
        userInfo.setUserName(USER_NAME);
        userInfo.setRoles(ROLES);
        userInfo.setFirstName(FIRST_NAME);
        userInfo.setLastName(LAST_NAME);
        userInfo.setCountry(COUNTRY);
        userInfo.setEmail(EMAIL);

        UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(userInfo);

        userInfoDTO.setRoles(userInfo.getRoles().stream().map(role -> {
            RoleDTO roleDTO = UserInfoMapper.INSTANCE.roleToRoleDTO(role);
            return roleDTO;
        }).collect(Collectors.toList()));

        assertEquals(userInfo.getUserName(), userInfoDTO.getUserName());
        assertEquals(userInfo.getRoles().get(1).getRole(), userInfoDTO.getRoles().get(1).getRole());
        assertEquals(userInfo.getFirstName(), userInfoDTO.getFirstName());
        assertEquals(userInfo.getLastName(), userInfoDTO.getLastName());
        assertEquals(userInfo.getCountry(), userInfoDTO.getCountry());
        assertEquals(userInfo.getEmail(), userInfoDTO.getEmail());
    }

    @Test
    public void UserDtoToUser() {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setRoles(ROLES_DTO);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setLastName(LAST_NAME);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setEmail(EMAIL);

        UserInfo userInfo = UserInfoMapper.INSTANCE.userInfoDTOToUserInfo(userInfoDTO);

        userInfo.setRoles(userInfoDTO.getRoles().stream().map(roleDTO -> {
            Role role = UserInfoMapper.INSTANCE.roleDTOToRole(roleDTO);
            return role;
        }).collect(Collectors.toList()));

        assertEquals(userInfoDTO.getUserName(), userInfo.getUserName());
        assertEquals(userInfoDTO.getRoles().get(1).getRole(), userInfo.getRoles().get(1).getRole());
        assertEquals(userInfoDTO.getFirstName(), userInfo.getFirstName());
        assertEquals(userInfoDTO.getLastName(), userInfo.getLastName());
        assertEquals(userInfoDTO.getCountry(), userInfo.getCountry());
        assertEquals(userInfoDTO.getEmail(), userInfo.getEmail());

    }

}