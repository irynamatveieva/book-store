package com.example.bookstore.controller;

import com.example.bookstore.dto.order.OrderCreateRequestDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.OrderUpdateRequestDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.model.User;
import com.example.bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Order management", description = "Endpoints for managing order")
@RequestMapping(value = "/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Place an order", description = "Place an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderResponseDto createOrder(
            Authentication authentication,
            @Valid @RequestBody OrderCreateRequestDto orderCreateRequestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user.getId(), orderCreateRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieve user's order history")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderResponseDto> getAllOrders(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrders(user.getId());
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status",
            description = "Update order status by order id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderResponseDto updateOrder(
            Authentication authentication,
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.updateOrderStatus(user.getId(), id, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all orders", description = "Retrieve user's order history")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<OrderItemDto> getAllOrderItems(@PathVariable Long orderId) {
        return new ArrayList<>(orderService.getOrderById(orderId).getOrderItems());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item",
            description = "Retrieve a specific OrderItem within an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderItemDto getOrderItem(@PathVariable Long orderId,
                                     @PathVariable Long itemId) {
        return getAllOrderItems(orderId).stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(
                        () -> new RuntimeException("Can`t get item by id:" + itemId)
                );
    }
}
