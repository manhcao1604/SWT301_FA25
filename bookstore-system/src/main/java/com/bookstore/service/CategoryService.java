package com.bookstore.service;

import com.bookstore.entity.Category;
import com.bookstore.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        log.info("Fetching all categories");
        return categoryRepository.findAll();
    }

    public List<Category> getActiveCategories() {
        log.info("Fetching active categories");
        return categoryRepository.findByActiveTrue();
    }

    public List<Category> getRootCategories() {
        log.info("Fetching root categories");
        return categoryRepository.findByParentIsNull();
    }

    public List<Category> getChildCategories(Long parentId) {
        log.info("Fetching child categories for parent ID: {}", parentId);
        return categoryRepository.findByParentId(parentId);
    }

    public Optional<Category> getCategoryById(Long id) {
        log.info("Fetching category by ID: {}", id);
        return categoryRepository.findById(id);
    }

    public Optional<Category> getCategoryByName(String name) {
        log.info("Fetching category by name: {}", name);
        return categoryRepository.findByName(name);
    }

    public Optional<Category> getCategoryBySlug(String slug) {
        log.info("Fetching category by slug: {}", slug);
        return categoryRepository.findBySlug(slug);
    }

    public Category saveCategory(Category category) {
        log.info("Saving category: {}", category.getName());

        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name " + category.getName() + " already exists");
        }

        if (category.getSlug() != null && categoryRepository.existsBySlug(category.getSlug())) {
            throw new IllegalArgumentException("Category with slug " + category.getSlug() + " already exists");
        }

        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails) {
        log.info("Updating category with ID: {}", id);

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        existingCategory.setName(categoryDetails.getName());
        existingCategory.setDescription(categoryDetails.getDescription());
        existingCategory.setSlug(categoryDetails.getSlug());
        existingCategory.setActive(categoryDetails.getActive());
        existingCategory.setDisplayOrder(categoryDetails.getDisplayOrder());
        existingCategory.setImageUrl(categoryDetails.getImageUrl());

        return categoryRepository.save(existingCategory);
    }

    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));

        if (!category.getBooks().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated books");
        }

        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with child categories");
        }

        categoryRepository.delete(category);
    }

    public Category addChildCategory(Long parentId, Category childCategory) {
        log.info("Adding child category to parent ID: {}", parentId);

        Category parent = categoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent category not found with ID: " + parentId));

        childCategory.setParent(parent);
        Category savedChild = categoryRepository.save(childCategory);
        parent.addChildCategory(savedChild);

        return savedChild;
    }
}