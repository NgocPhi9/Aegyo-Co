package group1.commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String idProduct;
    private String productName;
    private String category;
    private String artist;
    private String description;
    private String imageLink;
    private int cogs;

    // ProductDetails fields
    private int price;
    private int view;
    private int soldQuantity;
    private int availableQuantity;
    private BigDecimal averageRating;
    private int ratingCount;

    // You can add additional calculated fields if needed
    private boolean inStock;

    // Helper method to check if product is in stock
    public boolean isInStock() {
        return availableQuantity > 0;
    }
}
