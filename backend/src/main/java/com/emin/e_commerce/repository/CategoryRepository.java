package com.emin.e_commerce.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emin.e_commerce.model.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

    boolean existsByName(String name);

	List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategoryId(Long parentId);
    
}
