package group1.commerce.handler;

import group1.commerce.config.GlobalUserModel;
import group1.commerce.dto.UserDTO;
import group1.commerce.service.CartSessionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final CartSessionService cartSessionService;
    private final GlobalUserModel globalUserModel;

    public CustomLoginSuccessHandler(CartSessionService cartSessionService, GlobalUserModel globalUserModel) {
        this.cartSessionService = cartSessionService;
        this.globalUserModel = globalUserModel;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        OAuth2User oauth2User = ((OAuth2AuthenticationToken) authentication).getPrincipal();

        UserDTO userDTO = globalUserModel.resolveUser(authentication, oauth2User);
        cartSessionService.mergeCart(userDTO, session);

        response.sendRedirect("/4Moos");
    }

}
