package group1.commerce.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetails {
    @Id
    private String idProduct;
    private int price;
    private int view = 0;
    private int soldQuantity;
    private int availableQuantity;
    private BigDecimal averageRating;
    private int ratingCount;

    @OneToOne
    @MapsId // This makes idProduct both PK and FK
    @JoinColumn(name = "idProduct")
    private Product product;

}