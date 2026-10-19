package com.emin.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emin.e_commerce.dto.ListOfValuesDto;
import com.emin.e_commerce.request.ListOfValuesRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.ListOfValues.IListOfValuesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/list-of-values")
public class ListOfValueController {
    private final IListOfValuesService listOfValuesService;
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody ListOfValuesRequest request) {
        ListOfValuesDto dto = listOfValuesService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("List of value created successfully!", dto));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAll() {
        List<ListOfValuesDto> dtos = listOfValuesService.getAll();
        return ResponseEntity.ok(new ApiResponse("List of values retrieved successfully!", dtos));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse> getByType(@PathVariable String type) {
        List<ListOfValuesDto> dtos = listOfValuesService.getByType(type);
        return ResponseEntity.ok(new ApiResponse("List of values retrieved by type successfully!", dtos));
    }

    @DeleteMapping("/value/{id}/delete")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        listOfValuesService.delete(id);
        return ResponseEntity.ok(new ApiResponse("List of value deleted successfully!", null));
    }
}
