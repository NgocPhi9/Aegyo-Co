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

    private String idProduct;
    private String productName;
    private String productImageLink;

    private int quantity;
    private int totalPrice;

    @ManyToOne()
    @JoinColumn(name = "idOrder", nullable = false)
    private Orders order;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idReview", referencedColumnName = "idReview")
    private Reviews review;

}
