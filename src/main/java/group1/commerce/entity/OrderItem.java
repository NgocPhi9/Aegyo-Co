package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrderItem;

    private int quantity;
    private int totalPrice;

    @ManyToOne()
    @JoinColumn(name = "idProduct", nullable = false)
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "idOrder", nullable = false)
    private Orders order;
}
