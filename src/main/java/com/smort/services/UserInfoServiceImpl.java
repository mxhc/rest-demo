package com.smort.services;

import com.smort.api.v1.mapper.UserInfoMapper;
import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

        log.warn("Clear pass: " + userInfoDTO.getClearPassword());

        String encodedPassword = encoder.encode(userInfoDTO.getClearPassword());

        userInfo.setPassword(encodedPassword);

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO returnDto = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(savedUser);

        returnDto.setUserUrl(UrlBuilder.getUserUrl(savedUser.getId()));

        return returnDto;

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

    @Override
    public UserInfoDTO activateUser(Long id) {

        UserInfo enabledUser = userRepository.findById(id).map(userInfo -> {
            userInfo.setEnabled(true);
            return userInfo;
        }).orElseThrow(ResourceNotFoundException::new);

        UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(enabledUser);

        userInfoDTO.setUserUrl(UrlBuilder.getUserUrl(id));

        return userInfoDTO;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    @Override
    public UserInfoDTO editUser(UserInfoDTO userInfoDTO, Long id) {

        UserInfo oldUser = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        UserInfo userInfo = UserInfoMapper.INSTANCE.userInfoDTOToUserInfo(userInfoDTO);
        userInfo.setId(id);
        userInfo.setEnabled(oldUser.isEnabled());
        userInfo.setPassword(oldUser.getPassword());

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO returnDto = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(savedUser);
        returnDto.setUserUrl(UrlBuilder.getUserUrl(id));

        return returnDto;
    }



}
