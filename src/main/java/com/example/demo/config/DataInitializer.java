package com.example.demo.config;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // Only initialize if database is empty
        if (categoryService.count() == 0) {
            initializeData();
        }
    }

    private void initializeData() {
        // Create Categories
        Category electronics = new Category("Electronics", "Electronic devices and gadgets");
        Category clothing = new Category("Clothing", "Apparel and fashion items");
        Category books = new Category("Books", "Books and educational materials");
        Category home = new Category("Home & Garden", "Home improvement and garden supplies");
        
        categoryService.save(electronics);
        categoryService.save(clothing);
        categoryService.save(books);
        categoryService.save(home);

        // Create Products
        Product smartphone = new Product("Smartphone", "Latest model smartphone with advanced features", new BigDecimal("699.99"));
        smartphone.setCategory(electronics);
        smartphone.setStockQuantity(25);
        smartphone.setImageUrl("/images/smartphone.jpg");
        productService.save(smartphone);

        Product laptop = new Product("Gaming Laptop", "High-performance laptop for gaming and work", new BigDecimal("1299.99"));
        laptop.setCategory(electronics);
        laptop.setStockQuantity(15);
        laptop.setImageUrl("/images/laptop.jpg");
        productService.save(laptop);

        Product headphones = new Product("Wireless Headphones", "Premium noise-canceling wireless headphones", new BigDecimal("199.99"));
        headphones.setCategory(electronics);
        headphones.setStockQuantity(50);
        headphones.setImageUrl("/images/headphones.jpg");
        productService.save(headphones);

        Product tshirt = new Product("Cotton T-Shirt", "Comfortable 100% cotton t-shirt", new BigDecimal("24.99"));
        tshirt.setCategory(clothing);
        tshirt.setStockQuantity(100);
        tshirt.setImageUrl("/images/tshirt.jpg");
        productService.save(tshirt);

        Product jeans = new Product("Denim Jeans", "Classic blue denim jeans", new BigDecimal("79.99"));
        jeans.setCategory(clothing);
        jeans.setStockQuantity(75);
        jeans.setImageUrl("/images/jeans.jpg");
        productService.save(jeans);

        Product novel = new Product("Programming Guide", "Complete guide to modern programming", new BigDecimal("39.99"));
        novel.setCategory(books);
        novel.setStockQuantity(30);
        novel.setImageUrl("/images/programming-book.jpg");
        productService.save(novel);

        Product cookbook = new Product("Healthy Recipes", "Collection of healthy and delicious recipes", new BigDecimal("29.99"));
        cookbook.setCategory(books);
        cookbook.setStockQuantity(40);
        cookbook.setImageUrl("/images/cookbook.jpg");
        productService.save(cookbook);

        Product plant = new Product("Indoor Plant", "Beautiful low-maintenance indoor plant", new BigDecimal("19.99"));
        plant.setCategory(home);
        plant.setStockQuantity(60);
        plant.setImageUrl("/images/plant.jpg");
        productService.save(plant);

        Product toolset = new Product("Tool Set", "Complete home improvement tool set", new BigDecimal("149.99"));
        toolset.setCategory(home);
        toolset.setStockQuantity(20);
        toolset.setImageUrl("/images/tools.jpg");
        productService.save(toolset);

        // Create one inactive product for testing
        Product outOfStock = new Product("Vintage Camera", "Classic film camera (discontinued)", new BigDecimal("299.99"));
        outOfStock.setCategory(electronics);
        outOfStock.setStockQuantity(0);
        outOfStock.setActive(false);
        outOfStock.setImageUrl("/images/camera.jpg");
        productService.save(outOfStock);

        System.out.println("Sample data initialized: " + categoryService.count() + " categories, " + productService.count() + " products");
    }
}