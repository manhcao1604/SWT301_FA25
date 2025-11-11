package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String viewProfile(@AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfileForm(@AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/edit-profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid @ModelAttribute User userDetailsForm,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        User currentUser = getUserFromUserDetails(userDetails);
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "user/edit-profile";
        }

        try {
            User updatedUser = userService.updateUser(currentUser.getId(), userDetailsForm);
            redirectAttributes.addFlashAttribute("success", "Thông tin cá nhân đã được cập nhật!");
            return "redirect:/user/profile";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "user/edit-profile";
        }
    }

    @GetMapping("/change-password")
    public String changePasswordForm() {
        return "user/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp!");
            return "redirect:/user/change-password";
        }

        try {
            userService.changePassword(user.getId(), currentPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được thay đổi thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/user/change-password";
    }

    @GetMapping("/orders")
    public String userOrders(@AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        // Lấy danh sách đơn hàng của user
        // orderService.getOrdersByUserId(user.getId());
        return "user/orders";
    }

    @GetMapping("/addresses")
    public String userAddresses(@AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        User user = getUserFromUserDetails(userDetails);
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user/addresses.html";
    }

    private User getUserFromUserDetails(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }
        return userService.getUserByEmail(userDetails.getUsername())
                .orElse(null);
    }
}