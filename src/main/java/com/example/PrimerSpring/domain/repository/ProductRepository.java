package com.example.PrimerSpring.domain.repository;

import com.example.PrimerSpring.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getAll();
    Optional<List<Product>> getByCategory(int categoryId);
    Optional<List<Product>> getScarseProducts(int stock,boolean state);
    Optional<Product> getProduct (int productId);
    Product save(Product product);
    void delete(int productId);
}
