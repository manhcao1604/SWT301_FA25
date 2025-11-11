package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.entity.CartItem;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    public Cart getOrCreateCart(String sessionId) {
        log.debug("Getting or creating cart for session: {}", sessionId);

        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .sessionId(sessionId)
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    public Cart addToCart(String sessionId, Long bookId, Integer quantity) {
        log.info("Adding book {} to cart for session {}, quantity: {}", bookId, sessionId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        if (!book.isInStock()) {
            throw new IllegalStateException("Book is out of stock");
        }

        if (quantity > book.getStockQuantity()) {
            throw new IllegalStateException("Requested quantity exceeds available stock");
        }

        Cart cart = getOrCreateCart(sessionId);
        cart.addItem(book, quantity);

        return cartRepository.save(cart);
    }

    public Cart updateCartItem(String sessionId, Long bookId, Integer quantity) {
        log.info("Updating cart item for session {}, book {}, quantity: {}", sessionId, bookId, quantity);

        Cart cart = getCartBySessionId(sessionId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        cart.updateItemQuantity(book, quantity);

        return cartRepository.save(cart);
    }

    public Cart removeFromCart(String sessionId, Long bookId) {
        log.info("Removing book {} from cart for session {}", bookId, sessionId);

        Cart cart = getCartBySessionId(sessionId);
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        cart.removeItem(book);

        return cartRepository.save(cart);
    }

    public void clearCart(String sessionId) {
        log.info("Clearing cart for session: {}", sessionId);

        Cart cart = getCartBySessionId(sessionId);
        cart.clear();
        cartRepository.save(cart);
    }

    public Cart getCartBySessionId(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for session: " + sessionId));
    }

    public void cleanupExpiredCarts() {
        log.info("Cleaning up expired carts");
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(7);
        cartRepository.deleteExpiredCarts(expiryDate);
    }

    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public BigDecimal calculateCartTotal(String sessionId) {
        Cart cart = getCartBySessionId(sessionId);
        return cart.getTotalPrice();
    }

    public int getCartItemCount(String sessionId) {
        try {
            Cart cart = getCartBySessionId(sessionId);
            return cart.getTotalItems();
        } catch (IllegalArgumentException e) {
            return 0;
        }
    }

    // Method for getting current cart (simplified - in real app, you'd get session from HTTP request)
    public Cart getCurrentCart() {
        // This is a simplified version. In a real application, you would get the session ID from the HTTP session.
        // For now, we generate a new session ID for demonstration.
        String sessionId = generateSessionId();
        return getOrCreateCart(sessionId);
    }

    // Thêm method getCartDetails để fix lỗi
    public Cart getCartDetails(User user) {
        // Giả sử giỏ hàng liên kết với user qua sessionId dựa trên user ID
        String sessionId = "user-cart-" + user.getId();  // Có thể thay bằng logic thực tế (tìm cart theo user)
        return getOrCreateCart(sessionId);  // Hoặc throw exception nếu không tìm thấy
    }
}