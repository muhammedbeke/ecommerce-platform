package com.emin.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emin.e_commerce.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByProductId(Long id);

}
