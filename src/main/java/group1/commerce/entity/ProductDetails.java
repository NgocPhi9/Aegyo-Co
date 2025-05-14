package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

import java.text.DecimalFormat;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDetails {

    @Id
    @Column(name = "idProduct")
    private String idProduct;

    private int price;
    private int view = 0;
    private int soldQuantity;
    private int availableQuantity;
    private BigDecimal  averageRating;
    private int ratingCount = 0;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idProduct")
    private Product product;
}
