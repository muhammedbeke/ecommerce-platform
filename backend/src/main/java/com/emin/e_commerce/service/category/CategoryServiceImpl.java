package com.emin.e_commerce.service.category;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emin.e_commerce.dto.CategoryDto;
import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.exception.AlreadyExistsException;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Category;
import com.emin.e_commerce.repository.CategoryRepository;
import com.emin.e_commerce.request.CategoryRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new AlreadyExistsException(categoryRequest.getName() + " already exists");
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        if (categoryRequest.getParentCategoryId() != null) {
            Category parent = categoryRepository.findById(categoryRequest.getParentCategoryId())
                    .orElseThrow(() -> new NotFoundException(
                            "Parent category not found! ID: " + categoryRequest.getParentCategoryId()));
            category.setParentCategory(parent);
        } else {
            category.setParentCategory(null);
        }

        Category savedCategory = categoryRepository.save(category);
        return dtoMapper.toCategoryDto(savedCategory);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryRequest categoryDetails) {
        Category existingCategory = getCategoryEntityById(id);

        if (!existingCategory.getName().equalsIgnoreCase(categoryDetails.getName())) {
            if (categoryRepository.existsByName(categoryDetails.getName())) {
                throw new AlreadyExistsException(categoryDetails.getName() + " already exists");
            }
            existingCategory.setName(categoryDetails.getName());
        }
        existingCategory.setDescription(categoryDetails.getDescription());

        Long parentId = categoryDetails.getParentCategoryId();
        if (parentId != null) {
            if (id.equals(parentId)) {
                throw new IllegalArgumentException("A category cannot be its own parent.");
            }
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new NotFoundException("Parent category not found! ID: " + parentId));
            
            if (isChildOf(parent, existingCategory)) {
                throw new IllegalArgumentException("Circular reference: A category cannot have one of its subcategories as a parent.");
            }

            existingCategory.setParentCategory(parent);
        } else {
            existingCategory.setParentCategory(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return dtoMapper.toCategoryDto(updatedCategory);
    }

    private boolean isChildOf(Category potentialParent, Category category) {
        Category current = potentialParent;
        while (current != null) {
            if (current.getId() != null && current.getId().equals(category.getId())) {
                return true;
            }
            current = current.getParentCategory();
        }
        return false;
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
         return dtoMapper.toCategoryDto(getCategoryEntityById(id));
    }

    @Override
    public Category getCategoryEntityById(Long id) {
         return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found! ID: " + id));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(dtoMapper::toCategoryDto)
                .toList();
    }

    @Override
    public List<CategoryDto> getMainCategories() {
        return categoryRepository.findByParentCategoryIsNull().stream()
                .map(dtoMapper::toCategoryDto)
                .toList();
    }

    @Override
    public List<CategoryDto> getCategoriesByParents(Long parentId) {
        return categoryRepository.findByParentCategoryId(parentId).stream()
                .map(dtoMapper::toCategoryDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryEntityById(id);
        categoryRepository.delete(category);
    }
}
