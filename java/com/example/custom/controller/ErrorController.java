package com.example.custom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "";

        if (throwable != null) {
            errorMessage = throwable.getMessage();
        }
        model.addAttribute("errorMessage", errorMessage);

        if (status != null) {
            if (Integer.parseInt(status.toString()) == HttpStatus.NOT_FOUND.value()) {
                return "error/error_401";
            }
        }
        return "error/error";
    }

    @RequestMapping("/error_401")
    public String handleError401(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        String errorMessage = "";

        if (throwable != null) {
            errorMessage = throwable.getMessage();
        }

        model.addAttribute("errorMessage", errorMessage);
        return "error/error_401";
    }
}
