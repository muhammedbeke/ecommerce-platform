package com.emin.e_commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emin.e_commerce.model.ListOfValues;

public interface ListOfValuesRepository extends JpaRepository<ListOfValues,Long>{
    Optional<ListOfValues> findByTypeAndCode(String type,String Code);
    List<ListOfValues> findByType(String type);
    boolean existsByTypeAndCode(String type, String code);
}
