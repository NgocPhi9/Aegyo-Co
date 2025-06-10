package group1.commerce.config;

import group1.commerce.dto.UserDTO;
import group1.commerce.entity.Role;
import group1.commerce.entity.User;
import group1.commerce.security.CustomUserDetails;
import group1.commerce.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Map;

@Component
@ControllerAdvice
public class GlobalUserModel {
    private final UserService userService;

    public GlobalUserModel(UserService userService) {
        this.userService = userService;
    }

    public UserDTO resolveOAuth2User(Authentication authentication, OAuth2User oauth2User) {

        if (authentication == null || !authentication.isAuthenticated() || oauth2User == null)
            return null;

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Map<String, Object> attr = oauth2User.getAttributes();

        String id = switch (provider) {
            case "google" -> (String) attr.get("sub");
            case "facebook" -> (String) attr.get("id");
            default -> null;
        };

        String name = (String) attr.getOrDefault("name", attr.get("login"));
        String email = (String) attr.get("email");

        User user = userService.findUserByIdProvided(id);
        if (user == null){
            User newUser = new User();
            newUser.setUserName(name);
            newUser.setIdProvided(id);
            newUser.setEmail(email);
            newUser.setRole(Role.CUSTOMER);
            userService.save(newUser);
            id = newUser.getIdUser();
        } else {
            id = user.getIdUser();
        }

        return new UserDTO(id, name, email, null, null, Role.CUSTOMER);
    }

    public UserDTO resolveFormLoginUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return null;
        }

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findUserByEmail(customUserDetails.getUsername());
        if (user != null) {
            return new UserDTO(user.getIdUser(), user.getUserName(), user.getEmail(), user.getPhoneNumber(), user.getAddress(), user.getRole());
        }

        return null;


    }

    @ModelAttribute("user")
    public UserDTO currentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        // Handle OAuth2 authentication
        if (authentication.getPrincipal() instanceof OAuth2User) {
            return resolveOAuth2User(authentication, (OAuth2User) authentication.getPrincipal());
        }

        // Handle form-based authentication
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return resolveFormLoginUser(authentication);
        }

        return null;
    }

}
