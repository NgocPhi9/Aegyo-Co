package group1.commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group1.commerce.dto.CartDTO;
import group1.commerce.dto.CartItemSession;
import group1.commerce.dto.OrderItemDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/4Moos")
public class CartController {
    private final CartService cartService;
    private final CartSessionService cartSessionService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartService cartService, CartSessionService cartSessionService, ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.cartSessionService = cartSessionService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/cart")
    public String cart(@ModelAttribute("user") UserDTO user, HttpSession session, Model model) {
        if (user == null) {
            List<CartDTO> cart = cartSessionService.getCartFromSession(session);
            List<CartItemSession> cartToDisplay = cart.stream()
                    .map(item -> {
                        Product product = productService.getProductById(item.getIdProduct());
                        return new CartItemSession(product, item.getQuantity());
                    })
                    .toList();

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
    public List<?> updateCart(@RequestBody CartDTO request,
                                           @ModelAttribute("user") UserDTO user,
                                           HttpSession session) {
        String idProduct = request.getIdProduct();
        int quantity = request.getQuantity();

        if (user == null) {
            cartSessionService.updateCart(session, idProduct, quantity);
            return cartSessionService.getCartFromSession(session);
        } else {
            cartService.updateCart(user.getIdUser(), idProduct, quantity);
            return cartService.getCart(user.getIdUser());
        }

    }

    @PostMapping("/cart/remove")
    @ResponseBody
    public List<?> removeFromCart(@ModelAttribute("user") UserDTO user,
                                            @RequestParam String idProduct, HttpSession session) {

        if (user == null) {
            cartSessionService.removeFromCart(session, idProduct);
            return cartSessionService.getCartFromSession(session);
        } else {
            cartService.removeFromCart(user.getIdUser(), idProduct);
            return cartService.getCart(user.getIdUser());
        }
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("user") UserDTO user,
                           @RequestParam("itemsJson") String itemsJson,
                           RedirectAttributes redirectAttributes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CartDTO> items = objectMapper.readValue(itemsJson, new TypeReference<>() {});

        List<OrderItemDTO> selectedItems = items.stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getIdProduct());
                    return new OrderItemDTO(product, item.getQuantity(), item.getQuantity() * product.getPrice());
                })
                .toList();

        User dbUser = userService.getUserById(user.getIdUser());
        int totalAmount = selectedItems.stream().mapToInt(OrderItemDTO::getTotalPrice).sum();

        redirectAttributes.addFlashAttribute("dbUser", dbUser);
        redirectAttributes.addFlashAttribute("selectedItems", selectedItems);
        redirectAttributes.addFlashAttribute("totalAmount", totalAmount);

        String selectedItemsJson = objectMapper.writeValueAsString(selectedItems);
        redirectAttributes.addFlashAttribute("selectedItemsJson", selectedItemsJson);

        return "redirect:/4Moos/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(@ModelAttribute("user") UserDTO user, Model model) {
        return "checkout";
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
