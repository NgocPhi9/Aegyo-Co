package group1.commerce.repository;

import group1.commerce.entity.CartItem;
import group1.commerce.entity.Product;
import group1.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByUserAndProduct(User user, Product product);
    CartItem findByUser_IdUserAndProduct_IdProduct(String idUser, String idProduct);
    List<CartItem> findByUser_IdUser(String idUser);
}
