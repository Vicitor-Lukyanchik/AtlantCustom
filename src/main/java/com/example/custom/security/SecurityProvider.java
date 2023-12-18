package com.example.custom.security;

import com.example.custom.dto.UserDto;

public interface SecurityProvider {

    String check(String path, String token);
    String checkHtml(String path, String token);

    String authenticate(UserDto user);

    void deauthenticate(String token);
}
