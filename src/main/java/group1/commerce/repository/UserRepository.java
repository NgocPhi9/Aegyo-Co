package group1.commerce.repository;

import group1.commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByIdProvided(String id);

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
