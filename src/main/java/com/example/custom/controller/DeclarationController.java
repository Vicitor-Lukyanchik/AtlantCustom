package com.example.custom.controller;

import com.example.custom.dto.DeclarationDto;
import com.example.custom.dto.AllExportsDto;
import com.example.custom.dto.ImportByStorehouseDto;
import com.example.custom.dto.ImportDto;
import com.example.custom.security.NumberRequestProvider;
import com.example.custom.security.SecurityProvider;
import com.example.custom.service.DeclarationService;
import com.example.custom.service.ExportService;
import com.example.custom.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DeclarationController {

    private final DeclarationService declarationService;
    private final ImportService importService;
    private final ExportService exportService;
    private final SecurityProvider provider;
    private final NumberRequestProvider numberProvider;

    @GetMapping("/search")
    public String getSearch(Model model, @RequestParam("token") String token) {
        model.addAttribute("token", token);
        return provider.checkHtml("table/search", token);
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam("number") List<String> number, @RequestParam("token") String token) {
        DeclarationDto declarationDto = declarationService.findByNumber(number.get(1));
        if (declarationDto.getMessage().isEmpty()) {
            numberProvider.add(token, number.get(1));
            if (declarationDto.getIsImport()) {
                return provider.check("redirect:/import?", token);
            }
            return provider.check("redirect:/export?", token);
        }
        return getErrorSearch(model, token, declarationDto.getMessage());
    }

    @GetMapping("/export")
    public String getExport(Model model, @RequestParam("token") String token) {
        String number = numberProvider.take(token);

        DeclarationDto declarationDto = declarationService.findByNumber(number);
        AllExportsDto exports = exportService.findExportByDeclarationDto(declarationDto);
        if (exports.getMessage().isEmpty()) {
            model.addAttribute("declaration", declarationDto);
            model.addAttribute("exports", exports.getExports());
            model.addAttribute("number", number);
            model.addAttribute("token", token);

            return provider.checkHtml("table/export", token);
        }
        return getErrorSearch(model, token, exports.getMessage());
    }

    @GetMapping("/import")
    public String getImport(Model model, @RequestParam("token") String token) {
        String number = numberProvider.take(token);

        DeclarationDto declarationDto = declarationService.findByNumber(number);
        ImportDto importDto = importService.findImportByDeclarationDto(declarationDto);
        if (importDto.getMessage().isEmpty()) {
            model.addAttribute("declaration", declarationDto);
            model.addAttribute("arrivals", importDto.getArrivals());
            model.addAttribute("consumptions", importDto.getConsumptions());
            model.addAttribute("number", number);
            model.addAttribute("token", token);

            return provider.checkHtml("table/import", token);
        }
        return getErrorSearch(model, token, "imports.getMessage()");
    }

    @GetMapping("/import1")
    public String getImportByStorehouse(Model model, @RequestParam("token") String token) {
        String number = numberProvider.take(token);

        DeclarationDto declarationDto = declarationService.findByNumber(number);
        ImportByStorehouseDto importDto = importService.findImportByDeclarationDistributedByStorehouseDto(declarationDto);
        if (importDto.getMessage().isEmpty()) {
            model.addAttribute("declaration", declarationDto);
            model.addAttribute("arrivals", importDto.getArrivals());
            model.addAttribute("consumptions", importDto.getConsumptions());
            model.addAttribute("number", number);
            model.addAttribute("token", token);

            return provider.checkHtml("table/import1", token);
        }
        return getErrorSearch(model, token, "imports.getMessage()");
    }

    private String getErrorSearch(Model model, String token, String errorMessage) {
        model.addAttribute("isNotFind", true);
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("token", token);
        return provider.checkHtml("table/search", token);
    }
}
