package com.emin.e_commerce.controller;

import com.emin.e_commerce.dto.RoleDto;
import com.emin.e_commerce.request.RoleRequest;
import com.emin.e_commerce.response.ApiResponse;
import com.emin.e_commerce.service.role.IRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final IRoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createRole(@Valid @RequestBody RoleRequest request) {
        RoleDto dto = roleService.createRole(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Role created successfully!", dto));
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<ApiResponse> getRoleById(@PathVariable Long id) {
        RoleDto dto = roleService.getRoleById(id);
        return ResponseEntity.ok(new ApiResponse("Role retrieved successfully!", dto));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllRoles() {
        List<RoleDto> dtos = roleService.getAllRoles();
        return ResponseEntity.ok(new ApiResponse("All roles retrieved successfully!", dtos));
    }

    @PutMapping("/role/{id}/update")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequest request) {
        RoleDto dto = roleService.updateRole(id, request);
        return ResponseEntity.ok(new ApiResponse("Role updated successfully!", dto));
    }

    @DeleteMapping("/role/{id}/delete")
    public ResponseEntity<ApiResponse> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok(new ApiResponse("Role deleted successfully!", null));
    }
}
