package com.example.custom.service.impl;

import com.example.custom.converter.UserToDtoConverter;
import com.example.custom.dto.AuthenticationRequestDto;
import com.example.custom.dto.UserDto;
import com.example.custom.entity.User;
import com.example.custom.repository.UserRepository;
import com.example.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private static final String INVALID_PASSWORD_MESSAGE = "Неверный пароль";
    private static final String USER_NOT_FOUND_BY_USERNAME = "Не найден пользователь с логином : ";
    private static final String USER_NOT_FOUND_BY_ID = "Не найден пользователь с id : ";
    private static final String MIN_SIZE_MESSAGE = " должен составлять не менее 6 символов";
    private static final String LOGIN = "Логин";
    private static final String PASSWORD = "Пароль";
    private static final String CANT_BE_EMPTY = " не может быть пустым";
    private static final String AND = " и ";
    private static final String EMPTY_STRING = "";
    private static final Integer MIN_LOGIN_SIZE = 3;
    private static final Integer MIN_PASSWORD_SIZE = 3;

    private final UserRepository userRepository;
    private final UserToDtoConverter userToDtoConverter;

    @Override
    @Transactional
    public UserDto login(AuthenticationRequestDto requestDto) {
        String validatedMessage = generateValidatedMessage(requestDto);
        if (!validatedMessage.isEmpty()) {
            return UserDto.builder().message(validatedMessage).build();
        }

        UserDto userDto = findByUsername(requestDto.getUsername());
        if (userDto.getMessage().isEmpty() && !userDto.getPassword().equals(requestDto.getPassword())) {
            userDto.setMessage(INVALID_PASSWORD_MESSAGE);
        }

        return userDto;
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
        if (!result.isPresent()) {
            return UserDto.builder().message(USER_NOT_FOUND_BY_ID + id).build();
        }
        return userToDtoConverter.convert(result.get());
    }
}
