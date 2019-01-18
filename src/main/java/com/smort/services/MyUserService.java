package com.smort.services;

import com.smort.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class MyUserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public MyUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserInfo activeUserInfo = userDao.loadActiveUser(userName);

        GrantedAuthority authority = new SimpleGrantedAuthority(activeUserInfo.getRole());

        UserDetails userDetails = (UserDetails) new User(activeUserInfo.getUserName(), activeUserInfo.getPassword(), Arrays.asList(authority));

        return userDetails;
    }
}
