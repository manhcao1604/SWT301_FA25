package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Cart;
import com.bookstore.service.BookService;
import com.bookstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    @GetMapping
    public String viewCart(Model model) {
        // Trong thực tế, lấy cart từ session hoặc user đăng nhập
        Cart cart = cartService.getCurrentCart();
        model.addAttribute("cart", cart);
        model.addAttribute("totalItems", cart.getTotalItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());

        return "cart/view";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long bookId, @RequestParam Integer quantity) {
        Optional<Book> book = bookService.getBookById(bookId);
        if (book.isPresent()) {
            // Trong thực tế, sessionId sẽ được lấy từ session của user
            String sessionId = "current-session-id"; // Đây là ví dụ, cần thay thế
            cartService.addToCart(sessionId, bookId, quantity);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCartItem(@RequestParam Long bookId, @RequestParam Integer quantity) {
        String sessionId = "current-session-id";
        cartService.updateCartItem(sessionId, bookId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long bookId) {
        String sessionId = "current-session-id";
        cartService.removeFromCart(sessionId, bookId);
        return "redirect:/cart";
    }

    @PostMapping("/clear")
    public String clearCart() {
        String sessionId = "current-session-id";
        cartService.clearCart(sessionId);
        return "redirect:/cart";
    }
}