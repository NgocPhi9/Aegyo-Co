package group1.commerce.controller;

import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.Product;
import group1.commerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/4Moos")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public String getAllProducts(Model model) {
        List<ProductDTO> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "index";
    }

    // Get product by id
    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable String id, Model model) {
        Optional<ProductDTO> product = productService.getProductById(id);
        product.map(productDTO -> model.addAttribute("product", productDTO));
        return "product";
    }

    // Save a product from DTO
    public ProductDTO saveProduct(ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return productDTO;
    }

    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return productDTO;
    }

    // Delete a product
    public void deleteProduct(String idProduct) {
        productService.deleteProduct(idProduct);
    }

}
