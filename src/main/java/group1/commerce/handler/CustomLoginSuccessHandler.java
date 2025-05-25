package group1.commerce.handler;

import group1.commerce.config.GlobalUserModel;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.User;
import group1.commerce.security.CustomUserDetails;
import group1.commerce.service.CartSessionService;
import group1.commerce.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CartSessionService cartSessionService;
    private final GlobalUserModel globalUserModel;
    private final UserService userService;

    public CustomLoginSuccessHandler(CartSessionService cartSessionService, GlobalUserModel globalUserModel, UserService userService) {
        this.cartSessionService = cartSessionService;
        this.globalUserModel = globalUserModel;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        UserDTO userDTO = null;

        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            userDTO = globalUserModel.resolveOAuth2User
                    (authentication, oauth2User);
            cartSessionService.mergeCart(userDTO, session);

        }
        else if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findUserByEmail(userDetails.getUsername());
            if (user != null) {
                userDTO = new UserDTO(user.getIdUser(), user.getUserName());
            }
        }

        if (userDTO != null) {
            session.setAttribute("user", userDTO);
            cartSessionService.mergeCart(userDTO, session);
        }

        response.sendRedirect("/4Moos");
    }


}
