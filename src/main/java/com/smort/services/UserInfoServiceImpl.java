package com.smort.services;

import com.smort.api.v1.mapper.UserInfoMapper;
import com.smort.api.v1.model.RoleDTO;
import com.smort.api.v1.model.UserInfoDTO;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.error.InvalidUserOperationException;
import com.smort.error.ResourceNotFoundException;
import com.smort.repositories.RoleRepository;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserInfoServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserInfoDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userInfo -> {
                    UserInfoDTO userInfoDTO = convertToDTO(userInfo);
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

        if (userInfo.getRoles().stream().anyMatch(role1 -> role1.getRole().getRole().equals(role.getRole()))) {
            throw new InvalidUserOperationException("User already has that role");
        }

        userInfo.addRole(new Role(role));

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO userInfoDTO = convertToDTO(savedUser);

        return userInfoDTO;
    }

    @Override
    public UserInfoDTO activateUser(Long id) {

        UserInfo enabledUser = userRepository.findById(id).map(userInfo -> {
            userInfo.setEnabled(true);
            UserInfo saved = userRepository.save(userInfo);
            return saved;
        }).orElseThrow(ResourceNotFoundException::new);

        UserInfoDTO userInfoDTO = convertToDTO(enabledUser);

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

        UserInfoDTO returnDto = convertToDTO(savedUser);

        return returnDto;
    }

    @Override
    public UserInfoDTO getUserById(Long id) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        UserInfoDTO userInfoDTO = convertToDTO(userInfo);

        return userInfoDTO;
    }

    @Override
    public UserInfoDTO revokeRole(Long id, RolesEnum role) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (!userInfo.getRoles().stream().anyMatch(role1 -> role1.getRole().getRole().equals(role.getRole()))) {
            throw new InvalidUserOperationException("User does not have that role");
        }

        Role roleToDelete = userInfo.getRoles().stream()
                .filter(role1 -> role1.getRole().getRole().equals(role.getRole()))
                .findFirst()
                .get();

        userInfo.getRoles().remove(roleToDelete);

        roleRepository.delete(roleToDelete);

        UserInfoDTO userInfoDTO = convertToDTO(userInfo);

        return userInfoDTO;

    }

    @Override
    public UserInfoDTO resetPassword(Long id, String newPassword) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode(newPassword);

        userInfo.setPassword(password);

        userRepository.save(userInfo);

        return convertToDTO(userInfo);
    }

    private UserInfoDTO convertToDTO(UserInfo userInfo) {
        UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(userInfo);

        userInfoDTO.setUserUrl(UrlBuilder.getUserUrl(userInfo.getId()));

        userInfoDTO.setRoles(convertRolesToRolesDTO(userInfo.getRoles()));

        return userInfoDTO;
    }

    private List<RoleDTO> convertRolesToRolesDTO(List<Role> roles) {
        return roles
                .stream()
                .map(role -> {
                    RoleDTO roleDTO = UserInfoMapper.INSTANCE.roleToRoleDTO(role);
                    return roleDTO;
                }).collect(Collectors.toList());
    }

}
