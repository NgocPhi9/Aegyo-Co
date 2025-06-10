package group1.commerce.service;

import group1.commerce.dto.CartDTO;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.*;
import group1.commerce.mapper.ProductMapper;
import group1.commerce.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final CartService cartService;
    private final ProductMapper productMapper;

    public OrderService(OrderRepository orderRepository, ProductService productService, UserService userService, CartService cartService, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
        this.cartService = cartService;
        this.productMapper = productMapper;
    }

    public void save(Orders order) {
        orderRepository.save(order);
    }

    public List<OrderItemDTO> getSelectedItems(List<CartDTO> items) {
        return items.stream()
                .map(item -> {
                    Optional<ProductDTO> productDTOOptional = productService.getProductById(item.getIdProduct());
                    if (productDTOOptional.isEmpty()) {
                        throw new EntityNotFoundException("Product not found with ID: " + item.getIdProduct());
                    }
                    ProductDTO product = productDTOOptional.get();
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
        order.setCustomerName(name);
        order.setPhoneNumber(phone);
        order.setShippingAddress(address);
        order.setPaymentMethod(payment);
        order.setCurrentStatus(OrderStage.PLACED);
        order.setUser(userService.getUserById(idUser));

        List<OrderItem> orderItems = items.stream().map(dto -> {
            OrderItem orderItem = new OrderItem();
            Optional<ProductDTO> productDTO = productService.getProductById(dto.getProduct().getIdProduct());
            Product productEntity = productDTO.map(productMapper::toEntity).orElse(null);
            if (productEntity == null) {
                throw new EntityNotFoundException("Product entity not found with ID: " + dto.getProduct().getIdProduct());
            }
            orderItem.setProduct(productEntity);
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

    public List<Orders> searchUserOrders(String idUser, OrderStage status, String keyword, boolean newest) {

        Sort sort = Sort.by(newest ? Sort.Direction.DESC : Sort.Direction.ASC, "idOrder");

        if (keyword != null && !keyword.isBlank() && status != null) {
            return orderRepository.findByIdUserAndStatusAndKeyword(idUser, status, keyword, sort);
        }

        if (keyword != null && !keyword.isBlank()) {
            return orderRepository.findByIdUserAndKeyword(idUser, keyword, sort);
        }

        if (status != null) {
            return orderRepository.findByUser_IdUserAndCurrentStatus(idUser, status, sort);
        }

        return orderRepository.findByUser_IdUser(idUser, sort);
    }

    public Orders getOrder(int idOrder) {
        return orderRepository.findById(idOrder).orElseThrow(
                () -> new EntityNotFoundException("Order not found"));
    }

    public void cancelOrder(int idOrder) {
        Orders order = getOrder(idOrder);
        order.setCurrentStatus(OrderStage.CANCELLED);

        OrderStatus status = new OrderStatus();
        status.setOrder(order);
        status.setStatus(OrderStage.CANCELLED);
        status.setStatusTime(LocalDateTime.now());
        order.getStatusHistory().add(status);
        save(order);
    }
}
