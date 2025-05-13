package group1.commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import group1.commerce.dto.CartDTO;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.*;
import group1.commerce.service.CartService;
import group1.commerce.service.OrderService;
import group1.commerce.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/4Moos")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService, CartService cartService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/order")
    public String getOrder() {
        return "order";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("user") UserDTO user,
                           @RequestParam("itemsJson") String itemsJson,
                           RedirectAttributes redirectAttributes) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CartDTO> items = objectMapper.readValue(itemsJson, new TypeReference<>() {});

        List<OrderItemDTO> selectedItems = orderService.getSelectedItems(items);
        redirectAttributes.addFlashAttribute("selectedItems", selectedItems);

        User dbUser = userService.getUserById(user.getIdUser());
        redirectAttributes.addFlashAttribute("dbUser", dbUser);

        int totalAmount = orderService.getTotalAmount(selectedItems);
        redirectAttributes.addFlashAttribute("totalAmount", totalAmount);

        String selectedItemsJson = objectMapper.writeValueAsString(selectedItems);
        redirectAttributes.addFlashAttribute("selectedItemsJson", selectedItemsJson);

        return "redirect:/4Moos/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(@ModelAttribute("user") UserDTO user, Model model) {
        return "checkout";
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

        orderService.makeOrder(user.getIdUser(), totalAmount, name,
                phone, address, payment, selectedItems);

        return "redirect:/4Moos/checkout-success";
    }

    @GetMapping("/checkout-success")
    public String checkoutSuccess() {
        return "checkout-success";
    }
}
