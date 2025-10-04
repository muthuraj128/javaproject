package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByActiveTrue();
    
    List<Product> findByCategoryAndActiveTrue(Category category);
    
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.name ASC")
    List<Product> findActiveProductsOrderByName();
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.active = true")
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(@Param("name") String name);
    
    long countByActiveTrue();
    
    long countByCategoryAndActiveTrue(Category category);
    
    // Pageable versions for public website
    Page<Product> findByActiveTrueOrderByName(Pageable pageable);
    
    Page<Product> findByCategoryAndActiveTrueOrderByName(Category category, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:searchTerm% AND p.active = true ORDER BY p.name ASC")
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrueOrderByName(@Param("searchTerm") String searchTerm, Pageable pageable);
}