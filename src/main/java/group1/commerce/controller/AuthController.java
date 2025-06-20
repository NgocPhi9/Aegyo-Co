package group1.commerce.controller;

import group1.commerce.dto.UserDTO;
import group1.commerce.dto.UserLoginDTO;
import group1.commerce.dto.UserRegisteredDTO;
import group1.commerce.entity.User;
import group1.commerce.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/4Moos")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@Valid @ModelAttribute UserLoginDTO userLoginDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "login";
        }
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userRegisteredDTO", new UserRegisteredDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute UserRegisteredDTO userRegisteredDTO, BindingResult result) {
        if (userService.findUserByEmail(userRegisteredDTO.getEmail()) != null) {
            result.rejectValue("email", "userDto.email", "An account already exists for this email.");
        }
        if (result.hasErrors()) {
            return "register";
        }
        userService.registerUser(userRegisteredDTO);
        return "login";

    }


    // In ProfileController.java

    @GetMapping("/profile")
    public String viewProfile(@ModelAttribute("user") UserDTO currentUser, Model model) {
        User userFromDb = userService.getUserById(currentUser.getIdUser());

        // Add the full, up-to-date user object to the model
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "profile");

        return "profile-customer"; // The name of our new HTML template
    }

    @GetMapping("/profile/address")
    public String viewProfileAddress(@ModelAttribute("user") UserDTO currentUser, Model model) {
        // You must add this to every method using the layout
        User userFromDb = userService.getUserById(currentUser.getIdUser());
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "address");

        return "profile-address";
    }

    @GetMapping("/profile/password")
    public String viewProfilePassword(@ModelAttribute("user") UserDTO currentUser, Model model) {
        // You must add this to every method using the layout
        User userFromDb = userService.getUserById(currentUser.getIdUser());
        model.addAttribute("profileUser", userFromDb);
        model.addAttribute("activeTab", "password");

        return "profile-password";
    }

    // In ProfileController.java

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") UserDTO currentUser,
                                @RequestParam String userName,
                                @RequestParam String phoneNumber,
                                RedirectAttributes redirectAttributes) {

        User userFromDb = userService.getUserById(currentUser.getIdUser());
        String userEmail = userFromDb.getEmail();
        userService.updateUserInfo(userEmail, userName, phoneNumber);

        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
        return "redirect:/4Moos/profile";
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
        return "redirect:/4Moos/profile/address";
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
            return "redirect:/4Moos/profile/password";
        }

        User userFromDb = userService.getUserById(currentUser.getIdUser());
        String userEmail = userFromDb.getEmail();
        try {
            userService.changeUserPassword(userEmail, currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/4Moos/profile/password";
    }






}
