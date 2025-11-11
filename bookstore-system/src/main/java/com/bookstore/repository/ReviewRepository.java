package com.bookstore.repository;

import com.bookstore.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByBookIdOrderByCreatedAtDesc(Long bookId, Pageable pageable);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Review> findByBookId(Long bookId);

    Optional<Review> findByBookIdAndUserId(Long bookId, Long userId);

    Long countByBookId(Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.book.id = :bookId AND r.rating = :rating")
    Long countByBookIdAndRating(@Param("bookId") Long bookId, @Param("rating") Integer rating);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}