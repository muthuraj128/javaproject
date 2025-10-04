package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Category;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findActiveProducts() {
        return productRepository.findActiveProductsOrderByName();
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public long count() {
        return productRepository.count();
    }

    public long countActive() {
        return productRepository.countByActiveTrue();
    }

    public long countByCategory(Category category) {
        return productRepository.countByCategoryAndActiveTrue(category);
    }

    // Additional methods for public website
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findByActiveTrueOrderByName(pageable);
    }

    public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
        Category category = new Category();
        category.setId(categoryId);
        return productRepository.findByCategoryAndActiveTrueOrderByName(category, pageable);
    }

    public Page<Product> searchProducts(String searchTerm, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrueOrderByName(searchTerm, pageable);
    }
}