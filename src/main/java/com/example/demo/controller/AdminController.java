package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.error.TestIntentionalException;
import com.example.demo.entity.Product;
import com.example.demo.entity.Category;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import java.math.BigDecimal;

@Controller
public class AdminController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("productCount", productService.count());
        model.addAttribute("categoryCount", categoryService.count());
        model.addAttribute("activeProductCount", productService.countActive());
        model.addAttribute("activeCategoryCount", categoryService.countActive());
        return "admin";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // === PRODUCT MANAGEMENT ===
    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", productService.findAll());
        model.addAttribute("categories", categoryService.findActiveCategories());
        return "admin_products";
    }

    @GetMapping("/admin/products/new")
    public String newProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findActiveCategories());
        return "admin_product_form";
    }

    @GetMapping("/admin/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findActiveCategories());
        return "admin_product_form";
    }

    @PostMapping("/admin/products/save")
    public String saveProduct(@Valid @ModelAttribute Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findActiveCategories());
            return "admin_product_form";
        }
        productService.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    // === CATEGORY MANAGEMENT ===
    @GetMapping("/admin/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "admin_categories";
    }

    @GetMapping("/admin/categories/new")
    public String newCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin_category_form";
    }

    @GetMapping("/admin/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found"));
        model.addAttribute("category", category);
        return "admin_category_form";
    }

    @PostMapping("/admin/categories/save")
    public String saveCategory(@Valid @ModelAttribute Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "admin_category_form";
        }
        try {
            categoryService.save(category);
        } catch (IllegalArgumentException e) {
            result.rejectValue("name", "error.category", e.getMessage());
            return "admin_category_form";
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/test-error")
    public String testError() {
        throw new TestIntentionalException("Intentional test error to verify custom error handling");
    }
}
