package group1.commerce.controller;

import group1.commerce.dto.ProductDTO;
import group1.commerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;

@ControllerAdvice // This annotation makes methods in this class global to all controllers
public class GlobalModelAttributes {

    private final ProductService productService;

    @Autowired
    public GlobalModelAttributes(ProductService productService) {
        this.productService = productService;
    }

    // This method will run for every request and add its return value to the model
    @ModelAttribute("headerTopArtists")
    public List<String> addHeaderTopArtists() {
        try {
            return productService.getTopSellingArtistNames(2); // Get top 2 artists
        } catch (Exception e) {
            // Log error e.g., logger.error("Error fetching top artists for header", e);
            return Collections.emptyList(); // Return empty list on error
        }
    }

    @ModelAttribute("headerBestsellers")
    public List<ProductDTO> addHeaderBestsellers() {
        try {
            return productService.getBestSellers(3);
        } catch (Exception e) {
            // Log error
            return Collections.emptyList();
        }
    }

    @ModelAttribute("headerNewReleases")
    public List<ProductDTO> addHeaderNewReleases() {
        try {
            return productService.getNewReleases(3); // Get top 3 new releases
        } catch (Exception e) {
            // Log error
            return Collections.emptyList();
        }
    }

    // You can add other global attributes here if needed
}