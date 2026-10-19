package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.CategoryDto;
import com.emin.e_commerce.request.CategoryRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.category.ICategoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        List<CategoryDto> dtos = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse("All categories retrieved successfully!", dtos));
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
        CategoryDto dto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse("Category retrieved successfully!", dto));
    }


    @GetMapping("/main")
    public ResponseEntity<ApiResponse> getMainCategories() {
        List<CategoryDto> dtos = categoryService.getMainCategories();
        return ResponseEntity.ok(new ApiResponse("Main categories retrieved successfully!", dtos));
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<ApiResponse> getCategoriesByParents(@PathVariable Long parentId) {
        List<CategoryDto> dtos = categoryService.getCategoriesByParents(parentId);
        return ResponseEntity.ok(new ApiResponse("Subcategories retrieved successfully!", dtos));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryDto dto = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Category created successfully!", dto));
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryRequest request) {
        CategoryDto dto = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(new ApiResponse("Category updated successfully!", dto));
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse("Category deleted successfully!", null));
    }
}
