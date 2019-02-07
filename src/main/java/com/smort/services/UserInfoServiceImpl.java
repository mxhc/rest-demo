package com.smort.services;

import com.smort.api.v1.mapper.UserInfoMapper;
import com.smort.api.v1.model.*;
import com.smort.domain.Role;
import com.smort.domain.RolesEnum;
import com.smort.domain.UserInfo;
import com.smort.error.InvalidUserOperationException;
import com.smort.error.ResourceNotFoundException;
import com.smort.error.UniqueFieldException;
import com.smort.repositories.RoleRepository;
import com.smort.repositories.UserRepository;
import com.smort.repositories.UserRepositoryPaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {


    private final UserRepository userRepository;
    private final UserRepositoryPaging userRepositoryPaging;
    private final RoleRepository roleRepository;

    public UserInfoServiceImpl(UserRepository userRepository, UserRepositoryPaging userRepositoryPaging, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userRepositoryPaging = userRepositoryPaging;
        this.roleRepository = roleRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Override
    public UserListDTO getAllUsersMeta() {

        List<UserInfoDTO> usersDTO = userRepository.findAll()
                .stream()
                .map(userInfo -> {
                    UserInfoDTO userInfoDTO = convertToDTO(userInfo);
                    return userInfoDTO;
                }).collect(Collectors.toList());

        MetaDTO metaDTO = new MetaDTO();
        metaDTO.setCount((long) usersDTO.size());

        UserListDTO userListDTO = new UserListDTO();
        userListDTO.setUsers(usersDTO);
        userListDTO.setMeta(metaDTO);

        return userListDTO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Override
    public UserListDTO getAllUsersPaginated(Integer page, int limit) {
        UserListDTO userListDTO = new UserListDTO();

        Pageable pageableRequest = PageRequest.of(page - 1, limit);

        Page<UserInfo> userPage = userRepositoryPaging.findAll(pageableRequest);

        List<UserInfoDTO> listOfDTOs = userPage.getContent().stream().map(userInfo -> {
            UserInfoDTO userInfoDTO = convertToDTO(userInfo);
            return userInfoDTO;
        }).collect(Collectors.toList());

        Long userCount = userRepository.count();

        MetaDTO metaDTO = new MetaDTO();
        metaDTO.setCount(userCount);
        metaDTO.setLimit(limit);
        metaDTO.setPage(page);
        metaDTO.setNextUrl(UrlBuilder.getNextUsersPageUrl(page, limit));

        userListDTO.setUsers(listOfDTOs);
        userListDTO.setMeta(metaDTO);

        return userListDTO;

    }

    @Transactional
    @Override
    public UserInfoDTO createNewUser(UserInfoDTO userInfoDTO) {

        if (userRepository.existsByUserName(userInfoDTO.getUserName())) {
            throw new UniqueFieldException("User name already taken");
        }

        if (userRepository.existsByEmail(userInfoDTO.getEmail())) {
            throw new UniqueFieldException("Account with email \"" + userInfoDTO.getEmail() + "\" already exists");
        }

        UserInfo userInfo = UserInfoMapper.INSTANCE.userInfoDTOToUserInfo(userInfoDTO);

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String encodedPassword = encoder.encode(userInfoDTO.getClearPassword());

        userInfo.setPassword(encodedPassword);

        Role role = new Role(RolesEnum.ROLE_USER);
        role.setUser(userInfo);

        userInfo.setRoles(Arrays.asList(role));

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO returnDto = convertToDTO(savedUser);

        return returnDto;

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public UserInfoDTO setRole(RolesEnum role, Long id) {

        if (role.getRole() == "ROLE_SUPERADMIN" &&
                !AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_SUPERADMIN")) {
            throw new AccessDeniedException("Only SUPERADMIN can grant this role");
        }

        UserInfo userInfo = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        if (userInfo.getRoles().stream().anyMatch(role1 -> role1.getRole().getRole().equals(role.getRole()))) {
            throw new InvalidUserOperationException("User already has that role");
        }

        userInfo.addRole(new Role(role));

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO userInfoDTO = convertToDTO(savedUser);

        return userInfoDTO;
    }

    @Transactional
    @Override
    public UserInfoDTO activateUser(Long id) {

        UserInfo enabledUser = userRepository.findById(id).map(userInfo -> {
            userInfo.setEnabled(true);
            UserInfo saved = userRepository.save(userInfo);
            return saved;
        }).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        UserInfoDTO userInfoDTO = convertToDTO(enabledUser);

        return userInfoDTO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found")));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN') or authentication.name == #userInfoDTO.userName")
    @Transactional
    @Override
    public UserInfoDTO editUser(UserInfoDTO userInfoDTO, Long id) {

        UserInfo oldUser = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        if (!oldUser.getUserName().equals(userInfoDTO.getUserName())) {
            throw new InvalidUserOperationException("You can not change the user name");
        }

        if (!userInfoDTO.getEmail().equals(oldUser.getEmail()) && userRepository.existsByEmail(userInfoDTO.getEmail())) {
            throw new InvalidUserOperationException("User with email " + userInfoDTO.getEmail() + " already exists");
        }

        UserInfo userInfo = UserInfoMapper.INSTANCE.userInfoDTOToUserInfo(userInfoDTO);
        userInfo.setId(id);
        userInfo.setEnabled(oldUser.getEnabled());
        userInfo.setPassword(oldUser.getPassword());

        UserInfo savedUser = userRepository.save(userInfo);

        UserInfoDTO returnDto = convertToDTO(savedUser);

        return returnDto;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPERADMIN') or hasRole('ROLE_USER')")
    @Override
    public UserInfoDTO getUserById(Long id) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String currentPrincipalName = authentication.getName();
            if (!userInfo.getUserName().equals(currentPrincipalName) &&
                    (!AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_ADMIN") ||
                            !AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_SUPERADMIN"))) {
                throw new InvalidUserOperationException("You are not authorised for this operation");
            }
        }

        UserInfoDTO userInfoDTO = convertToDTO(userInfo);

        return userInfoDTO;
    }

    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    @Transactional
    @Override
    public UserInfoDTO revokeRole(Long id, RolesEnum role) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

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

    @Transactional
    @Override
    public UserInfoDTO resetPassword(Long id, PasswordDTO passwordDTO) {

        UserInfo userInfo = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with id: " + id + " not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!userInfo.getUserName().equals(currentPrincipalName)) {
            throw new InvalidUserOperationException("Wrong id supplied");
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();

        String password = encoder.encode(passwordDTO.getPassword());

        userInfo.setPassword(password);

        userRepository.save(userInfo);

        return convertToDTO(userInfo);
    }

    public UserInfoDTO convertToDTO(UserInfo userInfo) {
        UserInfoDTO userInfoDTO = UserInfoMapper.INSTANCE.userInfoToUserInfoDTO(userInfo);

        userInfoDTO.setUserUrl(UrlBuilder.getUserUrl(userInfo.getId()));

        if (userInfo.getRoles() != null) {

            userInfoDTO.setRoles(convertRolesToRolesDTO(userInfo.getRoles()));

        }

        return userInfoDTO;
    }

    public static List<RoleDTO> convertRolesToRolesDTO(List<Role> roles) {
        return roles
                .stream()
                .map(role -> {
                    RoleDTO roleDTO = UserInfoMapper.INSTANCE.roleToRoleDTO(role);
                    return roleDTO;
                }).collect(Collectors.toList());
    }

}
