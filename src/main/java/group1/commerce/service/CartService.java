package group1.commerce.service;


import group1.commerce.entity.CartItem;
import group1.commerce.entity.Product;
import group1.commerce.entity.User;
import group1.commerce.repository.CartRepository;
import group1.commerce.repository.ProductRepository;
import group1.commerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCart(String idUser) {
        return cartRepository.findByUser_IdUser(idUser);
    }

    public int getTotalQuantity(String idUser) {
        List<CartItem> cartItems = cartRepository.findByUser_IdUser(idUser);
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public void addToCart(String idUser, String idProduct, int quantity) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem item = cartRepository.findByUserAndProduct(user, product);

        if (item == null) {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartRepository.save(cartItem);
        } else {
            item.setQuantity(item.getQuantity() + quantity);
            cartRepository.save(item);
        }
    }

    public void updateCart(String idUser, String idProduct, int quantity) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem item = cartRepository.findByUserAndProduct(user, product);

        item.setQuantity(quantity);
        cartRepository.save(item);
    }

    public void removeFromCart(String idUser, String idProduct) {
        CartItem item = cartRepository.findByUser_IdUserAndProduct_IdProduct(idUser, idProduct);
        cartRepository.delete(item);
    }
}
