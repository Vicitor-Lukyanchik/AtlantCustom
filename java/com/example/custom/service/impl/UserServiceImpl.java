package com.example.custom.service.impl;

import com.example.custom.converter.dto.UserToDtoConverter;
import com.example.custom.dto.AuthenticationRequestDto;
import com.example.custom.dto.UserDto;
import com.example.custom.dto.UserRequestDto;
import com.example.custom.entity.User;
import com.example.custom.repository.UserRepository;
import com.example.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private static final String ADMIN_VALUE = "admin";
    private static final String INVALID_PASSWORD_MESSAGE = "Неверный пароль";
    private static final String INVALID_ADMIN_USERNAME_MESSAGE = "Логин не может содержать слово : admin";
    private static final String INVALID_PASSWORDS_MESSAGE = "Пароли должны быть идентичными";
    private static final String USER_NOT_FOUND_BY_USERNAME = "Не найден пользователь с логином : ";
    private static final String USER_FOUND_BY_USERNAME = "Найден пользователь с логином : ";
    private static final String USER_NOT_FOUND_BY_ID = "Не найден пользователь с id : ";
    private static final String MIN_SIZE_MESSAGE = " должен составлять не менее 4 символов";
    private static final String LOGIN = "Логин";
    private static final String PASSWORD = "Пароль";
    private static final String CANT_BE_EMPTY = " не может быть пустым";
    private static final String AND = " и ";
    private static final String EMPTY_STRING = "";
    private static final Integer MIN_LOGIN_SIZE = 4;
    private static final Integer MIN_PASSWORD_SIZE = 4;

    private final UserRepository userRepository;
    private final UserToDtoConverter userToDtoConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional
    public UserDto login(AuthenticationRequestDto requestDto) {
        String validatedMessage = generateValidatedMessage(requestDto);
        if (!validatedMessage.isEmpty()) {
            return UserDto.builder().message(validatedMessage).build();
        }
        UserDto userDto = findByUsername(requestDto.getUsername());
        if (userDto.getMessage().isEmpty() && !bCryptPasswordEncoder.matches(requestDto.getPassword(), userDto.getPassword())) {
            userDto.setMessage(INVALID_PASSWORD_MESSAGE);
        }
        return userDto;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAllWithoutAdmins() {
        List<User> users = userRepository.findAll();
        users.removeIf(user -> user.getUsername().contains(ADMIN_VALUE));
        return users;
    }

    @Override
    public UserDto saveUser(UserRequestDto userDto) {
        UserDto validatedUserDto = validateUser(userDto);
        if (!validatedUserDto.getMessage().isEmpty()) {
            return validatedUserDto;
        }
        return userToDtoConverter.convert(userRepository.save(buildUser(userDto)));
    }

    private UserDto validateUser(UserRequestDto userDto){
        String validatedMessage = generateValidatedMessage(AuthenticationRequestDto.builder().username(userDto.getUsername())
                .password(userDto.getPassword()).build());
        UserDto userByUsername = findByUsername(userDto.getUsername());

        if (userByUsername.getMessage().isEmpty() && !userByUsername.getId().equals(userDto.getId())) {
            return UserDto.builder().message(USER_FOUND_BY_USERNAME + userDto.getUsername()).build();
        }

        if (!validatedMessage.isEmpty()) {
            return UserDto.builder().message(validatedMessage).build();
        }
        if(userDto.getUsername().contains(ADMIN_VALUE)){
            return UserDto.builder().message(INVALID_ADMIN_USERNAME_MESSAGE).build();
        }
        if (!userDto.getPassword().equals(userDto.getRepeatPassword())) {
            return UserDto.builder().message(INVALID_PASSWORDS_MESSAGE).build();
        }
        return UserDto.builder().message(EMPTY_STRING).build();
    }

    private String generateValidatedMessage(AuthenticationRequestDto dto) {
        String password = dto.getPassword();
        String username = dto.getUsername();
        String result = EMPTY_STRING;

        if (password.isEmpty()) {
            result = PASSWORD + CANT_BE_EMPTY;
            if (username.isEmpty()) {
                return LOGIN + AND + PASSWORD + CANT_BE_EMPTY;
            } else if (username.length() < MIN_PASSWORD_SIZE) {
                return LOGIN + AND + PASSWORD + MIN_SIZE_MESSAGE;
            }
        } else if (password.length() < MIN_PASSWORD_SIZE) {
            result = PASSWORD + MIN_SIZE_MESSAGE;
            if (username.length() < MIN_LOGIN_SIZE) {
                return LOGIN + AND + result;
            }
        } else if (username.isEmpty()) {
            return LOGIN + CANT_BE_EMPTY;
        } else if (username.length() < MIN_LOGIN_SIZE) {
            return LOGIN + MIN_SIZE_MESSAGE;
        }
        return result;
    }

    private User buildUser(UserRequestDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return user;
    }

    @Override
    public UserDto findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);

        if (result.isEmpty() || !username.equals(result.get().getUsername())) {
            return UserDto.builder().message(USER_NOT_FOUND_BY_USERNAME + username).build();
        }
        return userToDtoConverter.convert(result.get());
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) {
            return UserDto.builder().message(USER_NOT_FOUND_BY_ID + id).build();
        }
        return userToDtoConverter.convert(result.get());
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}
