package com.smort.services;

import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.controllers.v1.AbstractRestControllerTest;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.error.InvalidUserOperationException;
import com.smort.repositories.RoleRepository;
import com.smort.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserInfoServiceImplTest extends AbstractRestControllerTest {

    UserInfoService userInfoService;

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    public static final Long ID = 1L;
    public static final String EMAIL = "jondoe@gmail.com";
    public static final String USER_NAME = "johndoe";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME= "Doe";
    public static final String COUNTRY = "USA";
    public static final Boolean ENABLED = true;
    public static final String PASSWORD = "password";

    HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        userInfoService = new UserInfoServiceImpl(userRepository, roleRepository);

    }

    @Test
    public void getAllUsers() {

        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);

        List<UserInfo> users = Arrays.asList(new UserInfo(), new UserInfo(), user);

        when(userRepository.findAll()).thenReturn(users);

        List<UserInfoDTO> userInfoDTOS = userInfoService.getAllUsers();

        assertEquals(3, userInfoDTOS.size());
        assertEquals(EMAIL, userInfoDTOS.get(2).getEmail());
        assertEquals(UrlBuilder.getUserUrl(ID), userInfoDTOS.get(2).getUserUrl());
        assertEquals(USER_NAME, userInfoDTOS.get(2).getUserName());
    }

    @Test
    public void createNewUser() {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);
        userInfoDTO.setClearPassword(PASSWORD);

        UserInfo user = new UserInfo();
        user.setEmail(userInfoDTO.getEmail());
        user.setUserName(userInfoDTO.getUserName());
        user.setCountry(userInfoDTO.getCountry());
        user.setFirstName(userInfoDTO.getFirstName());
        user.setLastName(userInfoDTO.getLastName());
        user.setPassword(new BCryptPasswordEncoder().encode(userInfoDTO.getClearPassword()));
        user.setId(ID);

        when(userRepository.save(any(UserInfo.class))).thenReturn(user);

        UserInfoDTO savedDTO = userInfoService.createNewUser(userInfoDTO);

        assertEquals(userInfoDTO.getEmail(), savedDTO.getEmail());
        assertEquals(userInfoDTO.getUserName(), savedDTO.getUserName());
        assertEquals(userInfoDTO.getCountry(), savedDTO.getCountry());

    }

    @Test(expected = InvalidUserOperationException.class)
    public void setDuplicateRole() {
        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);


        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(UserInfo.class))).thenReturn(user);

        UserInfoDTO userInfoDTO = userInfoService.setRole(RolesEnum.ROLE_USER, 1L);

    }

    @Test
    public void setRole() {
        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);


        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(userRepository.save(any(UserInfo.class))).thenReturn(user);

        UserInfoDTO userInfoDTO = userInfoService.setRole(RolesEnum.ROLE_ADMIN, 1L);

        assertEquals(RolesEnum.ROLE_ADMIN.getRole(), userInfoDTO.getRoles().get(1).getRole().getRole());
        assertEquals(RolesEnum.ROLE_USER, userInfoDTO.getRoles().get(0).getRole());

    }

    @Test
    public void deleteUser() {

        UserInfo user = new UserInfo();
        user.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        userInfoService.deleteUserById(1L);

        verify(userRepository, times(1)).delete(any(UserInfo.class));

    }


    @Test
    public void getUserById() {

        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);


        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserInfoDTO userInfoDTO = userInfoService.getUserById(1L);

        assertEquals(user.getEmail(), userInfoDTO.getEmail());
        assertEquals(user.getUserName(), userInfoDTO.getUserName());
        assertEquals(user.getCountry(), userInfoDTO.getCountry());
        assertEquals(user.getRoles().get(0).getRole(), userInfoDTO.getRoles().get(0).getRole());

    }

    @Test
    public void revokeRole() {

        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);


        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        Role r2 = new Role(RolesEnum.ROLE_ADMIN);
        r2.setId(10L);
        user.addRole(r2);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        UserInfoDTO userInfoDTO = userInfoService.revokeRole(1L, RolesEnum.ROLE_USER);

        assertEquals(user.getRoles().size(), userInfoDTO.getRoles().size());
        assertEquals(user.getRoles().get(0).getRole(), userInfoDTO.getRoles().get(0).getRole());
        assertEquals(user.getEmail(), userInfoDTO.getEmail());
    }

    @Test(expected = InvalidUserOperationException.class)
    public void revokeRoleThatUserDoesNotHave() {

        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);

        Role r2 = new Role(RolesEnum.ROLE_ADMIN);
        r2.setId(10L);
        user.addRole(r2);

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));

        userInfoService.revokeRole(1L, RolesEnum.ROLE_USER);
    }


    @Test
    public void convertToDTO() {
        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);

        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        Role r2 = new Role(RolesEnum.ROLE_ADMIN);
        r2.setId(10L);
        user.addRole(r2);

        UserInfoServiceImpl usi = new UserInfoServiceImpl(userRepository, roleRepository);
        UserInfoDTO userInfoDTO = usi.convertToDTO(user);

        assertEquals(user.getRoles().size(), userInfoDTO.getRoles().size());
        assertEquals(user.getRoles().get(0).getRole(), userInfoDTO.getRoles().get(0).getRole());
        assertEquals(user.getEmail(), userInfoDTO.getEmail());
        assertEquals(user.getUserName(), userInfoDTO.getUserName());
        assertEquals(user.getFirstName(), userInfoDTO.getFirstName());
        assertEquals(user.getLastName(), userInfoDTO.getLastName());


    }

    @Test
    public void convertRolesToRolesDTO() {

        UserInfo user = new UserInfo();
        user.setEnabled(ENABLED);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);

        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        Role r2 = new Role(RolesEnum.ROLE_ADMIN);
        r2.setId(10L);
        user.addRole(r2);

        UserInfoServiceImpl usi = new UserInfoServiceImpl(userRepository, roleRepository);

        List<RoleDTO> roleDTOS = usi.convertRolesToRolesDTO(user.getRoles());

        assertEquals(user.getRoles().size(), roleDTOS.size());
        assertEquals(user.getRoles().get(0).getRole(), roleDTOS.get(0).getRole());
        assertEquals(user.getRoles().get(1).getRole(), roleDTOS.get(1).getRole());

    }
}