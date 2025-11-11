package com.bookstore.controller;

import com.bookstore.config.SecurityConfig;
import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @MockBean
    private BookService bookService;

    @Test
    @WithMockUser
    void viewCart_ShouldReturnCartView() throws Exception {
        // Tạo mock cart với empty items để tránh circular reference
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setSessionId("test-session");
        mockCart.setTotalItems(0); // Đặt là 0 để tránh circular reference
        mockCart.setTotalPrice(new BigDecimal("0.00"));
        mockCart.setItems(Collections.emptyList()); // Sử dụng empty list

        when(cartService.getCurrentCart()).thenReturn(mockCart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/view"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("totalPrice"));
    }

    @Test
    @WithMockUser
    void addToCart_ShouldRedirectToCart() throws Exception {
        // Mock book
        Book mockBook = Book.builder()
                .id(1L)
                .title("Test Book")
                .author("Test Author")
                .price(new BigDecimal("25.00"))
                .stockQuantity(10)
                .build();

        when(bookService.getBookById(1L)).thenReturn(java.util.Optional.of(mockBook));

        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        when(cartService.addToCart(anyString(), anyLong(), anyInt())).thenReturn(mockCart);

        mockMvc.perform(post("/cart/add")
                        .param("bookId", "1")
                        .param("quantity", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void updateCartItem_ShouldRedirectToCart() throws Exception {
        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        when(cartService.updateCartItem(anyString(), anyLong(), anyInt())).thenReturn(mockCart);

        mockMvc.perform(post("/cart/update")
                        .param("bookId", "1")
                        .param("quantity", "2")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void removeFromCart_ShouldRedirectToCart() throws Exception {
        // Mock cart
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        when(cartService.removeFromCart(anyString(), anyLong())).thenReturn(mockCart);

        mockMvc.perform(post("/cart/remove")
                        .param("bookId", "1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void clearCart_ShouldRedirectToCart() throws Exception {
        mockMvc.perform(post("/cart/clear")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));
    }

    @Test
    @WithMockUser
    void viewCart_WithEmptyCart_ShouldReturnEmptyView() throws Exception {
        Cart emptyCart = new Cart();
        emptyCart.setId(1L);
        emptyCart.setSessionId("test-session");
        emptyCart.setTotalItems(0);
        emptyCart.setTotalPrice(BigDecimal.ZERO);
        emptyCart.setItems(Collections.emptyList()); // Sử dụng empty list

        when(cartService.getCurrentCart()).thenReturn(emptyCart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/view"))
                .andExpect(model().attributeExists("cart"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void viewCart_WithAdminUser_ShouldWork() throws Exception {
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setSessionId("test-session");
        mockCart.setTotalItems(0); // Đặt là 0
        mockCart.setTotalPrice(new BigDecimal("0.00"));
        mockCart.setItems(Collections.emptyList()); // Sử dụng empty list

        when(cartService.getCurrentCart()).thenReturn(mockCart);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/view"));
    }
}