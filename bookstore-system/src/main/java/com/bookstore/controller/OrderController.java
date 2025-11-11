package com.bookstore.controller;

import com.bookstore.entity.Cart;
import com.bookstore.entity.Order;
import com.bookstore.entity.User;
import com.bookstore.service.CartService;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final UserService userService;

    @GetMapping("/checkout")
    public String showCheckout(@AuthenticationPrincipal UserDetails userDetails,
                               Model model) {
        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getCartDetails(user);
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("user", user);
        return "order/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(@AuthenticationPrincipal UserDetails userDetails,
                             @RequestParam String shippingAddress,
                             @RequestParam String paymentMethod,
                             @RequestParam(required = false) String customerNote,
                             RedirectAttributes redirectAttributes) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Cart cart = cartService.getCartDetails(user);
            if (cart == null || cart.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Giỏ hàng của bạn đang trống!");
                return "redirect:/cart";
            }

            // Tạo đơn hàng
            Order order = orderService.createOrder(user.getId(), cart.getItems(),
                    shippingAddress, paymentMethod, customerNote);

            redirectAttributes.addFlashAttribute("success",
                    "Đơn hàng của bạn đã được đặt thành công! Mã đơn hàng: " + order.getOrderNumber());
            return "redirect:/order/confirmation/" + order.getId();

        } catch (Exception e) {
            log.error("Error placing order: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                    "Có lỗi xảy ra khi đặt hàng: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/confirmation/{orderId}")
    public String orderConfirmation(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable Long orderId,
                                    Model model) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isEmpty()) {
            return "redirect:/";
        }

        // Kiểm tra xem đơn hàng có thuộc về user không
        if (!order.get().getUser().getId().equals(user.getId())) {
            return "redirect:/";
        }

        model.addAttribute("order", order.get());
        return "order/confirmation";
    }

    @GetMapping("/history")
    public String orderHistory(@AuthenticationPrincipal UserDetails userDetails,
                               Model model) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        List<Order> orders = orderService.getOrdersByUserId(user.getId());
        model.addAttribute("orders", orders);
        return "order/history";
    }

    @GetMapping("/detail/{orderId}")
    public String orderDetail(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long orderId,
                              Model model) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isEmpty() || !order.get().getUser().getId().equals(user.getId())) {
            return "redirect:/order/history";
        }

        model.addAttribute("order", order.get());
        return "order/detail";
    }

    @PostMapping("/cancel/{orderId}")
    public String cancelOrder(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long orderId,
                              RedirectAttributes redirectAttributes) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.cancelOrder(orderId, "Khách hàng yêu cầu hủy");
            redirectAttributes.addFlashAttribute("success",
                    "Đơn hàng đã được hủy thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể hủy đơn hàng: " + e.getMessage());
        }

        return "redirect:/order/history";
    }

    private User getUserFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userService.getUserByEmail(userDetails.getUsername())
                .orElse(null);
    }
}