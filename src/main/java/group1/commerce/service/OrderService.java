package group1.commerce.service;

import group1.commerce.dto.CartDTO;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.entity.*;
import group1.commerce.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, ProductService productService, UserService userService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
    }

    public void save(Orders order) {
        orderRepository.save(order);
    }

    public List<OrderItemDTO> getSelectedItems(List<CartDTO> items) {
        return items.stream()
                .map(item -> {
                    Product product = productService.getProductById(item.getIdProduct());
                    return new OrderItemDTO(product, item.getQuantity(), item.getQuantity() * product.getPrice());
                })
                .toList();
    }

    public int getTotalAmount(List<OrderItemDTO> items) {
        return items.stream().mapToInt(OrderItemDTO::getTotalPrice).sum();
    }

    public void makeOrder(String idUser, int totalAmount, String name,
                          String phone, String address, String payment,
                          List<OrderItemDTO> items) {
        Orders order = new Orders();
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(address);
        order.setPaymentMethod(payment);
        order.setCurrentStatus(OrderStage.PLACED);
        order.setUser(userService.getUserById(idUser));

        List<OrderItem> orderItems = items.stream().map(dto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(dto.getProduct());
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setTotalPrice(dto.getTotalPrice());
            orderItem.setOrder(order);
            cartService.removeFromCart(idUser, dto.getProduct().getIdProduct());
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems);

        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setOrder(order);
        initialStatus.setStatus(OrderStage.PLACED);
        initialStatus.setStatusTime(LocalDateTime.now());

        order.setStatusHistory(List.of(initialStatus));
        save(order);
    }
}
