package group1.commerce.repository;

import group1.commerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByIdProvided(String id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Query(value = """
        SELECT * FROM user u
        WHERE u.role = 'CUSTOMER'
        AND (:keyword IS NULL OR u.idUser LIKE %:keyword% OR u.email LIKE %:keyword%)
        AND (:restricted IS NULL OR u.purchaseRestricted = :restricted)
        ORDER BY
            CASE WHEN :sort = 'name_asc' THEN u.userName END ASC,
            CASE WHEN :sort = 'name_desc' THEN u.userName END DESC,
            CASE WHEN :sort = 'email_asc' THEN u.email END ASC,
            CASE WHEN :sort = 'email_desc' THEN u.email END DESC,
            CASE WHEN :sort = 'badCancelCount_asc' THEN u.badCancelCount END ASC,
            CASE WHEN :sort = 'badCancelCount_desc' THEN u.badCancelCount END DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM user u
        WHERE (:keyword IS NULL OR u.idUser LIKE %:keyword% OR u.email LIKE %:keyword%)
          AND (:restricted IS NULL OR u.purchaseRestricted = :restricted)
        """,
            nativeQuery = true)
    Page<User> searchAndSort(
            @Param("keyword") String keyword,
            @Param("restricted") Boolean restricted,
            @Param("sort") String sort,
            Pageable pageable
    );
}
