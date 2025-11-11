// File: src/test/java/com/bookstore/service/BookServiceTest.java

package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Technology")
                .build();

        book1 = Book.builder()
                .id(1L)
                .title("Java Programming")
                .author("John Doe")
                .isbn("1234567890")
                .price(new BigDecimal("29.99"))
                .stockQuantity(10)
                .available(true)
                .build();

        book2 = Book.builder()
                .id(2L)
                .title("Spring Boot in Action")
                .author("Jane Smith")
                .isbn("0987654321")
                .price(new BigDecimal("39.99"))
                .discountPrice(new BigDecimal("34.99"))
                .stockQuantity(5)
                .available(true)
                .build();
    }

    @Test
    void testGetAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Java Programming", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(99L);

        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveBook_Success() {
        when(bookRepository.existsByIsbn("1234567890")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book result = bookService.saveBook(book1);

        assertNotNull(result);
        assertEquals("Java Programming", result.getTitle());
        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testSaveBook_DuplicateIsbn() {
        when(bookRepository.existsByIsbn("1234567890")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.saveBook(book1);
        });
    }

    @Test
    void testSearchBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = Arrays.asList(book1);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findByTitleContainingIgnoreCase("Java", pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.searchBooks("Java", pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Java Programming", result.getContent().get(0).getTitle());
    }

    @Test
    void testUpdateStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        bookService.updateStock(1L, 5);

        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testDecreaseStock_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        bookService.decreaseStock(1L, 5);

        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    void testDecreaseStock_InsufficientStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.decreaseStock(1L, 15);
        });
    }

    @Test
    void testIsBookAvailable_SufficientStock() {
        when(bookRepository.getStockQuantity(1L)).thenReturn(10);

        boolean result = bookService.isBookAvailable(1L, 5);

        assertTrue(result);
    }

    @Test
    void testIsBookAvailable_InsufficientStock() {
        when(bookRepository.getStockQuantity(1L)).thenReturn(3);

        boolean result = bookService.isBookAvailable(1L, 5);

        assertFalse(result);
    }

    @Test
    void testToggleBookAvailability() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenReturn(book1);

        Book result = bookService.toggleBookAvailability(1L);

        assertNotNull(result);
        verify(bookRepository, times(1)).save(book1);
    }
}