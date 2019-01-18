package com.smort.services;

import com.smort.api.v1.mapper.UserInfoMapper;
import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private UserRepository userRepository;

    public UserInfoServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userInfo -> {
                    UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(userInfo);
                    userInfoDTO.setUserUrl(UrlBuilder.getUserUrl(userInfo.getId()));
                    userInfoDTO.setRoles(userInfo.getRoles()
                            .stream()
                            .map(role -> {
                                RoleDTO roleDTO = UserInfoMapper.INSTANCE.roleToRoleDTO(role);
                                return roleDTO;
                            }).collect(Collectors.toList()));
                    return userInfoDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public UserInfoDTO createNewUser(UserInfoDTO userInfoDTO) {

        UserInfo userInfo = UserInfoMapper.INSTANCE.userInfoDTOToUserInfo(userInfoDTO);

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode(userInfoDTO.getClearPassword());

        userInfo.setPassword(encodedPassword);

        return UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(userRepository.save(userInfo));

    }

    @Override
    public UserInfoDTO setRole(RolesEnum role, Long id) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        userInfo.addRole(new Role(role));

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(savedUser);

        userInfoDTO.setUserUrl(UrlBuilder.getUserUrl(id));

        return userInfoDTO;
    }
}
