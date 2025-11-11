package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Review;
import com.bookstore.entity.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.ReviewRepository;
import com.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public Review saveReview(Review review) {
        log.info("Saving review for book ID: {}", review.getBook().getId());
        return reviewRepository.save(review);
    }

    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    public Page<Review> getReviewsByBookId(Long bookId, Pageable pageable) {
        log.info("Fetching reviews for book ID: {}", bookId);
        return reviewRepository.findByBookIdOrderByCreatedAtDesc(bookId, pageable);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        log.info("Fetching reviews for user ID: {}", userId);
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public Page<Review> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @Transactional
    public Review addReview(Long bookId, Long userId, Integer rating, String comment) {
        log.info("Adding review for book ID: {} by user ID: {}", bookId, userId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if user already reviewed this book
        Optional<Review> existingReview = reviewRepository.findByBookIdAndUserId(bookId, userId);
        if (existingReview.isPresent()) {
            throw new IllegalStateException("You have already reviewed this book");
        }

        Review review = Review.builder()
                .book(book)
                .user(user)
                .rating(rating)
                .comment(comment)
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        // Update book's average rating and review count
        updateBookRating(bookId);

        return savedReview;
    }

    @Transactional
    public void updateBookRating(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);

        if (!reviews.isEmpty()) {
            double averageRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);

            int reviewCount = reviews.size();

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            book.setAverageRating(averageRating);
            book.setReviewCount(reviewCount);
            bookRepository.save(book);

            log.info("Updated book ID: {} rating to: {} based on {} reviews",
                    bookId, averageRating, reviewCount);
        }
    }

    public void deleteReview(Long reviewId) {
        log.info("Deleting review ID: {}", reviewId);
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));

        Long bookId = review.getBook().getId();
        reviewRepository.delete(review);

        // Update book rating after deletion
        updateBookRating(bookId);
    }

    public boolean hasUserReviewedBook(Long bookId, Long userId) {
        return reviewRepository.findByBookIdAndUserId(bookId, userId).isPresent();
    }

    public Double getAverageRatingForBook(Long bookId) {
        return reviewRepository.findAverageRatingByBookId(bookId);
    }

    public Long getReviewCountForBook(Long bookId) {
        return reviewRepository.countByBookId(bookId);
    }
}