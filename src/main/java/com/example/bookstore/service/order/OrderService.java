package com.example.bookstore.service.order;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderCreateRequestDto requestDto);

    List<OrderResponseDto> getAllOrders(Long userId);

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrderStatus(Long userId, Long orderId, OrderUpdateRequestDto requestDto);
}
