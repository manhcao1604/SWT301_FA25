package com.bookstore.controller;

import com.bookstore.config.SecurityConfig;
import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CategoryService categoryService;

    @Test
    @WithAnonymousUser  // Allow anonymous access as per SecurityConfig
    void listBooks_ShouldReturnListView() throws Exception {
        // Add the default sort to match the controller's default behavior
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(0, 12, sort);

        List<Book> books = Collections.emptyList();
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());
        when(bookService.getAllBooks(pageable)).thenReturn(bookPage);
        when(categoryService.getActiveCategories()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @WithAnonymousUser  // Allow anonymous access
    void searchBooks_ShouldReturnSearchView() throws Exception {
        Pageable pageable = PageRequest.of(0, 12);
        List<Book> books = Collections.emptyList();
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookService.searchBooks("keyword", pageable)).thenReturn(bookPage);

        mockMvc.perform(get("/books/search").param("keyword", "keyword"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/search-results"))
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("keyword", "keyword"));
    }

    @Test
    @WithAnonymousUser  // Allow anonymous access
    void booksByCategory_ShouldReturnCategoryView() throws Exception {
        Pageable pageable = PageRequest.of(0, 12);
        List<Book> books = Collections.emptyList();
        Page<Book> bookPage = new PageImpl<>(books);
        when(bookService.getBooksByCategory(1L, pageable)).thenReturn(bookPage);
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/books/category/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/category"))
                .andExpect(model().attributeExists("books"));
    }
}