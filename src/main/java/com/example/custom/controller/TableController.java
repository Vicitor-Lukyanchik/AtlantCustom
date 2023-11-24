package com.example.custom.controller;

import com.example.custom.security.SecurityProvider;
import com.example.custom.test.entity.Country;
import com.example.custom.test.service.TestService;
import com.example.custom.test.service.TestServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TableController {

    private final SecurityProvider provider;

    private final TestService testService;

    @GetMapping("/table1")
    public String index(Model model, @RequestParam("number") Optional<String> number, @RequestParam("token") String token) {
        List<Country> countries = testService.getCountries();

        model.addAttribute("countries", countries.subList(1,8));
        model.addAttribute("number", number.orElse(""));
        model.addAttribute("token", token);

        return provider.checkHtml("test/table", token);
    }

    @GetMapping("/search1")
    public String getSearch(Model model, @RequestParam("token") String token) {
        model.addAttribute("token", token);
        return provider.checkHtml("table/search",token);
    }

    @PostMapping("/search1")
    public String search(Model model, @RequestParam("number") List<String> number, @RequestParam("token") String token) {
        String errorMessage = "";
        if (errorMessage.isEmpty()) {
            return provider.check("redirect:/table?number=" + number.get(1) + "&", token);
        }
        model.addAttribute("isNotFind", true);
        model.addAttribute("errorMessage", "Произошла ошибка");
        model.addAttribute("token", token);
        return provider.checkHtml("table/search", token);
    }
}
