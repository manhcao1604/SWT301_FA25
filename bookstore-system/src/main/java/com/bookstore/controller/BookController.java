package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String sort,
            Model model) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Book> booksPage = bookService.getAllBooks(pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());
        model.addAttribute("categories", categoryService.getActiveCategories());

        return "books/list";
    }

    @GetMapping("/search")
    public String searchBooks(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookService.searchBooks(keyword, pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());

        return "books/search-results";
    }

    @GetMapping("/category/{categoryId}")
    public String booksByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookService.getBooksByCategory(categoryId, pageable);
        Optional<Category> category = categoryService.getCategoryById(categoryId);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("category", category.orElse(null));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());

        return "books/category";
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isEmpty()) {
            return "redirect:/books";
        }

        model.addAttribute("book", book.get());
        // Lấy sách liên quan (cùng danh mục đầu tiên)
        List<Category> categories = book.get().getCategories();
        if (!categories.isEmpty()) {
            model.addAttribute("relatedBooks", bookService.getBooksByCategory(
                    categories.get(0).getId(), PageRequest.of(0, 4)).getContent());
        } else {
            model.addAttribute("relatedBooks", List.of());
        }

        return "books/detail";
    }

    @GetMapping("/sale")
    public String booksOnSale(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookService.getBooksOnSale(pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());

        return "books/sale";
    }

    @GetMapping("/bestsellers")
    public String bestSellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage = bookService.getBestSellers(pageable);

        model.addAttribute("books", booksPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", booksPage.getTotalPages());
        model.addAttribute("totalItems", booksPage.getTotalElements());

        return "books/bestsellers";
    }

    private Pageable createPageable(int page, int size, String sort) {
        Sort sortObj = Sort.by(Sort.Direction.DESC, "createdAt");

        if (sort != null) {
            switch (sort) {
                case "price-asc":
                    sortObj = Sort.by(Sort.Direction.ASC, "price");
                    break;
                case "price-desc":
                    sortObj = Sort.by(Sort.Direction.DESC, "price");
                    break;
                case "title":
                    sortObj = Sort.by(Sort.Direction.ASC, "title");
                    break;
                case "newest":
                    sortObj = Sort.by(Sort.Direction.DESC, "createdAt");
                    break;
            }
        }

        return PageRequest.of(page, size, sortObj);
    }
}