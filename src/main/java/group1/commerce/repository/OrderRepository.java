package group1.commerce.repository;

import group1.commerce.entity.OrderStage;
import group1.commerce.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer>{
    Page<Orders> findAll(Pageable pageable);
    Page<Orders> findOrdersByCurrentStatus(OrderStage orderStage, Pageable pageable);

    @Query("""
        SELECT DISTINCT o FROM Orders o 
        LEFT JOIN o.orderItems i 
        WHERE (
            LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
        )
    """)
    Page<Orders> findOrdersByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT DISTINCT o FROM Orders o 
    LEFT JOIN o.orderItems i 
    WHERE o.currentStatus = :status
    AND (
        LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
        OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
    )
""")
    Page<Orders> findOrdersByStatusAndKeyword(@Param("status") OrderStage status,
                                              @Param("keyword") String keyword, Pageable pageable);
    // Tìm theo idUser
    Page<Orders> findByUser_IdUser(String idUser, Pageable pageable);

    // Tìm theo idUser và trạng thái
    Page<Orders> findByUser_IdUserAndCurrentStatus(String idUser, OrderStage orderStage, Pageable pageable);

    // Tìm theo idUser và keyword (tên sản phẩm hoặc mã đơn)
    @Query("""
        SELECT DISTINCT o FROM Orders o 
        LEFT JOIN o.orderItems i 
        WHERE o.user.idUser = :idUser 
        AND (
            LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
        )
    """)
    Page<Orders> findByIdUserAndKeyword(@Param("idUser") String idUser,
                                        @Param("keyword") String keyword, Pageable pageable);

    // Tìm theo user và status và keyword
    @Query("""
    SELECT DISTINCT o FROM Orders o 
    LEFT JOIN o.orderItems i 
    WHERE o.user.idUser = :idUser 
    AND o.currentStatus = :status
    AND (
        LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
        OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
    )
""")
    Page<Orders> findByIdUserAndStatusAndKeyword(@Param("idUser") String idUser, @Param("status") OrderStage status,
                                                 @Param("keyword") String keyword, Pageable pageable);

}
