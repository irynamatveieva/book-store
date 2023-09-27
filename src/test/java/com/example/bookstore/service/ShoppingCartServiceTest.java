package com.example.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.cartitem.CartItemRepository;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import com.example.bookstore.service.shoppingcart.impl.ShoppingCartServiceImpl;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long EXISTING_ID = 1L;
    private static final Long NON_EXISTING_ID = 100L;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private CartItemMapper cartItemMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    private ShoppingCart shoppingCart;
    private ShoppingCartDto shoppingCartDto;
    private CreateCartItemRequestDto createCartItemRequestDto;
    private CartItem cartItem;
    private CartItemDto cartItemDto;
    private UpdateCartItemRequestDto updateCartItemRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        shoppingCart = getShoppingCart();
        shoppingCartDto = getShoppingCartDto();
        createCartItemRequestDto = getCreateCartItemRequestDto();
        cartItem = getCartItem();
        cartItemDto = getCartItemDto();
        updateCartItemRequestDto = getUpdateCartItemRequestDto();
        user = getUser();
    }

    @Test
    @DisplayName(
            "Verify that the get shopping cart method is working correctly"
    )
    void getShoppingCart_ExistingId_ReturnsShoppingCartDto() {
        Mockito.when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto expected = shoppingCartDto;
        ShoppingCartDto actual = shoppingCartService.getShoppingCart(EXISTING_ID);

        assertThat(actual).isEqualTo(expected);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartMapper);
    }

    @Test
    @DisplayName(
            "Verify that the get shopping cart method is working correctly with non existing id"
    )
    void getShoppingCart_NonExistingId_ReturnsShoppingCartDto() {
        Mockito.when(shoppingCartRepository.findShoppingCartByUserId(NON_EXISTING_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCart(NON_EXISTING_ID));

        String expected = "Can`t find shopping cart by id";
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(shoppingCartRepository, times(1))
                .findShoppingCartByUserId(NON_EXISTING_ID);
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    @Test
    @DisplayName(
            "Verify that the add to the shopping cart method is working correctly"
    )
    void addBookToTheShoppingCart_CreateCartItemRequestDto_ReturnsShoppingCartDto() {
        Mockito.when(shoppingCartRepository.findShoppingCartByUserId(EXISTING_ID))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemMapper.toEntity(createCartItemRequestDto)).thenReturn(cartItem);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.setCartItems(Set.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto expected = shoppingCartDto;
        ShoppingCartDto actual = shoppingCartService
                .addBookToTheShoppingCart(EXISTING_ID, createCartItemRequestDto);
        assertThat(actual).isEqualTo(expected);
        verify(shoppingCartRepository, times(2)).findShoppingCartByUserId(EXISTING_ID);
        verify(cartItemMapper, times(1)).toEntity(createCartItemRequestDto);
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(shoppingCartMapper, times(1)).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, cartItemMapper,
                cartItemRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName(
            "Verify that the update cart item method is working correctly"
    )
    void updateCartItem_CartItemIdAndUpdateCartItemRequestDto_ReturnsCartItemDto() {
        Mockito.when(cartItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(cartItem));
        cartItem.setQuantity(updateCartItemRequestDto.getQuantity());
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        cartItemDto.setQuantity(updateCartItemRequestDto.getQuantity());
        Mockito.when(cartItemMapper.toDto(cartItem)).thenReturn(cartItemDto);

        CartItemDto expected = cartItemDto;
        CartItemDto actual = shoppingCartService.updateCartItem(
                anyLong(), EXISTING_ID, updateCartItemRequestDto);
        assertThat(actual).isEqualTo(expected);
        verify(cartItemRepository, times(1)).findById(EXISTING_ID);
        verify(cartItemRepository, times(1)).save(cartItem);
        verify(cartItemMapper, times(1)).toDto(cartItem);
        verifyNoMoreInteractions(cartItemRepository, cartItemMapper);
    }

    @Test
    @DisplayName(
            "Verify that the update cart item method is working correctly with non existing id"
    )
    void updateCartItem_NonExistingCartItemId_ThrowsException() {
        Mockito.when(cartItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService
                        .updateCartItem(anyLong(), NON_EXISTING_ID, updateCartItemRequestDto));

        String expected = "Can`t find cartItem by id" + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(cartItemRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    @DisplayName(
            "Verify that the remove cart item method is working correctly"
    )
    void removeCartItem_ExistingCartItemId_Ok() {
        Mockito.when(cartItemRepository.findById(EXISTING_ID)).thenReturn(Optional.of(cartItem));
        Mockito.doNothing().when(cartItemRepository).delete(cartItem);

        shoppingCartService.removeCartItem(EXISTING_ID);

        verify(cartItemRepository, times(1)).findById(EXISTING_ID);
        verify(cartItemRepository, times(1)).delete(cartItem);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    @DisplayName(
            "Verify that the remove cart item method is working correctly with non existing id"
    )
    void removeCartItem_NonExistingCartItemId_ThrowsException() {
        Mockito.when(cartItemRepository.findById(NON_EXISTING_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService
                        .removeCartItem(NON_EXISTING_ID));

        String expected = "Can`t find cartItem by id" + NON_EXISTING_ID;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(cartItemRepository, times(1)).findById(NON_EXISTING_ID);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    @DisplayName(
            "Verify that the register new shopping cart method is working correctly"
    )
    void registerNewShoppingCart_User_Ok() {
        Mockito.when(shoppingCartRepository.save(Mockito.any(ShoppingCart.class)))
                .thenReturn(shoppingCart);

        shoppingCartService.registerNewShoppingCart(user);

        verify(shoppingCartRepository, times(1)).save(Mockito.any(ShoppingCart.class));
        verifyNoMoreInteractions(shoppingCartRepository);
    }

    private ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        return shoppingCart;
    }

    private ShoppingCartDto getShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(Set.of(new CartItemDto()));
        return shoppingCartDto;
    }

    private CreateCartItemRequestDto getCreateCartItemRequestDto() {
        CreateCartItemRequestDto cartItemRequestDto = new CreateCartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(3);
        return cartItemRequestDto;
    }

    private CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(new ShoppingCart());
        Book book = new Book();
        book.setId(1L);
        book.setTitle("title1");
        cartItem.setBook(book);
        cartItem.setQuantity(3);
        return cartItem;
    }

    private CartItemDto getCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setQuantity(3);
        cartItemDto.setBookTitle("title1");
        return cartItemDto;
    }

    private UpdateCartItemRequestDto getUpdateCartItemRequestDto() {
        UpdateCartItemRequestDto cartItemRequestDto = new UpdateCartItemRequestDto();
        cartItemRequestDto.setQuantity(5);
        return cartItemRequestDto;
    }

    private User getUser() {
        User user = new User();
        user.setId(EXISTING_ID);
        user.setEmail("email@mail.com");
        user.setFirstName("john");
        user.setLastName("doe");
        return user;
    }
}
