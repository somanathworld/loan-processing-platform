package com.gs.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.gs.model.User;

public interface CustomerUserDetailsService extends UserDetailsService {

    String saveUser(User user);
    UserDetails loadUserByUsername(String username);

}