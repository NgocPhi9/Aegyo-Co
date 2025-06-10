package group1.commerce.mapper;


import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.Product;
import group1.commerce.entity.ProductDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    // Convert Product entity to ProductDTO
    public ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        // Set Product fields
        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setProductName(product.getProductName());
        productDTO.setCategory(product.getCategory());
        productDTO.setArtist(product.getArtist());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageLink(product.getImageLink());
        productDTO.setCogs(product.getCogs());
        productDTO.setCreatedAt(product.getCreatedAt());
        // Set ProductDetails fields if available
        ProductDetails productDetails = product.getProductDetails();
        if (productDetails != null) {
            productDTO.setPrice(productDetails.getPrice());
            productDTO.setView(productDetails.getView());
            productDTO.setSoldQuantity(productDetails.getSoldQuantity());
            productDTO.setAvailableQuantity(productDetails.getAvailableQuantity());
            productDTO.setAverageRating(productDetails.getAverageRating());
            productDTO.setRatingCount(productDetails.getRatingCount());
            productDTO.setInStock(productDetails.getAvailableQuantity() > 0);
        }
        return productDTO;
    }
    // Convert list of Product entities to list of ProductDTOs
    public List<ProductDTO> toDTOList(List<Product> products) {
        return products.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        }

    // Convert ProductDTO to Product entity (useful for updates)
    public Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setIdProduct(dto.getIdProduct());
        product.setProductName(dto.getProductName());
        product.setCategory(dto.getCategory());
        product.setArtist(dto.getArtist());
        product.setDescription(dto.getDescription());
        product.setImageLink(dto.getImageLink());
        product.setCogs(dto.getCogs());

        ProductDetails details = new ProductDetails();
        details.setIdProduct(dto.getIdProduct());
        details.setPrice(dto.getPrice());
        details.setView(dto.getView());
        details.setSoldQuantity(dto.getSoldQuantity());
        details.setAvailableQuantity(dto.getAvailableQuantity());
        details.setAverageRating(dto.getAverageRating());
        details.setRatingCount(dto.getRatingCount());
        details.setProduct(product);

        product.setProductDetails(details);

        return product;
    }

}
