package com.bookstore.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String imageUrl;
    private Boolean active;
    private Integer displayOrder;
    private Long parentId;
    private String parentName;
    private List<CategoryDTO> children = new ArrayList<>();
    private Integer bookCount;

    // Constructors
    public CategoryDTO() {}

    public CategoryDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}