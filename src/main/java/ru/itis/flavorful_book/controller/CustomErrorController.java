package ru.itis.flavorful_book.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusAttr = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusAttr == null) {
            return "errors/500";
        }
        int status = Integer.parseInt(statusAttr.toString());
        model.addAttribute("statusCode", status);

        if (status == HttpStatus.NOT_FOUND.value()) return "errors/404";
        if (status == HttpStatus.FORBIDDEN.value()) return "errors/403";
        if (status == HttpStatus.BAD_REQUEST.value()) return "errors/400";
        if (status == HttpStatus.METHOD_NOT_ALLOWED.value()) return "errors/405";
        return "errors/500";
    }
}
