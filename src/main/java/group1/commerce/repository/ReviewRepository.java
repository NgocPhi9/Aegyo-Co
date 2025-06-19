package group1.commerce.repository;

import group1.commerce.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, String> {
    List<Reviews> findReviewsByProduct_IdProduct(String idProduct);
}
