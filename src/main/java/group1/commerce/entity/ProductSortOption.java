package group1.commerce.entity;

import org.springframework.data.domain.Sort;

public enum ProductSortOption {
    // Define options relevant for general product listings
    NEWEST("Newest First", "createdAt", Sort.Direction.DESC),
    OLDEST("Oldest First", "createdAt", Sort.Direction.ASC),
    NAME_ASC("Name: A-Z", "productName", Sort.Direction.ASC),
    NAME_DESC("Name: Z-A", "productName", Sort.Direction.DESC),
    PRICE_ASC("Price: Low to High", "productDetails.price", Sort.Direction.ASC),
    PRICE_DESC("Price: High to Low", "productDetails.price", Sort.Direction.DESC),
    BEST_SELLING("Best Selling", "productDetails.soldQuantity", Sort.Direction.DESC); // For "products sold"

    private final String displayName;
    private final String property;
    private final Sort.Direction direction;

    ProductSortOption(String displayName, String property, Sort.Direction direction) {
        this.displayName = displayName;
        this.property = property;
        this.direction = direction;
    }

    public String getDisplayName() { return displayName; }
    public String getProperty() { return property; }
    public Sort.Direction getDirection() { return direction; }

    public Sort getSort() {
        if (this.property == null) return Sort.unsorted();
        return Sort.by(this.direction, this.property);
    }

    public static ProductSortOption fromString(String text, ProductSortOption defaultOption) {
        if (text != null) {
            for (ProductSortOption option : ProductSortOption.values()) {
                if (text.equalsIgnoreCase(option.name())) {
                    return option;
                }
            }
        }
        return defaultOption; // Return a sensible default
    }
}