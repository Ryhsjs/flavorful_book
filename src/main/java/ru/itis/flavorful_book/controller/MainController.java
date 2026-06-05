package ru.itis.flavorful_book.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }
}
