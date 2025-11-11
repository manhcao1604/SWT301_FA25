package com.bookstore.repository;

import com.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Tìm kiếm sách
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    // Lọc theo danh mục
    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    Page<Book> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    // Sách đang giảm giá
    Page<Book> findByDiscountPriceIsNotNull(Pageable pageable);

    // Sách bán chạy (giả sử có trường soldQuantity)
    @Query("SELECT b FROM Book b WHERE b.stockQuantity > 0 AND b.available = true ORDER BY b.soldQuantity DESC")
    Page<Book> findBestSellers(Pageable pageable);
    Page<Book> findByAvailableTrue(Pageable pageable);
    // Sách mới
    Page<Book> findByOrderByCreatedAtDesc(Pageable pageable);

    // Lọc theo khoảng giá
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice AND b.available = true")
    Page<Book> findByPriceBetween(@Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  Pageable pageable);

    // Tìm kiếm nâng cao
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) AND " +
            "(:minPrice IS NULL OR b.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR b.price <= :maxPrice) AND " +
            "(:categoryId IS NULL OR EXISTS (SELECT c FROM b.categories c WHERE c.id = :categoryId))")
    Page<Book> advancedSearch(@Param("title") String title,
                              @Param("author") String author,
                              @Param("minPrice") BigDecimal minPrice,
                              @Param("maxPrice") BigDecimal maxPrice,
                              @Param("categoryId") Long categoryId,
                              Pageable pageable);

    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    // Kiểm tra tồn kho
    @Query("SELECT b.stockQuantity FROM Book b WHERE b.id = :bookId")
    Integer getStockQuantity(@Param("bookId") Long bookId);

    // Đếm sách có sẵn
    @Query("SELECT COUNT(b) FROM Book b WHERE b.available = true")
    Long countAvailableBooks();
}