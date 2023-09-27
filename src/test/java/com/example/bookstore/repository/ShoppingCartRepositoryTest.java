package com.example.bookstore.repository;

import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.repository.shoppingcart.ShoppingCartRepository;
import java.util.Optional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NON_EXISTING_CATEGORY_ID = 100L;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName(
            "Verify that the find shopping cart by user id method is working correctly"
    )
    @Sql(scripts = {
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-shopping-carts-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findShoppingCartByUserId_ExistingUserId_ReturnsOptionalShoppingCart() {
        Optional<ShoppingCart> actual
                = shoppingCartRepository.findShoppingCartByUserId(EXISTING_CATEGORY_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1, actual.get().getCartItems().size());
    }

    @Test
    @DisplayName(
            "Verify that the find shopping cart by user id method is working correctly"
    )
    @Sql(scripts = {
            "classpath:database/fill-cart-items-table.sql",
            "classpath:database/fill-shopping-carts-table.sql"
            }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-from-shopping-carts-table.sql",
            "classpath:database/delete-from-cart-items-table.sql"
            }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findShoppingCartByUserId_NonExistingUserId_ReturnsOptionalShoppingCart() {
        Optional<ShoppingCart> actual
                = shoppingCartRepository.findShoppingCartByUserId(NON_EXISTING_CATEGORY_ID);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(Optional.empty(), actual);
    }
}
