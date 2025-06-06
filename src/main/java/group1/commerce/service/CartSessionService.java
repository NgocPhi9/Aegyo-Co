package group1.commerce.service;

import group1.commerce.dto.CartDTO;
import group1.commerce.dto.CartItemSession;
import group1.commerce.dto.ProductDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.CartItem;
import group1.commerce.entity.Product;
import group1.commerce.entity.User;
import group1.commerce.mapper.ProductMapper;
import group1.commerce.repository.CartRepository;
import group1.commerce.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartSessionService {

    private static final String CART_SESSION_KEY = "CART_SESSION";
    private final ProductService productService;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductMapper productMapper;

    public CartSessionService(ProductService productService, CartService cartService, CartRepository cartRepository, UserRepository userRepository, UserService userService, ProductMapper productMapper) {
        this.productService = productService;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productMapper = productMapper;
    }

    @SuppressWarnings("unchecked")
    public List<CartDTO> getCartFromSession(HttpSession session) {
        List<CartDTO> cart = (List<CartDTO>) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public int getTotalQuantity(HttpSession session) {
        return getCartFromSession(session).stream()
                .mapToInt(CartDTO::getQuantity)
                .sum();
    }

    public void addToCart(HttpSession session, String idProduct, int quantity) {
        List<CartDTO> cart = getCartFromSession(session);

        boolean updated = false;
        for (CartDTO item : cart) {
            if (item.getIdProduct().equals(idProduct)) {
                item.setQuantity(item.getQuantity() + quantity);
                updated = true;
                break;
            }
        }

        if (!updated) {
            cart.add(new CartDTO(idProduct, quantity));
        }
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateCart(HttpSession session, String idProduct, int quantity) {
        List<CartDTO> cart = getCartFromSession(session);

        for (CartDTO item : cart) {
            if (item.getIdProduct().equals(idProduct)) {
                item.setQuantity(quantity);
                session.setAttribute(CART_SESSION_KEY, cart);
            }
        }
    }

    public void removeFromCart(HttpSession session, String idProduct) {
        List<CartDTO> cart = getCartFromSession(session);
        cart.removeIf(item -> item.getIdProduct().equals(idProduct));
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void mergeCart(UserDTO user, HttpSession session) {
        List<CartDTO> cart = getCartFromSession(session);
        User dbUser = userService.getUserById(user.getIdUser());


        for (CartDTO sessionItem : cart) {
            CartItem dbItem = cartRepository.findByUser_IdUserAndProduct_IdProduct
                    (user.getIdUser(), sessionItem.getIdProduct());
            Optional<ProductDTO> optionalProductDTO = productService.getProductById(sessionItem.getIdProduct());
            Product product = optionalProductDTO.map(productMapper::toEntity).orElse(null);

            if (dbItem != null) {
                // Nếu đã có trong DB, cộng dồn số lượng
                dbItem.setQuantity(dbItem.getQuantity() + sessionItem.getQuantity());
                cartRepository.save(dbItem);
            } else {
                // Nếu chưa có, thêm mới vào DB
                CartItem newItem = new CartItem();
                newItem.setUser(dbUser);
                newItem.setProduct(product);
                newItem.setQuantity(sessionItem.getQuantity());

                cartRepository.save(newItem);
            }
        }
        session.removeAttribute("CART_SESSION_KEY");
    }

    public List<CartItemSession> getCartToDisplay(HttpSession session) {
        List<CartDTO> cart = getCartFromSession(session);
        return cart.stream()
                .map(item -> {
                    Optional<ProductDTO> optionalProductDTO = productService.getProductById(item.getIdProduct());
                    ProductDTO productDTO = optionalProductDTO.get();
                    return new CartItemSession(productDTO, item.getQuantity());
                })
                .toList();
    }
}
