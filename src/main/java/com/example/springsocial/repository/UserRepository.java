package com.example.springsocial.repository;

import com.example.springsocial.model.User;
import com.example.springsocial.model.UserInfo;
import com.example.springsocial.model.UserRequest;

import java.util.List;

public interface UserRepository {

    User create(UserRequest userRequest);

    void update(UserRequest userRequest);

    void delete(String email);

    List<User> getAll();

    List<UserInfo> getAllEnabled();

    User findByIdOrUser(int userId, String mail);
    Boolean existsByEmail(String email);


    void enabledDisabled(int userId);

}
