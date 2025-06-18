package group1.commerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProductDTO {
    @NotEmpty(message = "Product name cannot be empty")
    private String productName;
    private String category;
    @NotEmpty(message = "Artist name cannot be empty")
    private String artist;
    private String description;
    private MultipartFile imageLink;
    private int cogs;
    private int price;
    private int availableQuantity;
}

