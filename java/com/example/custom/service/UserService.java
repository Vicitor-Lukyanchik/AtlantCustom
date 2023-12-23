package com.example.custom.service;

import com.example.custom.dto.AuthenticationRequestDto;
import com.example.custom.dto.UserDto;
import com.example.custom.dto.UserRequestDto;
import com.example.custom.entity.User;

import java.util.List;

public interface UserService {
    UserDto login(AuthenticationRequestDto requestDto);

    List<User> findAll();

    List<User> findAllWithoutAdmins();

    UserDto saveUser(UserRequestDto userDto);

    UserDto findByUsername(String username);

    UserDto findById(Long id);

    void deleteById(Long id);
}
