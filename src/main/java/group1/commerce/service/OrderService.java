package group1.commerce.service;

import group1.commerce.dto.CartDTO;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.dto.ProductDTO;
import group1.commerce.entity.*;
import group1.commerce.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
            orderItem.setIdProduct(dto.getProduct().getIdProduct());
            orderItem.setProductName(dto.getProduct().getProductName());
            orderItem.setProductImageLink(dto.getProduct().getImageLink());
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setTotalPrice(dto.getTotalPrice());
            orderItem.setOrder(order);
            // Xóa khỏi giỏ hàng
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

    public Page<Orders> searchUserOrders(String idUser, OrderStage status, String keyword, boolean newest, int page) {

        Sort sort = Sort.by(newest ? Sort.Direction.DESC : Sort.Direction.ASC, "idOrder");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);

        if (keyword != null && !keyword.isBlank() && status != null) {
            return orderRepository.findByIdUserAndStatusAndKeyword(idUser, status, keyword, pageable);
        }

        if (keyword != null && !keyword.isBlank()) {
            return orderRepository.findByIdUserAndKeyword(idUser, keyword, pageable);
        }

        if (status != null) {
            return orderRepository.findByUser_IdUserAndCurrentStatus(idUser, status, pageable);
        }

        return orderRepository.findByUser_IdUser(idUser, pageable);
    }

    public Page<Orders> searchOrders(OrderStage status, String keyword, boolean newest, int page) {

        Sort sort = Sort.by(newest ? Sort.Direction.DESC : Sort.Direction.ASC, "idOrder");
        Pageable pageable = PageRequest.of(page - 1, 16, sort);

        if (keyword != null && !keyword.isBlank() && status != null) {
            return orderRepository.findOrdersByStatusAndKeyword(status, keyword, pageable);
        }

        if (keyword != null && !keyword.isBlank()) {
            return orderRepository.findOrdersByKeyword(keyword, pageable);
        }

        if (status != null) {
            return orderRepository.findOrdersByCurrentStatus(status, pageable);
        }

        return orderRepository.findAll(pageable);
    }

    public Orders getOrder(int idOrder) {
        return orderRepository.findById(idOrder).orElseThrow(
                () -> new EntityNotFoundException("Order not found"));
    }

    public void cancelOrder(int idOrder, Role role, CancelReason reason) {
        Orders order = getOrder(idOrder);
        OrderStage oldStatus = order.getCurrentStatus();
        order.setCurrentStatus(OrderStage.CANCELLED);
        order.setCancelledBy(role);
        order.setCancelReason(reason);

        List<OrderItem> items = order.getOrderItems();
        if (oldStatus == OrderStage.SHIPPED) {
            for (OrderItem item : items) {
                productService.returnProduct(item.getIdProduct(), item.getQuantity());
            }
        }

        OrderStatus status = new OrderStatus();
        status.setOrder(order);
        status.setStatus(OrderStage.CANCELLED);
        status.setStatusTime(LocalDateTime.now());
        order.getStatusHistory().add(status);
        save(order);
    }

    public void editStatus(int idOrder, OrderStage status) {
        Orders order = getOrder(idOrder);
        order.setCurrentStatus(status);

        List<OrderItem> items = order.getOrderItems();
        if (status == OrderStage.SHIPPED) {
            for (OrderItem item : items) {
                productService.shipProduct(item.getIdProduct(), item.getQuantity());
            }
        }
        if (status == OrderStage.DELIVERED) {
            for (OrderItem item : items) {
                productService.sellProduct(item.getIdProduct(), item.getQuantity());
            }
        }

        OrderStatus newStatus = new OrderStatus();
        newStatus.setOrder(order);
        newStatus.setStatus(status);
        newStatus.setStatusTime(LocalDateTime.now());
        order.getStatusHistory().add(newStatus);
        save(order);
    }

    public void checkCancelation(String idUser, OrderStage currentStatus, CancelReason reason) {
        User user = userService.getUserById(idUser);
        Role role = user.getRole();
        if (role == Role.CUSTOMER && currentStatus == OrderStage.CONFIRMED ||
                role == Role.ADMIN && reason == CancelReason.CUSTOMER_REJECTED) {
            user.setBadCancelCount(user.getBadCancelCount() + 1);
            userService.save(user);
        }
    }
}
