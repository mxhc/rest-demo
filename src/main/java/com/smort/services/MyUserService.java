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

import java.util.List;
import java.util.stream.Collectors;

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

        List<GrantedAuthority> authorities = activeUserInfo.getRoles().stream().map(role -> {
            GrantedAuthority authority = new SimpleGrantedAuthority(role.getRole().toString());
            return authority;
        }).collect(Collectors.toList());

        UserDetails userDetails = (UserDetails) new User(activeUserInfo.getUserName(), activeUserInfo.getPassword(), authorities);

        return userDetails;
    }
}
