package com.bookstore.entity;

import lombok.*;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String description;

    @Column(length = 100, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default  // Fix warning
    private List<Category> children = new ArrayList<>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @Builder.Default  // Fix warning
    private List<Book> books = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default  // Fix warning
    private Boolean active = true;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(length = 500)
    private String imageUrl = "https://example.com/default-category.jpg";  // Placeholder image

    public void addChildCategory(Category child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        child.setParent(this);
        this.children.add(child);
    }

    public void removeChildCategory(Category child) {
        if (this.children != null) {
            this.children.remove(child);
            child.setParent(null);
        }
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean isRootCategory() {
        return parent == null;
    }

    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }

    public void addBook(Book book) {
        if (this.books == null) {
            this.books = new ArrayList<>();
        }
        if (!this.books.contains(book)) {
            this.books.add(book);
            book.getCategories().add(this);
        }
    }

    public void removeBook(Book book) {
        if (this.books != null) {
            this.books.remove(book);
            book.getCategories().remove(this);
        }
    }
}