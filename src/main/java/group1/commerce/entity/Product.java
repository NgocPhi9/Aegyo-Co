package group1.commerce.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idProduct;
    private String productName;
    private String category;
    private String artist;
    private String description;
    private String imageLink;
    private int cogs;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ProductDetails productDetails;
}
