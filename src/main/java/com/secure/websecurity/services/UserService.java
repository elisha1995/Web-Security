package com.secure.websecurity.services;

import com.secure.websecurity.dtos.UserDTO;
import com.secure.websecurity.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

    User findByUsername(String username);
}

