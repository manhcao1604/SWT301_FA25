package com.bookstore.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookNotFoundException(BookNotFoundException e, Model model) {
        log.error("Book not found: {}", e.getMessage());
        model.addAttribute("errorMessage", "Sách không tồn tại hoặc đã bị xóa.");
        model.addAttribute("errorCode", "404");
        return "error/404";
    }

    @ExceptionHandler(InsufficientStockException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInsufficientStockException(InsufficientStockException e, Model model) {
        log.error("Insufficient stock: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/400";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        log.error("User not found: {}", e.getMessage());
        model.addAttribute("errorMessage", "Người dùng không tồn tại.");
        return "error/404";
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMaxSizeException(MaxUploadSizeExceededException e, Model model) {
        log.error("File upload size exceeded: {}", e.getMessage());
        model.addAttribute("errorMessage", "Kích thước file vượt quá giới hạn cho phép (10MB).");
        return "error/400";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException e, Model model) {
        log.error("Page not found: {}", e.getRequestURL());
        model.addAttribute("errorMessage", "Trang bạn tìm kiếm không tồn tại.");
        model.addAttribute("errorCode", "404");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception e, HttpServletRequest request, Model model) {
        log.error("Internal server error for URL: {} - Error: {}",
                request.getRequestURL(), e.getMessage(), e);

        model.addAttribute("errorMessage", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
        model.addAttribute("errorCode", "500");
        return "error/500";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        log.error("Illegal argument: {}", e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        return "error/400";
    }
}