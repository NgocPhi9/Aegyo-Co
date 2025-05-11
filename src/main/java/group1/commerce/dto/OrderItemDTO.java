package group1.commerce.dto;

import group1.commerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {
    private Product product;
    private int quantity;
    private int totalPrice;
}
