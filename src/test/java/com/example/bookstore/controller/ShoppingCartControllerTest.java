package com.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cartitem.CartItemDto;
import com.example.bookstore.dto.cartitem.CreateCartItemRequestDto;
import com.example.bookstore.dto.cartitem.UpdateCartItemRequestDto;
import com.example.bookstore.dto.shoppingcart.ShoppingCartDto;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName(
            "Verify that get shopping cart method is working correctly"
    )
    void getShoppingCart_User_ReturnsShoppingCartDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user(getUser()))
        )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto expected = getShoppingCartDto();
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName(
            "Verify that add book to the shopping cart method is working correctly"
    )
    void addBookToTheShoppingCart_CreateCartItemRequestDto_ReturnsShoppingCartDto()
            throws Exception {
        CreateCartItemRequestDto requestDto = getCreateCartItemRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(getUser()))
        )
                .andExpect(status().isOk())
                .andReturn();

        ShoppingCartDto expected = getShoppingCartDto();
        Set<CartItemDto> cartItems = expected.getCartItems();
        Set<CartItemDto> newSet = new HashSet<>(cartItems);
        newSet.add(getCartItemDto());
        expected.setCartItems(newSet);
        ShoppingCartDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), ShoppingCartDto.class);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName(
            "Verify that update cart item method is working correctly"
    )
    void updateCartItem_UpdateCartItemRequestDto_ReturnsCartItemDto() throws Exception {
        UpdateCartItemRequestDto updateCartItemRequestDto = getUpdateCartItemRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(updateCartItemRequestDto);

        MvcResult result = mockMvc.perform(put("/cart/cart-items/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(getUser()))
        )
                .andExpect(status().isOk())
                .andReturn();

        CartItemDto expected = getCartItemDto();
        expected.setQuantity(updateCartItemRequestDto.getQuantity());
        CartItemDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CartItemDto.class);
        EqualsBuilder.reflectionEquals(actual, expected, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/fill-shopping-carts-table.sql",
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-users-table.sql",
            "classpath:database/fill-users-roles-table.sql",
            "classpath:database/fill-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql",
            "classpath:database/delete-from-users-table.sql",
            "classpath:database/delete-from-users-roles-table.sql",
            "classpath:database/delete-from-books-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName(
            "Verify that update cart item method is working correctly"
    )
    void removeCartItem_CartItemId_NoContent() throws Exception {
        mockMvc.perform(
                        delete("/cart/cart-items/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("admin.doe@example.com");
        user.setPassword("securePassword");
        user.setShippingAddress("123 Main St, City, Country");
        Role role = new Role();
        role.setId(2L);
        role.setName(Role.RoleName.ROLE_USER);
        Role role2 = new Role();
        role2.setId(1L);
        role2.setName(Role.RoleName.ROLE_ADMIN);
        user.setRoles(Set.of(role, role2));
        return user;
    }

    private ShoppingCartDto getShoppingCartDto() {
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setId(1L);
        shoppingCartDto.setUserId(1L);
        shoppingCartDto.setCartItems(Set.of(getCartItemDto()));
        return shoppingCartDto;
    }

    private CartItemDto getCartItemDto() {
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(1L);
        cartItemDto.setBookId(1L);
        cartItemDto.setBookTitle("title1");
        cartItemDto.setQuantity(13);
        return cartItemDto;
    }

    private CreateCartItemRequestDto getCreateCartItemRequestDto() {
        CreateCartItemRequestDto createCartItemRequestDto = new CreateCartItemRequestDto();
        createCartItemRequestDto.setBookId(1L);
        createCartItemRequestDto.setQuantity(5);
        return createCartItemRequestDto;
    }

    private UpdateCartItemRequestDto getUpdateCartItemRequestDto() {
        UpdateCartItemRequestDto updateCartItemRequestDto = new UpdateCartItemRequestDto();
        updateCartItemRequestDto.setQuantity(10);
        return updateCartItemRequestDto;
    }
}
