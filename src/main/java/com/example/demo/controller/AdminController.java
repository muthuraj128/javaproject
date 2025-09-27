package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.error.TestIntentionalException;

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

    @GetMapping("/admin/products")
    public String products() {
        return "admin_products"; // admin_products.html
    }

    @GetMapping("/admin/categories")
    public String categories() {
        return "admin_categories"; // admin_categories.html
    }

    @GetMapping("/admin/test-error")
    public String testError() {
        throw new TestIntentionalException("Intentional test error to verify custom error handling");
    }
}
