package group1.commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.OrderItem;
import group1.commerce.entity.OrderStage;
import group1.commerce.entity.OrderStatus;
import group1.commerce.entity.Orders;
import group1.commerce.service.CartService;
import group1.commerce.service.OrderService;
import group1.commerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/4Moos")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/order")
    public String getOrder() {
        return "order";
    }


    @PostMapping("/submit-order")
    public String submitOrder(@ModelAttribute("user") UserDTO user,
                              @RequestParam("totalAmount") int totalAmount,
                              @RequestParam("name") String name,
                              @RequestParam("phone") String phone,
                              @RequestParam("address") String address,
                              @RequestParam("payment") String payment,
                              @RequestParam("selectedItemsJson") String selectedItemsJson) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderItemDTO> selectedItems = objectMapper.readValue(selectedItemsJson, new TypeReference<>() {});

        Orders order = new Orders();
        order.setTotalAmount(totalAmount);
        order.setShippingAddress(address);
        order.setPaymentMethod(payment);
        order.setCurrentStatus(OrderStage.PLACED);
        order.setUser(userService.getUserById(user.getIdUser()));

        List<OrderItem> orderItems = selectedItems.stream().map(dto -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(dto.getProduct());
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setTotalPrice(dto.getTotalPrice());
            orderItem.setOrder(order);
            cartService.removeFromCart(user.getIdUser(), dto.getProduct().getIdProduct());
            return orderItem;
        }).toList();
        order.setOrderItems(orderItems);

        OrderStatus initialStatus = new OrderStatus();
        initialStatus.setOrder(order);
        initialStatus.setStatus(OrderStage.PLACED);
        initialStatus.setStatusTime(LocalDateTime.now());

        order.setStatusHistory(List.of(initialStatus));
        orderService.save(order);

        return "redirect:/4Moos/checkout-success";
    }

    @GetMapping("/checkout-success")
    public String checkoutSuccess() {
        return "checkout-success";
    }
}
