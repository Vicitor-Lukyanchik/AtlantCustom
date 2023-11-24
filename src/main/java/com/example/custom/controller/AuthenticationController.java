package com.example.custom.controller;

import com.example.custom.dto.AuthenticationRequestDto;
import com.example.custom.dto.UserDto;
import com.example.custom.entity.User;
import com.example.custom.security.SecurityProvider;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.ExportService;
import com.example.custom.service.UnitService;
import com.example.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final SecurityProvider provider;


    @GetMapping()
    public String loginTemplate(Model model, @ModelAttribute("login") AuthenticationRequestDto requestDto,
                                @RequestParam("token") Optional<String> token) {
        token.ifPresent(provider::deauthenticate);
        return "authentication/login";
    }

    @PostMapping()
    public String login(@Valid @ModelAttribute AuthenticationRequestDto requestDto,
                        RedirectAttributes redirectAttributes, HttpServletResponse httpServletResponse) {
        httpServletResponse.addHeader("Hello","Hello");
        UserDto userDto = userService.login(requestDto);
        if (userDto.getMessage().isEmpty()) {
            String token = provider.authenticate(userDto);
            return provider.check("redirect:/search?", token);
        } else {
            redirectAttributes.addFlashAttribute("isNotLogin", true);
            redirectAttributes.addFlashAttribute("errorMessage", userDto.getMessage());
            return "redirect:/login";
        }
    }
}
