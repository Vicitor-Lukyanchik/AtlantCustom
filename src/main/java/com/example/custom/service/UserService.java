package com.example.custom.service;

import com.example.custom.dto.AuthenticationRequestDto;
import com.example.custom.dto.UserDto;

public interface UserService {
    UserDto login(AuthenticationRequestDto requestDto);

    UserDto findByUsername(String username);

    UserDto findById(Long id);

}
