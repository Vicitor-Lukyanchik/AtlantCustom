package com.example.custom.security.impl;

import com.example.custom.dto.UserDto;
import com.example.custom.entity.User;
import com.example.custom.security.SecurityProvider;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Component
public class SecurityProviderImpl implements SecurityProvider {

    private static final String LOGIN_PATH = "redirect:/login";
    private static final String TOKEN_DELIMITER = "_";
    private static Random random = new Random();

    private Map<String, Optional<User>> users = new HashMap<>();

    @Override
    public String check(String path, String token) {

        if (isAuthenticate(token)) {
            return path + "token=" + token;
        }
        return LOGIN_PATH;
    }

    @Override
    public String checkHtml(String path, String token) {
        if (isAuthenticate(token)) {
            return path;
        }
        return LOGIN_PATH;
    }

    private boolean isAuthenticate(String token) {
        if (users.containsKey(token)) {
            return users.get(token).isPresent();
        }
        return false;
    }

    @Override
    public String authenticate(UserDto userDto) {
        String token = generateToken(userDto.getUsername());
        Optional<User> user = Optional.of(User.builder().password(userDto.getPassword()).username(userDto.getUsername()).build());

        while (users.containsKey(token)) {
            token = generateToken(userDto.getUsername());
        }

        users.put(token, user);
        return token;
    }

    private static String generateToken(String username) {
        return random.nextInt(8999) + 1000 + TOKEN_DELIMITER + username;
    }

    @Override
    public void deauthenticate(String token) {
        if (users.containsKey(token)) {
            users.put(token, Optional.empty());
        }
    }
}
