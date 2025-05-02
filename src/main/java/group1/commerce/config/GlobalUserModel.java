package group1.commerce.config;

import group1.commerce.dto.UserDTO;
import group1.commerce.entity.User;
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

    @ModelAttribute("user")
    public UserDTO currentUser(@AuthenticationPrincipal OAuth2User oauth2User,
                                  Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || oauth2User == null)
            return null;

        String provider = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        Map<String, Object> attr = oauth2User.getAttributes();

        String id = switch (provider) {
            case "google" -> (String) attr.get("sub");
            case "facebook" -> (String) attr.get("id");
            default -> null;
        };

        String name = (String) attr.getOrDefault("name", attr.get("login"));//Sửa lại theo đúng định dạng
        User user = userService.findUserByIdProvided(id);
        if (user == null){
            User newUser = new User();
            newUser.setUserName(name);
            newUser.setIdProvided(id);
            userService.save(newUser);
        } else {
            id = user.getIdUser();
        }

        return new UserDTO(id, name);
    }
}
