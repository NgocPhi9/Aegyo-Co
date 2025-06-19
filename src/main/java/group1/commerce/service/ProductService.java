package group1.commerce.service;

import group1.commerce.dto.CreateProductDTO;
import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.Product;
import group1.commerce.entity.ProductDetails;
import group1.commerce.entity.ProductSortOption;
import group1.commerce.mapper.ProductMapper;
import group1.commerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void createProduct(CreateProductDTO createProductDTO) {
        // --- 1. Handle File Upload ---
        MultipartFile image = createProductDTO.getImageLink();
        String storageFileName = ""; // Default to empty string if no file

        // Check if a file was actually uploaded
        if (image != null && !image.isEmpty()) {
            try {
                // Define the directory to save images
                String uploadDir = "public/images/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                storageFileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

                // Save the file
                try (InputStream inputStream = image.getInputStream()) {
                    Path filePath = uploadPath.resolve(storageFileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                // In a real application, you should handle this exception more gracefully
                // (e.g., log it, throw a custom exception).
                throw new RuntimeException("Could not save image file: " + image.getOriginalFilename(), e);
            }
        }
        Product product = productMapper.toEntity(createProductDTO, storageFileName);

        productRepository.save(product);
    }

    public void updateProduct(ProductDTO dto, MultipartFile newImageFile) {
        Product product = productRepository.findById(dto.getIdProduct()).orElse(null);
        String uploadDir = "public/images/";
        String newImageFileName = null;

        if (newImageFile != null && !newImageFile.isEmpty()) {
            String oldImageFileName = product.getImageLink();

            newImageFileName = System.currentTimeMillis() + "_" + newImageFile.getOriginalFilename();

            try {
                Path uploadPath = Paths.get(uploadDir);
                if(!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = newImageFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(newImageFileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Could not save image file: " + newImageFile.getOriginalFilename(), e);
            }

            if(oldImageFileName != null && !oldImageFileName.isEmpty()) {
                try {
                    Path oldImagePath = Paths.get(uploadDir + oldImageFileName);
                    Files.deleteIfExists(oldImagePath);
                }
                catch (IOException e) {
                    throw new RuntimeException("Could not delete old image file: " + oldImageFileName, e);
                }
            }
        }
        product.setProductName(dto.getProductName());
        product.setCategory(dto.getCategory());
        product.setArtist(dto.getArtist());
        product.setDescription(dto.getDescription());
        product.setCogs(dto.getCogs());

        // If a new image was uploaded, update the entity's imageLink field
        if (newImageFileName != null) {
            product.setImageLink(newImageFileName);
        }

        ProductDetails details = product.getProductDetails();
        if (details != null) {
            details.setPrice(dto.getPrice());
            details.setAvailableQuantity(dto.getAvailableQuantity());
            // Note: We don't update fields like soldQuantity or view from the edit form
        }

        // 4. Save the MODIFIED entity. JPA will generate an UPDATE statement.
        productRepository.save(product);
    }


    // Delete a product
    public void deleteProduct(String idProduct) {
        Product product = productRepository.findById(idProduct).get();
        ProductDetails details = product.getProductDetails();
        if (details != null) {
            // Important: Break the link from the parent side before deleting the child.
            product.setProductDetails(null);
        }
        Path filePath = Paths.get("public/images/" + product.getImageLink());
        try {
            Files.deleteIfExists(filePath);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not delete image file: " + product.getImageLink(), e);
        }
        productRepository.delete(product);
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
        saveProduct(productDTO);
    }

    public void shipProduct(String idProduct, int quantity) {
        ProductDTO productDTO = getProductById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found "));
        productDTO.setAvailableQuantity(productDTO.getAvailableQuantity() - quantity);
        saveProduct(productDTO);
    }

    public void returnProduct(String idProduct, int quantity) {
        ProductDTO productDTO = getProductById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found "));
        productDTO.setAvailableQuantity(productDTO.getAvailableQuantity() + quantity);
        saveProduct(productDTO);
    }

}
