package com.bookstore.service;

import com.bookstore.entity.CartItem;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.bookstore.entity.User;
import com.bookstore.exception.UserNotFoundException;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookService bookService;

    public Order saveOrder(Order order) {
        log.info("Saving order: {}", order.getOrderNumber());
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        log.info("Fetching order by ID: {}", id);
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for user ID: {}", userId);
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    public Page<Order> getAllOrders(Pageable pageable) {
        log.info("Fetching all orders with pagination");
        return orderRepository.findAll(pageable);
    }

    public List<Order> getPendingOrders() {
        log.info("Fetching pending orders");
        return orderRepository.findPendingOrders();
    }

    @Transactional
    public Order createOrder(Long userId, List<CartItem> items,
                             String shippingAddress, String paymentMethod,
                             String customerNote) {
        log.info("Creating order for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        // Calculate total amount
        BigDecimal totalAmount = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Create order
        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .shippingAddress(shippingAddress)
                .paymentMethod(paymentMethod)
                .customerNote(customerNote)
                .status(Order.OrderStatus.PENDING)
                .paymentStatus(Order.PaymentStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        // Create order items
        List<OrderItem> orderItems = items.stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .book(item.getBook())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getSubtotal())
                        .build())
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Decrease stock for each book
        for (CartItem item : items) {
            bookService.decreaseStock(item.getBook().getId(), item.getQuantity());
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder.getOrderNumber());

        return savedOrder;
    }

    @Transactional
    public Order cancelOrder(Long orderId, String reason) {
        log.info("Cancelling order ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (order.getStatus() != Order.OrderStatus.PENDING) {
            throw new IllegalStateException("Cannot cancel order that is not pending. Current status: " + order.getStatus());
        }

        order.setStatus(Order.OrderStatus.CANCELLED);

        // Restore stock for each book
        order.getOrderItems().forEach(item ->
                item.getBook().increaseStock(item.getQuantity()));

        Order cancelledOrder = orderRepository.save(order);
        log.info("Order cancelled successfully: {}", cancelledOrder.getOrderNumber());

        return cancelledOrder;
    }

    public Order updateOrderStatus(Long orderId, Order.OrderStatus status) {
        log.info("Updating order {} status to: {}", orderId, status);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order updatePaymentStatus(Long orderId, Order.PaymentStatus paymentStatus) {
        log.info("Updating order {} payment status to: {}", orderId, paymentStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setPaymentStatus(paymentStatus);
        order.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Long countOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    public BigDecimal getTotalRevenue() {
        return orderRepository.findAll().stream()
                .filter(order -> order.getPaymentStatus() == Order.PaymentStatus.PAID)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}