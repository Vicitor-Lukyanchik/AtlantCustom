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

    private static final String TOKEN_PATH_VALUE = "token=";
    private static final String ADMIN_VALUE = "admin";
    private static final String SEARCH_PATH = "redirect:/search?";
    private static final String ADMIN_PATH = "redirect:/admin/menu?";
    private static final String ERROR_PATH = "redirect:/error_401";
    private static final String TOKEN_DELIMITER = "_";
    private static Random random = new Random();

    private Map<String, Optional<User>> users = new HashMap<>();

    @Override
    public String check(String path, String token) {
        if (isAuthenticate(token)) {
            if (users.get(token).get().getUsername().contains(ADMIN_VALUE) && path.equals("redirect:/search?")) {
                return ADMIN_PATH+ TOKEN_PATH_VALUE + token;
            }
            return path + TOKEN_PATH_VALUE + token;
        }

        return ERROR_PATH;
    }

    @Override
    public String checkHtml(String path, String token) {
        if (isAuthenticate(token)) {
            return path;
        }
        return ERROR_PATH;
    }

    private boolean isAuthenticate(String token) {
        if (users.containsKey(token)) {
            return users.get(token).isPresent();
            //return true;
        }
        return false;
    }

    @Override
    public String authenticate(UserDto userDto) {
        if(isAdmin(userDto) && isAdminActive()){
            return ERROR_PATH;
        }

        String token = generateToken(userDto.getUsername());
        Optional<User> user = Optional.of(User.builder().password(userDto.getPassword()).username(userDto.getUsername()).build());

        if (users.size() > 100){
            deleteCaches();
        }

        while (users.containsKey(token)) {
            token = generateToken(userDto.getUsername());
        }

        users.put(token, user);
        return token;
    }

    private boolean isAdmin(UserDto userDto){
        return userDto.getUsername().contains(ADMIN_VALUE);
    }

    private boolean isAdminActive(){
        for (Map.Entry<String, Optional<User>> user : users.entrySet()) {
           if(user.getValue().get().getUsername().contains(ADMIN_VALUE)){
               //return true;(Admin can come if he on site now)
               return false;
           }
        }
        return false;
    }

    private static String generateToken(String username) {
        return random.nextInt(89999) + 10000 + TOKEN_DELIMITER + username;
    }

    @Override
    public void deauthenticate(String token) {
        users.remove(token);
    }

    @Override
    public void deleteCaches() {
        this.users = new HashMap<>();
    }
}
