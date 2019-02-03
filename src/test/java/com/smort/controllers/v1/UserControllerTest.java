package com.smort.controllers.v1;

import com.smort.api.v1.model.PasswordDTO;
import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.controllers.RestResponseEntityExceptionHandler;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.services.UrlBuilder;
import com.smort.services.UserInfoService;
import com.smort.services.UserInfoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractRestControllerTest {

    public static final Long ID = 1L;
    public static final String EMAIL = "jondoe@gmail.com";
    public static final String USER_NAME = "johndoe";
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME= "Doe";
    public static final String COUNTRY = "USA";
    public static final Boolean ENABLED = true;
    public static final String PASSWORD = "password";

    @Mock
    UserInfoService userInfoService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getUsers() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);
        userInfoDTO.setClearPassword(PASSWORD);

        UserInfoDTO ui1 = new UserInfoDTO();

        UserInfoDTO ui2 = new UserInfoDTO();

        List<UserInfoDTO> userInfoDTOS = Arrays.asList(userInfoDTO, ui1, ui2);

        when(userInfoService.getAllUsers()).thenReturn(userInfoDTOS);

        mockMvc.perform(get(UserController.BASE_URL).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0]lastName", equalTo(LAST_NAME)));
    }

    @Test
    public void createNewUser() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);
        userInfoDTO.setClearPassword(PASSWORD);

        UserInfoDTO returnDTO = new UserInfoDTO();
        returnDTO.setUserName(userInfoDTO.getUserName());
        returnDTO.setFirstName(userInfoDTO.getFirstName());
        returnDTO.setLastName(userInfoDTO.getLastName());
        returnDTO.setEmail(userInfoDTO.getEmail());
        returnDTO.setCountry(userInfoDTO.getCountry());
        returnDTO.setUserUrl(UrlBuilder.getUserUrl(ID));
        returnDTO.setEnabled(false);

        when(userInfoService.createNewUser(any(UserInfoDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(post(UserController.BASE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(userInfoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user_url", equalTo(UrlBuilder.getUserUrl(ID))))
                .andExpect(jsonPath("$.userName", equalTo(USER_NAME)));

    }


    @Test
    public void setRole() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);

        UserInfo user = new UserInfo();
        user.setEnabled(false);
        user.setEmail(EMAIL);
        user.setUserName(USER_NAME);
        user.setCountry(COUNTRY);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setId(ID);

        Role r1 = new Role(RolesEnum.ROLE_USER);
        r1.setId(1L);
        user.addRole(r1);

        List<RoleDTO> roleDTOS = UserInfoServiceImpl.convertRolesToRolesDTO(user.getRoles());

        userInfoDTO.setRoles(roleDTOS);

        when(userInfoService.setRole(any(RolesEnum.class), anyLong())).thenReturn(userInfoDTO);

//        when(userInfoService.setRole(RolesEnum.ROLE_USER, ID, request)).thenReturn(userInfoDTO);

        mockMvc.perform(post(UserController.BASE_URL + "/" + ID + "/roles?role=" + RolesEnum.ROLE_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userInfoDTO)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.email", equalTo(EMAIL)))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles[0].role", equalTo(r1.getRole().getRole())))
                .andExpect(jsonPath("$.enabled", equalTo(false)));

    }


    @Test
    public void setActive() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);
        userInfoDTO.setEnabled(true);

        when(userInfoService.activateUser(anyLong())).thenReturn(userInfoDTO);

        mockMvc.perform(post(UserController.BASE_URL + "/" + ID + "/activate")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser() throws Exception {

        mockMvc.perform(delete(UserController.BASE_URL + "/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userInfoService, times(1)).deleteUserById(anyLong());

    }

    @Test
    public void editUser() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);

        when(userInfoService.editUser(any(UserInfoDTO.class), anyLong())).thenReturn(userInfoDTO);

        mockMvc.perform(put(UserController.BASE_URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userInfoDTO)))
                .andExpect(status().isOk());

    }

    @Test
    public void getUser() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);

        when(userInfoService.getUserById(anyLong())).thenReturn(userInfoDTO);

        mockMvc.perform(get(UserController.BASE_URL + "/" + ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo(EMAIL)))
                .andExpect(jsonPath("$.lastName", equalTo(LAST_NAME)));

    }

    @Test
    public void revokeRole() throws Exception {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);

        when(userInfoService.revokeRole(anyLong(), any(RolesEnum.class))).thenReturn(userInfoDTO);

        mockMvc.perform(delete(UserController.BASE_URL + "/" + ID + "/roles?role=ROLE_USER")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void changePassword() throws Exception {

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setUserName(USER_NAME);
        userInfoDTO.setFirstName(FIRST_NAME);
        userInfoDTO.setEmail(EMAIL);
        userInfoDTO.setCountry(COUNTRY);
        userInfoDTO.setLastName(LAST_NAME);

        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setPassword("password");

        when(userInfoService.resetPassword(anyLong(), any(PasswordDTO.class))).thenReturn(userInfoDTO);

        mockMvc.perform(post(UserController.BASE_URL + "/" + ID + "/password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(passwordDTO)))
                .andExpect(status().isOk());

    }
}