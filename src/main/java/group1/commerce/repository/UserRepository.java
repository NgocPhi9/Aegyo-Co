package group1.commerce.repository;

import group1.commerce.dto.UserWithOrderCount;
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
        SELECT u.idUser, u.email, u.userName, u.phoneNumber, u.address, u.purchaseRestricted, u.badCancelCount,
               COUNT(o.idOrder) AS orderCount, COALESCE(SUM(o.totalAmount), 0) AS totalSpending
        FROM user u
        LEFT JOIN orders o ON u.idUser = o.idUser 
            AND o.currentStatus IN ('REVIEWED', 'DELIVERED')
        WHERE u.role = 'CUSTOMER'
          AND (:keyword IS NULL OR u.idUser LIKE %:keyword% OR u.email LIKE %:keyword%)
          AND (:restricted IS NULL OR u.purchaseRestricted = :restricted)
        GROUP BY u.idUser, u.email, u.userName, u.phoneNumber, u.address, u.purchaseRestricted, u.badCancelCount
        ORDER BY
            CASE WHEN :sort = 'name_asc' THEN u.userName END ASC,
            CASE WHEN :sort = 'name_desc' THEN u.userName END DESC,
            CASE WHEN :sort = 'email_asc' THEN u.email END ASC,
            CASE WHEN :sort = 'email_desc' THEN u.email END DESC,
            CASE WHEN :sort = 'badCancelCount_asc' THEN u.badCancelCount END ASC,
            CASE WHEN :sort = 'badCancelCount_desc' THEN u.badCancelCount END DESC,
            CASE WHEN :sort = 'totalSpending_asc' THEN totalSpending END ASC,
            CASE WHEN :sort = 'totalSpending_desc' THEN totalSpending END DESC
        """,
            countQuery = """
        SELECT COUNT(*) FROM user u
        WHERE u.role = 'CUSTOMER'
          AND (:keyword IS NULL OR u.idUser LIKE %:keyword% OR u.email LIKE %:keyword%)
          AND (:restricted IS NULL OR u.purchaseRestricted = :restricted)
        """,
            nativeQuery = true)
    Page<UserWithOrderCount> searchAndSort(
            @Param("keyword") String keyword,
            @Param("restricted") Boolean restricted,
            @Param("sort") String sort,
            Pageable pageable
    );

}
