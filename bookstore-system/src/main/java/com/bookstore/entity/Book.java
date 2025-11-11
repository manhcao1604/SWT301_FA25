package com.bookstore.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"categories", "images"})
@ToString(exclude = {"categories", "images"})
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 20, unique = true)
    private String isbn;

    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal price;

    @Column(columnDefinition = "DECIMAL(10, 2)")
    private BigDecimal discountPrice;

    @Column(nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    // THÊM TRƯỜNG MỚI: soldQuantity để khắc phục lỗi findBestSellers
    @Builder.Default
    private Integer soldQuantity = 0;

    @Column(length = 100)
    private String publisher;

    private LocalDateTime publicationDate;

    private Integer pages;

    @Column(length = 20)
    private String language;

    @Column(length = 500)
    @Builder.Default
    private String coverImage = "default-book.jpg";

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "book_images", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Builder.Default
    private List<Category> categories = new ArrayList<>();

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Integer reviewCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // =============================
    // Lifecycle Callbacks
    // =============================

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (stockQuantity == null) stockQuantity = 0;
        if (reviewCount == null) reviewCount = 0;
        if (averageRating == null) averageRating = 0.0;
        if (available == null) available = true;
        if (soldQuantity == null) soldQuantity = 0; // Khởi tạo soldQuantity
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // =============================
    // Business Logic Methods
    // =============================

    public BigDecimal getCurrentPrice() {
        return discountPrice != null ? discountPrice : price;
    }

    public boolean isOnSale() {
        return discountPrice != null && discountPrice.compareTo(price) < 0;
    }

    public BigDecimal getDiscountPercentage() {
        if (isOnSale() && price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = price.subtract(discountPrice);
            return discountAmount.divide(price, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public boolean isInStock() {
        return available && stockQuantity != null && stockQuantity > 0;
    }

    public void decreaseStock(Integer quantity) {
        if (this.stockQuantity >= quantity) {
            this.stockQuantity -= quantity;
        } else {
            throw new IllegalArgumentException("Insufficient stock. Available: " + this.stockQuantity + ", Requested: " + quantity);
        }
    }

    public void increaseStock(Integer quantity) {
        this.stockQuantity += quantity;
    }

    public void addCategory(Category category) {
        if (!this.categories.contains(category)) {
            this.categories.add(category);
            // Cần đảm bảo Category.java có List<Book> books
        }
    }

    public void removeCategory(Category category) {
        if (this.categories != null) {
            this.categories.remove(category);
            // Cần đảm bảo Category.java có List<Book> books
        }
    }

    public void addImage(String imageUrl) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.add(imageUrl);
    }
}