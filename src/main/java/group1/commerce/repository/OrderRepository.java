package group1.commerce.repository;

import group1.commerce.entity.OrderStage;
import group1.commerce.entity.Orders;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Integer>{
    List<Orders> findAll(Sort sort);
    List<Orders> findOrdersByCurrentStatus(OrderStage orderStage, Sort sort);

    // Tìm theo idUser
    List<Orders> findByUser_IdUser(String idUser, Sort sort);

    // Tìm theo idUser và trạng thái
    List<Orders> findByUser_IdUserAndCurrentStatus(String idUser, OrderStage orderStage, Sort sort);

    // Tìm theo idUser và keyword (tên sản phẩm hoặc mã đơn)
    @Query("""
        SELECT DISTINCT o FROM Orders o 
        LEFT JOIN o.orderItems i 
        LEFT JOIN i.idProduct p 
        WHERE o.user.idUser = :idUser 
        AND (
            LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
            OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
        )
    """)
    List<Orders> findByIdUserAndKeyword(@Param("idUser") String idUser, @Param("keyword") String keyword, Sort sort);

    // Tìm theo user và status và keyword
    @Query("""
    SELECT DISTINCT o FROM Orders o 
    LEFT JOIN o.orderItems i 
    LEFT JOIN i.idProduct p 
    WHERE o.user.idUser = :idUser 
    AND o.currentStatus = :status
    AND (
        LOWER(i.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) 
        OR STR(o.idOrder) LIKE CONCAT('%', :keyword, '%')
    )
""")
    List<Orders> findByIdUserAndStatusAndKeyword(@Param("idUser") String idUser, @Param("status") OrderStage status,
                                                 @Param("keyword") String keyword, Sort sort);

}
