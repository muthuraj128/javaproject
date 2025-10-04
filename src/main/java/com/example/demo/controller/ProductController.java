package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.Category;
import com.example.demo.service.ProductService;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage;

        if (categoryId != null) {
            productPage = productService.findByCategory(categoryId, pageable);
            model.addAttribute("selectedCategoryId", categoryId);
        } else if (search != null && !search.trim().isEmpty()) {
            productPage = productService.searchProducts(search, pageable);
            model.addAttribute("searchTerm", search);
        } else {
            productPage = productService.findAllActive(pageable);
        }

        List<Category> categories = categoryService.findAllActive();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);

        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Optional<Product> productOpt = productService.findById(id);
        
        if (productOpt.isEmpty() || !productOpt.get().isActive()) {
            return "redirect:/products";
        }

        Product product = productOpt.get();
        List<Product> relatedProducts = productService.findByCategory(
            product.getCategory().getId(), PageRequest.of(0, 4)
        ).getContent();
        
        // Remove current product from related products
        relatedProducts.removeIf(p -> p.getId().equals(id));

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }

    @GetMapping("/categories/{id}/products")
    public String productsByCategory(@PathVariable Long id, 
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "12") int size,
                                   Model model) {
        
        Optional<Category> categoryOpt = categoryService.findById(id);
        if (categoryOpt.isEmpty() || !categoryOpt.get().isActive()) {
            return "redirect:/products";
        }

        Category category = categoryOpt.get();
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage = productService.findByCategory(id, pageable);
        List<Category> allCategories = categoryService.findAllActive();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", allCategories);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedCategoryId", id);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());

        return "products";
    }
}