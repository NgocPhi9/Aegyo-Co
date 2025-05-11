package group1.commerce.config;

import group1.commerce.dto.UserDTO;
import group1.commerce.service.CartService;
import group1.commerce.service.CartSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalCartModel {
    private final CartService cartService;
    private final CartSessionService cartSessionService;

    public GlobalCartModel(CartService cartService, CartSessionService cartSessionService) {
        this.cartService = cartService;
        this.cartSessionService = cartSessionService;
    }

    @ModelAttribute("cartQuantity")
    public int getCartItemCount(@ModelAttribute("user") UserDTO user, HttpSession session) {
        if (user == null) {
            return cartSessionService.getTotalQuantity(session);
        } else {
            return cartService.getTotalQuantity(user.getIdUser());
        }
    }
}
