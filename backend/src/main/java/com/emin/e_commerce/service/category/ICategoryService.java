package com.emin.e_commerce.service.category;

import java.util.List;

import com.emin.e_commerce.dto.CategoryDto;
import com.emin.e_commerce.model.Category;
import com.emin.e_commerce.request.CategoryRequest;

public interface ICategoryService {
    CategoryDto createCategory(CategoryRequest categoryRequest);
    
    CategoryDto updateCategory(Long id, CategoryRequest categoryDetails);
    
    CategoryDto getCategoryById(Long id);

    Category getCategoryEntityById(Long id);
    
    List<CategoryDto> getAllCategories();
    
    List<CategoryDto> getMainCategories(); // En üst seviye kategoriler

    List<CategoryDto> getCategoriesByParents(Long parentId); // Parent alt kategorileri
    
    void deleteCategory(Long id);
}
