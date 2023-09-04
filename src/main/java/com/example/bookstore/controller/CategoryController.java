package com.example.bookstore.controller;

import com.example.bookstore.dto.book.BookDtoWithoutCategoryIds;
import com.example.bookstore.dto.category.CategoryDto;
import com.example.bookstore.dto.category.CreateCategoryRequestDto;
import com.example.bookstore.service.book.BookService;
import com.example.bookstore.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Category management", description = "Endpoints for managing categories")
@RequestMapping(value = "/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final BookService bookService;

    @PostMapping
    @Operation(summary = "Create a new category", description = "Create a new category")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @GetMapping
    @Operation(summary = "Get all categories", description = "Get a list of all categories")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a category by id", description = "Get a category by id")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    @Operation(summary = "Update a category by id", description = "Update a category by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody @Valid CreateCategoryRequestDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category by id", description = "Delete a category by id")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @Operation(summary = "Get books by category id",
            description = "Retrieve books by a specific category")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id,
                                                                Pageable pageable) {
        return bookService.findAllByCategoryId(id, pageable);
    }
}
