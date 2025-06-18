package group1.commerce.controller;

import group1.commerce.dto.CreateProductDTO;
import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.ProductSortOption;
import group1.commerce.repository.ProductRepository;
import group1.commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/4Moos/admin/products")
public class AdminProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    public AdminProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }


    private void addPaginationAttributesToModel(Model model, Page<ProductDTO> productsPage, String listAttributeName, int currentViewPage, String currentSortOption, String baseUrl) {
        List<ProductDTO> contentList = productsPage.getContent();

        model.addAttribute("currentPage", currentViewPage);
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute(listAttributeName, contentList);

        // For the sort fragment
        model.addAttribute("currentSortOption", currentSortOption);
        model.addAttribute("sortOptions", Arrays.asList(ProductSortOption.values()));
        model.addAttribute("baseUrl", baseUrl);

    }

    // Get all products
    @GetMapping("/{page}")
    public String getAllProducts(@PathVariable(value = "page") int page,
                                 @RequestParam(name = "sort", defaultValue = "BEST_SELLING") String sortOptionString, Model model) {
        int pageSize = 16;
        Page<ProductDTO> productsPage = productService.getAllProducts(page, pageSize, sortOptionString);
        addPaginationAttributesToModel(model, productsPage, "products", page, sortOptionString, "/4Moos/admin/products");
        return "admin/products";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        CreateProductDTO product = new CreateProductDTO();
        model.addAttribute("product", product);
        return "admin/create-product";
    }

    @PostMapping("/create") // Use a full, clear path
    public String processCreateProduct(@ModelAttribute("product") @Valid CreateProductDTO product,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes) {

        // Check for validation errors from your DTO annotations (e.g., @NotEmpty)
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form to display them.
            return "admin/create-product";
        }

        // Check if the image file is empty (if it's a required field for you)
        if (product.getImageLink().isEmpty()) {
            bindingResult.rejectValue("imageLink", "error.product", "An image file is required.");
            // CORRECTED: Return to the correct view name
            return "admin/create-product";
        }

        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating product: " + e.getMessage());
            // Stay on the form if there's an error during processing
            return "admin/create-product";
        }

        // Redirect to the main admin products list on success
        return "redirect:/4Moos/admin/products/1";
    }

    @GetMapping("/details")
    public String internDetails(@RequestParam String idProduct, Model model) {
        ProductDTO product = productService.getProductById(idProduct).orElse(null);

        model.addAttribute("product", product);
        return "admin/product-details";
    }

    @GetMapping("/edit")
    public String showEditPage(@RequestParam("idProduct") String idProduct, Model model) {
        ProductDTO product = productService.getProductById(idProduct).orElse(null);
        model.addAttribute("product", product);
        return "admin/edit-product";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") @Valid ProductDTO product,
                              @RequestParam("newImageFile") MultipartFile newImageFile,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/edit-product";
        }

        try{
            productService.updateProduct(product,newImageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        }
        catch (Exception e) {
            e.printStackTrace(); // Log the exception
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating product: " + e.getMessage());
            return "admin/edit-product";
        }


        return "redirect:/4Moos/admin/products/1";
    }

    @GetMapping("/delete")
    public String deleteProduct(@RequestParam("idProduct") String idProduct, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(idProduct);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/4Moos/admin/products/1";
    }
}
