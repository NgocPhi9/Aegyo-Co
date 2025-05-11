package group1.commerce.service;

import group1.commerce.entity.Orders;
import group1.commerce.repository.OrderRepository;
import jakarta.persistence.criteria.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(Orders order) {
        orderRepository.save(order);
    }
}
