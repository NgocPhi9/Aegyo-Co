package group1.commerce.controller;

import group1.commerce.dto.ProductDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.User;
import group1.commerce.service.ProductService;
import group1.commerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ControllerAdvice // This annotation makes methods in this class global to all controllers
public class GlobalModelAttributes {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public GlobalModelAttributes(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
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

//    @ModelAttribute("user")
//    public UserDTO currentUser(Authentication authentication) {
//        // ... your existing code ...
//        User user = profileUser(authentication); // We can even reuse our new method
//        if (user != null) {
//            return new UserDTO(user.getIdUser(), user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getAddress(), user.isPurchaseRestricted(), user.getRole());
//        }
//        return null;
//    }
//
//
//    // --- THIS IS THE NEW, IMPORTANT METHOD ---
//    // It adds the full User ENTITY to every page, which your profile layout needs.
//    @ModelAttribute("profileUser")
//    public User profileUser(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return null;
//        }
//
//        // Handle regular form login
//        if (authentication.getPrincipal() instanceof UserDetails) {
//            String email = ((UserDetails) authentication.getPrincipal()).getUsername();
//            return userService.findUserByEmail(email);
//        }
//
//        // Handle social logins (Google, Facebook, etc.)
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
//            Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
//
//            String idProvided = switch (oauthToken.getAuthorizedClientRegistrationId()) {
//                case "google" -> (String) attributes.get("sub");
//                case "facebook" -> (String) attributes.get("id");
//                default -> null;
//            };
//
//            if (idProvided != null) {
//                return userService.findUserByIdProvided(idProvided);
//            }
//        }
//
//        return null; // Fallback
//    }



}