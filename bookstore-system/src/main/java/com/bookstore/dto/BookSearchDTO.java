package com.bookstore.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BookSearchDTO {
    private String keyword;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String author;
    private String sortBy;
    private String sortDirection;
    private Boolean onSaleOnly;
    private Boolean inStockOnly;

    // Default constructor with default values
    public BookSearchDTO() {
        this.sortBy = "createdAt";
        this.sortDirection = "DESC";
        this.onSaleOnly = false;
        this.inStockOnly = false;
    }
}