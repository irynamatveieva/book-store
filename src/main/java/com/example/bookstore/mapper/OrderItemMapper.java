package com.example.bookstore.mapper;

import com.example.bookstore.config.MapperConfig;
import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.orderitem.OrderItemDto;
import com.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    @Mappings({
            @Mapping(target = "order", ignore = true),
            @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId"),
            @Mapping(target = "price", ignore = true)
    })
    OrderItem toEntity(CartItemDto cartItemDto);
}
