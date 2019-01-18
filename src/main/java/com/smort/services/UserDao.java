package com.smort.services;

import com.smort.domain.UserInfo;
import com.smort.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDao {

    private UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfo loadActiveUser(String userName) {
        UserInfo userInfo = userRepository.findByUserName(userName);

        return userInfo.getEnabled() == 1 ? userInfo : null;

    }

}
