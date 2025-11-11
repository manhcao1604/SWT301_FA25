package com.bookstore.entity;

import lombok.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"items"}) // QUAN TRỌNG: Loại trừ items khỏi toString
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String sessionId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    @Column(precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "total_items")
    @Builder.Default
    private Integer totalItems = 0;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateTotals();
    }

    public void calculateTotals() {
        this.totalItems = items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        this.totalPrice = items.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(Book book, int quantity) {
        CartItem existingItem = findItemByBook(book);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(this)
                    .book(book)
                    .quantity(quantity)
                    .unitPrice(book.getCurrentPrice())
                    .build();
            items.add(newItem);
        }
        calculateTotals();
    }

    public void removeItem(Book book) {
        items.removeIf(item -> item.getBook().equals(book));
        calculateTotals();
    }

    public void updateItemQuantity(Book book, int quantity) {
        CartItem item = findItemByBook(book);
        if (item != null) {
            if (quantity <= 0) {
                removeItem(book);
            } else {
                item.setQuantity(quantity);
            }
        }
        calculateTotals();
    }

    public void clear() {
        items.clear();
        calculateTotals();
    }

    private CartItem findItemByBook(Book book) {
        return items.stream()
                .filter(item -> item.getBook().equals(book))
                .findFirst()
                .orElse(null);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Ghi đè phương thức toString để tránh circular reference
    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", totalItems=" + totalItems +
                ", totalPrice=" + totalPrice +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}