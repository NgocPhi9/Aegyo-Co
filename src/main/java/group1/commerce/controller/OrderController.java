package group1.commerce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import group1.commerce.dto.CartDTO;
import group1.commerce.dto.OrderItemDTO;
import group1.commerce.dto.UserDTO;
import group1.commerce.entity.*;
import group1.commerce.service.CartService;
import group1.commerce.service.OrderService;
import group1.commerce.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
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
        return "orders";
    }

    @PostMapping("/checkout")
    public String checkout(@ModelAttribute("user") UserDTO user,
                           @RequestParam("itemsJson") String itemsJson,
                           RedirectAttributes redirectAttributes) throws JsonProcessingException {
        // Configure ObjectMapper locally
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
        // Configure ObjectMapper locally
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<OrderItemDTO> selectedItems = objectMapper.readValue(selectedItemsJson, new TypeReference<>() {});

        orderService.makeOrder(user.getIdUser(), totalAmount, name,
                phone, address, payment, selectedItems);

        return "redirect:/4Moos/checkout-success";
    }

    @GetMapping("/checkout-success")
    public String checkoutSuccess() {
        return "checkout-success";
    }

    @GetMapping("/orders/{page}")
    public String viewOrders(@ModelAttribute("user") UserDTO user,
            @RequestParam(required = false) OrderStage status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Boolean newest,
            @PathVariable() int page,
            Model model) {

        if (newest == null) {
            newest = true;
        }

        Page<Orders> ordersPage = orderService.searchUserOrders(user.getIdUser(), status, keyword, newest, page);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("newest", newest);
        model.addAttribute("statusList", List.of(OrderStage.values()));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalItems", ordersPage.getTotalElements());
        model.addAttribute("baseUrl", "/4Moos/orders");

        return "orders";
    }

    @GetMapping("/order/{idOrder}")
    public String viewOrderDetails(@ModelAttribute("user") UserDTO user,
                                   @PathVariable int idOrder, Model model) {
        Orders order = orderService.getOrder(idOrder);
        model.addAttribute("order", order);

        List<CancelReason> reasons = Arrays.stream(CancelReason.values())
                .filter(reason -> reason.getAllowedRoles().contains(user.getRole()))
                .toList();
        model.addAttribute("reasonList", reasons);

        return "order-details";
    }

    @PostMapping("/order/cancel/{idOrder}")
    public String cancelOrder(@ModelAttribute("user") UserDTO user, @PathVariable int idOrder,
                              @RequestParam OrderStage currentStatus, @RequestParam CancelReason reason) {
        orderService.checkCancelation(user.getIdUser(), currentStatus, reason);
        orderService.cancelOrder(idOrder, user.getRole(), reason);
        return "redirect:/4Moos/order/" + idOrder;
    }

}
