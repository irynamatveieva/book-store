package com.example.bookstore.service.order.impl;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import com.example.bookstore.mapper.OrderItemMapper;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.order.OrderRepository;
import com.example.bookstore.repository.orderitem.OrderItemRepository;
import com.example.bookstore.repository.user.UserRepository;
import com.example.bookstore.service.book.BookService;
import com.example.bookstore.service.order.OrderService;
import com.example.bookstore.service.shoppingcart.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ShoppingCartService shoppingCartService;
    private final BookService bookService;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderCreateRequestDto requestDto) {
        Order order = new Order();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("Can`t find user by id:" + userId)
        );
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        Set<OrderItem> orderItems =
                shoppingCartService.getShoppingCart(userId).getCartItems().stream()
                        .map(orderItemMapper::toEntity)
                        .collect(Collectors.toSet());
        orderItems.forEach(orderItem ->
                orderItem.setPrice(bookService.findById(orderItem.getBook().getId()).getPrice()));
        order.setOrderItems(orderItems);
        order.setShippingAddress(requestDto.getShippingAddress());
        BigDecimal total = BigDecimal.valueOf(0);
        for (OrderItem item : orderItems) {
            total = total.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);
        orderItems.forEach(orderItem ->
                orderItem.setOrder(savedOrder));
        orderItemRepository.saveAll(orderItems);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can`t find order by id:" + id)
        );
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Long userId) {
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long userId,
                                              Long orderId,
                                              OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new RuntimeException("Can`t find order by id:" + orderId)
        );
        order.setStatus(requestDto.getStatus());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
}
