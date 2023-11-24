package com.example.custom.controller;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.security.SecurityProvider;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;
    private final ExportService exportService;
    private final SecurityProvider provider;

    @GetMapping("/search")
    public String getSearch(Model model, @RequestParam("token") String token) {
        model.addAttribute("token", token);
        return provider.checkHtml("table/search", token);
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("number") List<String> number, @RequestParam("token") String token) {
        DeclarationDto declarationDto = declarationService.findByNumber(number.get(1));
        if (declarationDto.getMessage().isEmpty()) {
            if (declarationDto.getIsImport()) {
                return provider.check("redirect:/import?number=" + number.get(1) + "&", token);
            }
            return provider.check("redirect:/export?number=" + number.get(1) + "&", token);
        }
        return getErrorSearch(model, token, declarationDto.getMessage());
    }

    @GetMapping("/export")
    public String getExport(Model model, @RequestParam("number") Optional<String> number, @RequestParam("token") String token) {
        DeclarationDto declarationDto = declarationService.findByNumber(number.get());
        AllExportsDto exports = exportService.findExportByDeclarationDto(declarationDto);
        if (exports.getMessage().isEmpty()) {
            model.addAttribute("declaration", declarationDto);
            model.addAttribute("exports", exports.getExports());
            model.addAttribute("number", number.orElse(""));
            model.addAttribute("token", token);

            return provider.checkHtml("table/export", token);
        }
        return getErrorSearch(model, token, exports.getMessage());
    }

    @GetMapping("/import")
    public String getImport(Model model, @RequestParam("number") Optional<String> number, @RequestParam("token") String token) {
        DeclarationDto declarationDto = declarationService.findByNumber(number.get());

//        ExportDto imports = importService.findImportByDeclarationDto(declarationDto);
//        if (imports.getMessage().isEmpty()) {
//            model.addAttribute("declaration", declarationDto);
//            model.addAttribute("imports", imports.getExports());
//            model.addAttribute("number", number.orElse(""));
//            model.addAttribute("token", token);
//
//            return provider.checkHtml("table/import", token);
//        }
        return getErrorSearch(model, token, "imports.getMessage()");
    }

    private String getErrorSearch(Model model, String token, String errorMessage) {
        model.addAttribute("isNotFind", true);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("token", token);
        return provider.checkHtml("table/search", token);
    }

}
