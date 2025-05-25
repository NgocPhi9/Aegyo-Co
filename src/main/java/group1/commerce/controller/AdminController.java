package group1.commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/4Moos/admin")
public class AdminController {

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Welcome to Admin Dashboard");
        return "admin/dashboard";
    }

}