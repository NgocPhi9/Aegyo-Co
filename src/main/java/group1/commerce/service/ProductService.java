package group1.commerce.service;

import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.Product;
import group1.commerce.mapper.ProductMapper;
import group1.commerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // Get all products as DTOs
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.toDTOList(products);
    }

    // Get product by ID as DTO
    public Optional<ProductDTO> getProductById(String idProduct) {
        return productRepository.findById(idProduct).map(productMapper::toDTO);
    }


    // Save a product from DTO
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDTO(updatedProduct);
    }

    // Delete a product
    public void deleteProduct(String idProduct) {
        productRepository.deleteById(idProduct);
    }

}
