package com.example.custom.controller;

import com.example.custom.dto.*;
import com.example.custom.security.SecurityProvider;
import com.example.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final SecurityProvider provider;

    @GetMapping("/user")
    public String getExport(Model model, @RequestParam("token") String token) {
        model.addAttribute("users", userService.findAllWithoutAdmins());
        model.addAttribute("token", token);
        return provider.checkHtml("user/index", token);
    }

    @GetMapping("/user/new")
    public String saveTemplate(Model model, @RequestParam("token") String token,
                               RedirectAttributes redirectAttributes, @ModelAttribute("user") UserRequestDto requestDto) {
        model.addAttribute("token", token);
        return provider.checkHtml("user/save", token);
    }

    @PostMapping("/user/new")
    public String save(@ModelAttribute("user") UserRequestDto requestDto,
                       Model model, @RequestParam("token") String token,
                       RedirectAttributes redirectAttributes) {
        UserDto userDto = userService.saveUser(requestDto);
        if (userDto.getMessage().isEmpty()) {
            return provider.check("redirect:/admin/user?", token);
        }
        redirectAttributes.addFlashAttribute("isNotLogin", true);
        redirectAttributes.addFlashAttribute("errorMessage", userDto.getMessage());
        return provider.check("redirect:/admin/user/new?", token);
    }

    @GetMapping("/user/update/{id}")
    public String updateShow(@PathVariable("id") Long id,Model model,
                             @RequestParam("token") String token,
                             RedirectAttributes redirectAttributes,
                             @ModelAttribute("user") UserRequestDto requestDto) {
        UserDto user = userService.findById(id);
        requestDto.setUsername(user.getUsername());
        model.addAttribute("id", id);
        model.addAttribute("token", token);
        return provider.checkHtml("user/update", token);
    }

    @PostMapping("/user/update/{id}")
    public String update(@PathVariable("id") Long id, Model model,
                         @RequestParam("token") String token,
                         RedirectAttributes redirectAttributes,
                         @ModelAttribute UserRequestDto requestDto) {
        requestDto.setId(id);
        UserDto userDto = userService.saveUser(requestDto);
        if (userDto.getMessage().isEmpty()) {
            return provider.check("redirect:/admin/user?", token);
        }
        redirectAttributes.addFlashAttribute("isNotLogin", true);
        redirectAttributes.addFlashAttribute("errorMessage", userDto.getMessage());
        return provider.check("redirect:/admin/user/update/" + id + "?", token);

    }

    @GetMapping("/user/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model, @RequestParam("token") String token) {
        userService.deleteById(id);
        return provider.check("redirect:/admin/user?", token);
    }
}
