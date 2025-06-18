package group1.commerce.controller;

import group1.commerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/4Moos/admin")
public class AdminController {

    private final ProductService productService;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Welcome to Admin Dashboard");
        return "admin/dashboard";
    }


}