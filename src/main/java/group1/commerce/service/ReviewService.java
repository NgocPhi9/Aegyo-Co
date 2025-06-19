package group1.commerce.service;

import group1.commerce.entity.*;
import group1.commerce.repository.OrderItemRepository;
import group1.commerce.repository.OrderRepository;
import group1.commerce.repository.ProductRepository;
import group1.commerce.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ReviewService {
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ReviewService(OrderItemRepository orderItemRepository, ProductRepository productRepository, ReviewRepository reviewRepository, OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    public int saveReview(Reviews review, int idOrderItem) {
        // Save review
        OrderItem orderItem = orderItemRepository.findById(idOrderItem).orElse(null);
        review.setOrderItem(orderItem);
        if (orderItem.getReview() != null) {
            // Or handle this with a custom exception
            return orderItem.getOrder().getIdOrder();
        }

        Product product = productRepository.findById(orderItem.getIdProduct()).orElse(null);
        User user = orderItem.getOrder().getUser();

        review.setUser(user);
        review.setProduct(product);

        // Set the relationship from the review's side
        review.setOrderItem(orderItem);
        reviewRepository.save(review); // Save the review first

        // IMPORTANT: Set the relationship from the orderItem's side
        orderItem.setReview(review);
        orderItemRepository.save(orderItem); // Now save the orderItem

        // Update product average rating

        ProductDetails productDetails = product.getProductDetails();

        int newRatingCount = productDetails.getRatingCount() + 1;
        BigDecimal newAverageRating = productDetails.getAverageRating().multiply(BigDecimal.valueOf(productDetails.getRatingCount())).add(BigDecimal.valueOf(review.getRating())).divide(BigDecimal.valueOf(newRatingCount), 2, BigDecimal.ROUND_HALF_UP);

        productDetails.setRatingCount(newRatingCount);
        productDetails.setAverageRating(newAverageRating);
        productRepository.save(product);

        // Check if all items in order are reviewed
        Orders order = orderItem.getOrder();
        boolean allItemsReviewed = order.getOrderItems().stream().allMatch(item -> item.getReview() != null);

        // If they are all reviewed, update the order status
        if (allItemsReviewed) {
            order.setCurrentStatus(OrderStage.REVIEWED);
            orderRepository.save(order);
        }
        return order.getIdOrder();
    }

    public int getOrderIdFromOrderItem(int idOrderItem) {
        OrderItem orderItem = orderItemRepository.findById(idOrderItem).orElse(null);
        return orderItem.getOrder().getIdOrder();
    }

    public List<Reviews> getReviewsByProduct(String idProduct) {
        return reviewRepository.findReviewsByProduct_IdProduct(idProduct);
    };
}
