package com.example.custom.controller;

import com.example.custom.dto.*;
import com.example.custom.entity.Changelog;
import com.example.custom.migration.DbfToDbMigrator;
import com.example.custom.security.SecurityProvider;
import com.example.custom.service.ChangelogService;
import com.example.custom.service.SettingsService;
import com.example.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String DECLARATION_CHANGELOG_NAME = "Declaration";
    private static final String CONSUMPTION_CHANGELOG_NAME = "Consumption";
    private static final String ARRIVAL_CHANGELOG_NAME = "Arrival";
    private static final String EXPORT_CHANGELOG_NAME = "Export";


    private final UserService userService;
    private final SecurityProvider provider;
    private final ChangelogService changelogService;
    private final DbfToDbMigrator migrator;
    private final SettingsService settingsService;


    @GetMapping("/menu")
    public String getMenu(Model model, @RequestParam("token") String token) {
        model.addAttribute("token", token);
        return provider.checkHtml("admin/menu", token);
    }

    @GetMapping("/user")
    public String getUsers(Model model, @RequestParam("token") String token) {
        model.addAttribute("users", userService.findAllWithoutAdmins());
        model.addAttribute("token", token);
        return provider.checkHtml("admin/user/users", token);
    }

    @GetMapping("/user/new")
    public String saveTemplate(Model model, @RequestParam("token") String token,
                               RedirectAttributes redirectAttributes, @ModelAttribute("user") UserRequestDto requestDto) {
        model.addAttribute("token", token);
        return provider.checkHtml("admin/user/save", token);
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
    public String updateShow(@PathVariable("id") Long id, Model model,
                             @RequestParam("token") String token,
                             RedirectAttributes redirectAttributes,
                             @ModelAttribute("user") UserRequestDto requestDto) {
        UserDto user = userService.findById(id);
        requestDto.setUsername(user.getUsername());
        model.addAttribute("id", id);
        model.addAttribute("token", token);
        return provider.checkHtml("admin/user/update", token);
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

    @GetMapping("/data")
    public String getData(Model model, @RequestParam("token") String token) {
        List<Changelog> changelogList = new ArrayList<>();
        changelogList.add(changelogService.getOrSaveChangelogByName(EXPORT_CHANGELOG_NAME));
        changelogList.add(changelogService.getOrSaveChangelogByName(DECLARATION_CHANGELOG_NAME));
        changelogList.add(changelogService.getOrSaveChangelogByName(ARRIVAL_CHANGELOG_NAME));
        changelogList.add(changelogService.getOrSaveChangelogByName(CONSUMPTION_CHANGELOG_NAME));
        boolean isExist = changelogList.stream().anyMatch(c -> c.getAllCount()>0);
        SettingsDto settings = settingsService.findSettings();
        model.addAttribute("isExist", isExist);
        model.addAttribute("logs", changelogList);
        model.addAttribute("token", token);
        model.addAttribute("isActive", migrator.isActive());
        model.addAttribute("formatter", formatter);
        model.addAttribute("isAutoOn",settings.isOn());
        model.addAttribute("times",settings.getTime());
        model.addAttribute("selectedTime","");
        model.addAttribute("lastUpdate",settings.getLastUpdate());
        return provider.checkHtml("admin/data/data", token);
    }

    @PostMapping("/data/update")
    public String updateData(Model model,
                             @RequestParam("token") String token,
                             RedirectAttributes redirectAttributes) {
        MessageDto messageDto = migrator.migrateLastUpdate();
        model.addAttribute("token", token);
        if (messageDto.getMessage().isEmpty()) {
            return provider.check("redirect:/admin/data?", token);
        }
        redirectAttributes.addFlashAttribute("isNotMigrate", true);
        redirectAttributes.addFlashAttribute("errorMessage", messageDto.getMessage());
        return provider.check("redirect:/admin/data?", token);
    }

    @PostMapping("/data/updateAll")
    public String updateAllData(Model model,
                                @RequestParam("token") String token,
                                RedirectAttributes redirectAttributes) {
        MessageDto messageDto = migrator.migrateAll();
        model.addAttribute("token", token);
        if (messageDto.getMessage().isEmpty()) {
            return provider.check("redirect:/admin/data?", token);
        }
        redirectAttributes.addFlashAttribute("isNotMigrate", true);
        redirectAttributes.addFlashAttribute("errorMessage", messageDto.getMessage());
        return provider.check("redirect:/admin/data?", token);
    }

    @PostMapping("/data/delete")
    public String deleteAllData(Model model,
                                @RequestParam("token") String token,
                                RedirectAttributes redirectAttributes) {
        model.addAttribute("token", token);
        migrator.deleteAll();
        return provider.check("redirect:/admin/data?", token);
    }

    @GetMapping("/cache")
    public String deleteCache(Model model,
                              @RequestParam("token") String token) {
        provider.deleteCaches();
        model.addAttribute("token", token);
        return "redirect:/login";
    }

    @PostMapping("/data/settings")
    public String saveSettings(Model model,
                               @RequestParam("token") String token,
                               @ModelAttribute("isAutoOn") String isAutoOn,
                               @ModelAttribute("selectedTime") String selectedTime,
                               RedirectAttributes redirectAttributes) {
        model.addAttribute("token", token);
        boolean isOn = isAutoOn.equals("on");
        settingsService.updateSettings(isOn, selectedTime);
        return provider.check("redirect:/admin/data?", token);
    }
}
