package group1.commerce.controller;

import group1.commerce.dto.UserDTO;
import group1.commerce.dto.UserLoginDTO;
import group1.commerce.dto.UserRegisteredDTO;
import group1.commerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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




}
