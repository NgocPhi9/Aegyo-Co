package group1.commerce.controller;


import group1.commerce.dto.CartDTO;
import group1.commerce.dto.CartItemSession;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.*;
import group1.commerce.service.CartService;
import group1.commerce.service.CartSessionService;
import group1.commerce.service.ProductService;
import group1.commerce.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/4Moos")
public class CartController {
    private final CartService cartService;
    private final CartSessionService cartSessionService;

    public CartController(CartService cartService, CartSessionService cartSessionService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.cartSessionService = cartSessionService;
    }

    @GetMapping("/cart")
    public String cart(@ModelAttribute("user") UserDTO user, HttpSession session, Model model) {
        if (user == null) {
            List<CartItemSession> cartToDisplay = cartSessionService.getCartToDisplay(session);
            model.addAttribute("cart", cartToDisplay);
        } else {
            List<CartItem> cart = cartService.getCart(user.getIdUser());
            model.addAttribute("cart", cart);
        }

        return "cart";
    }

    @PostMapping(value = "/cart/add", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartDTO request,
                                       @ModelAttribute("user") UserDTO user,
                                       HttpSession session) {
        String idProduct = request.getIdProduct();
        int quantity = request.getQuantity();
        if (user == null) {
            cartSessionService.addToCart(session, idProduct, quantity);
        } else {
            cartService.addToCart(user.getIdUser(), idProduct, quantity);

        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/update")
    @ResponseBody
    public ResponseEntity<?> updateCart(@RequestBody CartDTO request,
                                           @ModelAttribute("user") UserDTO user,
                                           HttpSession session) {
        String idProduct = request.getIdProduct();
        int quantity = request.getQuantity();

        if (user == null) {
            cartSessionService.updateCart(session, idProduct, quantity);
        } else {
            cartService.updateCart(user.getIdUser(), idProduct, quantity);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cart/remove")
    @ResponseBody
    public ResponseEntity<?> removeFromCart(@ModelAttribute("user") UserDTO user,
                                            @RequestParam String idProduct, HttpSession session) {

        if (user == null) {
            cartSessionService.removeFromCart(session, idProduct);
        } else {
            cartService.removeFromCart(user.getIdUser(), idProduct);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/private/cart/quantity")
    @ResponseBody
    public int getCartQuantity(@ModelAttribute("user") UserDTO user,
                                        HttpSession session) {
        if (user == null) {
            return cartSessionService.getTotalQuantity(session);
        } else {
            return cartService.getTotalQuantity(user.getIdUser());
        }
    }
}
