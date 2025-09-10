package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin"; // Thymeleaf template admin.html
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // login.html
    }
}
