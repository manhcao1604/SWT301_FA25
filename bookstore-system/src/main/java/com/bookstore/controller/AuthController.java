package com.bookstore.controller;

import com.bookstore.entity.User;
import com.bookstore.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("pageTitle", "Login - BookStore");
        return "user/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("pageTitle", "Register - BookStore");
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute User user,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Register - BookStore");
            return "user/register";
        }

        try {
            // Check if email already exists
            if (userService.getUserByEmail(user.getEmail()).isPresent()) {
                model.addAttribute("error", "Email already exists");
                return "user/register";
            }

            // Encode password and set default role
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(User.UserRole.CUSTOMER);
            user.setEnabled(true);

            userService.saveUser(user);

            redirectAttributes.addFlashAttribute("success",
                    "Registration successful! Please login with your credentials.");
            return "redirect:/login";

        } catch (Exception e) {
            log.error("Registration error: {}", e.getMessage());
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "user/register";
        }
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}