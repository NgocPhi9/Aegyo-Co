package group1.commerce.service;

import group1.commerce.entity.Product;
import group1.commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findProductsByProductName(category);
    }

    public Product getProductById(String id) {
        return productRepository.findProductsByIdProduct(id);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
