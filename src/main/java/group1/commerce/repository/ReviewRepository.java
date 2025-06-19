package group1.commerce.repository;

import group1.commerce.entity.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Reviews, String> {
    List<Reviews> findByProduct_IdProductOrderByCreatedAtDesc(String idProduct);


}
