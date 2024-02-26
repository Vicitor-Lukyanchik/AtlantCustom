package com.example.custom.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
public class UserRequestDto {

    private Long id;

    @Size(min = 6, max = 50, message = "Username should be more then 4 and less than 50")
    private String username;

    @Size(min = 8, max = 60, message = "Password should be more then 8 and less than 60")
    private String password;

    @Size(min = 8, max = 60, message = "Password should be more then 8 and less than 60")
    private String repeatPassword;

    private String message;
}