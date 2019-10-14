package com.example.springsocial.controller;

import com.example.springsocial.model.User;
import com.example.springsocial.model.UserInfo;
import com.example.springsocial.repository.UserRepository;
import com.example.springsocial.security.CurrentUser;
import com.example.springsocial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findByIdOrUser(0,userPrincipal.getEmail());
    }

    @GetMapping("/user/doSomething")
    @PreAuthorize("hasRole('USER')")
    public UserInfo doSomething(@CurrentUser UserPrincipal userPrincipal) {
        UserInfo user = UserInfo.builder().mail(userPrincipal.getEmail()).build();
        return user;
    }

}
