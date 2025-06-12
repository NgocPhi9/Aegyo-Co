package group1.commerce.service;

import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.Product;
import group1.commerce.entity.ProductSortOption;
import group1.commerce.mapper.ProductMapper;
import group1.commerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
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
    public Page<ProductDTO> getAllProducts(int page, int size, String sortOptionString) {
        ProductSortOption sortOption = ProductSortOption.fromString(sortOptionString, ProductSortOption.BEST_SELLING);
        Pageable pageable = PageRequest.of(page -1, size, sortOption.getSort());
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDTO);
    }


    // Get product by ID as DTO
    public Optional<ProductDTO> getProductById(String idProduct) {
        return productRepository.findById(idProduct).map(productMapper::toDTO);
    }

    public List<ProductDTO> getBestSellers(int limit) {
        Pageable topN = PageRequest.of(0, limit);
        List<Product> bestSellers = productRepository.findTopBestsellers(topN);
        return productMapper.toDTOList(bestSellers);
    }

    public List<ProductDTO> getFeaturedProducts(int limit) {
        Pageable topN = PageRequest.of(0, limit);
        List<Product> featuredProducts = productRepository.findFeaturedProducts(topN);
        return productMapper.toDTOList(featuredProducts);
    }

    public List<ProductDTO> getNewReleases(int limit) {
        Pageable topN = PageRequest.of(0, limit);
        List<Product> featuredProducts = productRepository.findNewProducts(topN);
        return productMapper.toDTOList(featuredProducts);
    }

    public Page<ProductDTO> getProductsByCategory(String category, int page, int size, String sortOptionString) {
        ProductSortOption sortOption = ProductSortOption.fromString(sortOptionString, ProductSortOption.BEST_SELLING);
        Pageable pageable = PageRequest.of(page -1, size, sortOption.getSort());
        Page<Product> products = productRepository.findProductsByCategory(category, pageable);
        return products.map(productMapper::toDTO);
    }

    public Page<ProductDTO> getProductsByArtist(String artist, int page, int size, String sortOptionString) {
        ProductSortOption sortOption = ProductSortOption.fromString(sortOptionString, ProductSortOption.BEST_SELLING);
        Pageable pageable = PageRequest.of(page -1, size, sortOption.getSort());
        Page<Product> products = productRepository.findProductsByArtist(artist, pageable);
        return products.map(productMapper::toDTO);
    }


    public List<String> getTopSellingArtistNames(int limit) {
        Pageable topN = PageRequest.of(0, limit);
        List<String> topSellingArtists = productRepository.findTopSellingArtistNames(topN);
        return topSellingArtists;
    }

    public List<String> getAllDistinctArtistNames() {
        List<String> names = productRepository.findAllArtist();
        return names != null ? names : Collections.emptyList(); // Ensure non-null
    }

    // Save a product from DTO
    public void saveProduct(ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
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

    public void viewProduct(String idProduct) {
        ProductDTO productDTO = getProductById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found "));
        productDTO.setView(productDTO.getView() + 1);
        saveProduct(productDTO);
    }

    public void sellProduct(String idProduct, int quantity) {
        ProductDTO productDTO = getProductById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found "));
        productDTO.setSoldQuantity(productDTO.getSoldQuantity() + quantity);
        productDTO.setAvailableQuantity(productDTO.getAvailableQuantity() - quantity);
        saveProduct(productDTO);
    }

}
