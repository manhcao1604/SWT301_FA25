package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Category;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        log.info("Fetching all books with pageable: {}", pageable);
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> getBookById(Long id) {
        log.info("Fetching book by ID: {}", id);
        return bookRepository.findById(id);
    }

    public Book saveBook(Book book) {
        log.info("Saving book: {}", book.getTitle());

        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        log.info("Updating book with ID: {}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setDescription(bookDetails.getDescription());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPrice(bookDetails.getPrice());
        existingBook.setDiscountPrice(bookDetails.getDiscountPrice());
        existingBook.setStockQuantity(bookDetails.getStockQuantity());
        existingBook.setPublisher(bookDetails.getPublisher());
        existingBook.setPublicationDate(bookDetails.getPublicationDate());
        existingBook.setPages(bookDetails.getPages());
        existingBook.setLanguage(bookDetails.getLanguage());
        existingBook.setCoverImage(bookDetails.getCoverImage());
        existingBook.setAvailable(bookDetails.getAvailable());

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + id));
        bookRepository.delete(book);
    }

    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        log.info("Searching books with keyword: {}", keyword);
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findByAvailableTrue(pageable);
        }
        return bookRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public Page<Book> searchByAuthor(String author, Pageable pageable) {
        log.info("Searching books by author: {}", author);
        return bookRepository.findByAuthorContainingIgnoreCase(author, pageable);
    }

    public Page<Book> getBooksByCategory(Long categoryId, Pageable pageable) {
        log.info("Fetching books by category ID: {}", categoryId);
        return bookRepository.findByCategoryId(categoryId, pageable);
    }

    public Page<Book> getBooksOnSale(Pageable pageable) {
        log.info("Fetching books on sale");
        return bookRepository.findByDiscountPriceIsNotNull(pageable);
    }

    public Page<Book> getBestSellers(Pageable pageable) {
        log.info("Fetching best sellers");
        return bookRepository.findBestSellers(pageable);
    }

    public Page<Book> getNewBooks(Pageable pageable) {
        log.info("Fetching new books");
        return bookRepository.findByOrderByCreatedAtDesc(pageable);
    }

    public Page<Book> advancedSearch(String title, String author,
                                     BigDecimal minPrice, BigDecimal maxPrice,
                                     Long categoryId, Pageable pageable) {
        log.info("Advanced search with title: {}, author: {}, minPrice: {}, maxPrice: {}, categoryId: {}",
                title, author, minPrice, maxPrice, categoryId);
        return bookRepository.advancedSearch(title, author, minPrice, maxPrice, categoryId, pageable);
    }

    public boolean isBookAvailable(Long bookId, Integer quantity) {
        Integer stock = bookRepository.getStockQuantity(bookId);
        return stock != null && stock >= quantity;
    }

    @Transactional
    public void updateStock(Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        book.setStockQuantity(book.getStockQuantity() - quantity);
        bookRepository.save(book);
    }

    public void decreaseStock(Long bookId, int quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        book.decreaseStock(quantity);
        bookRepository.save(book);
    }

    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    public Long getAvailableBooksCount() {
        return bookRepository.countAvailableBooks();
    }

    public Book addCategoryToBook(Long bookId, Long categoryId) {
        log.info("Adding category {} to book {}", categoryId, bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        book.addCategory(category);
        return bookRepository.save(book);
    }

    public Book removeCategoryFromBook(Long bookId, Long categoryId) {
        log.info("Removing category {} from book {}", categoryId, bookId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));

        book.removeCategory(category);
        return bookRepository.save(book);
    }

    // Thêm method findByAvailableTrue để fix lỗi
    public Page<Book> findByAvailableTrue(Pageable pageable) {
        // Gọi repository method tương ứng (giả sử repository có method này)
        return bookRepository.findByAvailableTrue(pageable);
    }

    public Book toggleBookAvailability(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with ID: " + bookId));

        book.setAvailable(!book.getAvailable());
        return bookRepository.save(book);
    }
}