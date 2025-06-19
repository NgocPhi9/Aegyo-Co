package group1.commerce.controller;

import group1.commerce.dto.UserDTO;
import group1.commerce.entity.CancelReason;
import group1.commerce.entity.OrderStage;
import group1.commerce.entity.Orders;
import group1.commerce.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/4Moos/admin")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
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

        Page<Orders> ordersPage = orderService.searchOrders(status, keyword, newest, page);

        model.addAttribute("ordersPage", ordersPage);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("keyword", keyword);
        model.addAttribute("newest", newest);
        model.addAttribute("statusList", List.of(OrderStage.values()));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ordersPage.getTotalPages());
        model.addAttribute("totalItems", ordersPage.getTotalElements());
        model.addAttribute("baseUrl", "/4Moos/admin/orders");
        List<CancelReason> reasons = Arrays.stream(CancelReason.values())
                .filter(reason -> reason.getAllowedRoles().contains(user.getRole()))
                .toList();
        model.addAttribute("reasonList", reasons);

        Map<Integer, OrderStage> nextStageMap = new LinkedHashMap<>();
        for (Orders order : ordersPage.getContent()) {
            nextStageMap.put(order.getIdOrder(), OrderStage.nextStage(order.getCurrentStatus()));
        }
        model.addAttribute("nextStageMap", nextStageMap);

        return "admin/orders";
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

        OrderStage nextStage = OrderStage.nextStage(order.getCurrentStatus());
        model.addAttribute("nextStage", nextStage);

        return "admin/order-details";
    }

    @PostMapping("/order/cancel/{idOrder}")
    public String cancelOrder(@ModelAttribute("user") UserDTO user, @PathVariable int idOrder,
                              @RequestParam String redirectSource, @RequestParam OrderStage currentStatus,
                              @RequestParam CancelReason reason, @RequestParam(required = false) Integer page) {
        orderService.checkCancelation(user.getIdUser(), currentStatus, reason);
        orderService.cancelOrder(idOrder, user.getRole(), reason);
        if ("detail".equals(redirectSource)) {
            return "redirect:/4Moos/admin/order/" + idOrder;
        } else {
            return "redirect:/4Moos/admin/orders/" + page;
        }
    }

    @PostMapping("/order/edit/{id}")
    public String updateOrderStatus(@PathVariable int id, @RequestParam("nextStatus") OrderStage nextStatus,
                                    @RequestParam String redirectSource, @RequestParam(required = false) Integer page) {

        orderService.editStatus(id, nextStatus);
        if ("detail".equals(redirectSource)) {
            return "redirect:/4Moos/admin/order/" + id;
        } else {
            return "redirect:/4Moos/admin/orders/" + page;
        }
    }


}
