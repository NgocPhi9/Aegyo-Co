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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

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
        boolean isAdmin = false;

        // Check if user has ROLE_ADMIN authority
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                isAdmin = true;
                break;
            }
        }


        if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
            DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
            userDTO = globalUserModel.resolveOAuth2User
                    (authentication, oauth2User);

        }
        else if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findUserByEmail(userDetails.getUsername());
            if (user != null) {
                userDTO = new UserDTO(user.getIdUser(), user.getUserName(), user.getEmail(),
                        user.getPhoneNumber(), user.getAddress(), user.isPurchaseRestricted(), user.getRole());

            }
        }

        if (userDTO != null) {
            session.setAttribute("user", userDTO);
            session.setAttribute("isAdmin",isAdmin);
            cartSessionService.mergeCart(userDTO, session);
        }

        // Redirect to admin dashboard if user is admin
        if (isAdmin) {
            response.sendRedirect("/4Moos/admin");
        } else {
            response.sendRedirect("/4Moos");
        }

    }


}
