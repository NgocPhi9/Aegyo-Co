package group1.commerce.controller;

import group1.commerce.dto.UserDTO;
import group1.commerce.entity.User;
import group1.commerce.service.ProductService;
import group1.commerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/4Moos/admin")
public class AdminController {

    private final UserService userService; ;


    public AdminController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("message", "Welcome to Admin Dashboard");
        return "admin/dashboard";
    }


    @GetMapping("/profile")
    public String viewProfile(@ModelAttribute("user") UserDTO currentUser, Model model) {
        User userFromDb = userService.getUserById(currentUser.getIdUser());

        // Add the full, up-to-date user object to the model
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "profile");

        return "admin/admin-profile"; // The name of our new HTML template
    }

    @GetMapping("/profile/address")
    public String viewProfileAddress(@ModelAttribute("user") UserDTO currentUser, Model model) {
        // You must add this to every method using the layout
        User userFromDb = userService.getUserById(currentUser.getIdUser());
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "address");

        return "admin/admin-profile-address";
    }

    @GetMapping("/profile/password")
    public String viewProfilePassword(@ModelAttribute("user") UserDTO currentUser, Model model) {
        // You must add this to every method using the layout
        User userFromDb = userService.getUserById(currentUser.getIdUser());
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "password");

        return "admin/admin-profile-password";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") UserDTO currentUser,
                                @RequestParam String userName,
                                @RequestParam String phoneNumber,
                                RedirectAttributes redirectAttributes) {

        User userFromDb = userService.getUserById(currentUser.getIdUser());
        String userEmail = userFromDb.getEmail();
        userService.updateUserInfo(userEmail, userName, phoneNumber);

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/4Moos/admin/profile";
    }

    @PostMapping("/profile/address/update")
    public String updateAddress(@ModelAttribute("user") UserDTO currentUser,
                                @RequestParam String phoneNumber,
                                @RequestParam String address,
                                RedirectAttributes redirectAttributes) {

        User userFromDb = userService.getUserById(currentUser.getIdUser());
        String userEmail = userFromDb.getEmail();
        userService.updateUserAddress(userEmail, phoneNumber, address);

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/4Moos/admin/profile/address";
    }

    // In ProfileController.java

    @PostMapping("/profile/password/update")
    public String updatePassword(@ModelAttribute("user") UserDTO currentUser,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "New passwords do not match.");
            return "redirect:/4Moos/admin/profile/password";
        }

        User userFromDb = userService.getUserById(currentUser.getIdUser());
        String userEmail = userFromDb.getEmail();
        try {
            userService.changeUserPassword(userEmail, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/4Moos/admin/profile/password";
    }


}