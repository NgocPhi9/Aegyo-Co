package group1.commerce.repository;

import group1.commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findProductsByProductName(String productName);
    Product findProductsByIdProduct(String id);
}
