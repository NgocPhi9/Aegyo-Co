package group1.commerce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

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
