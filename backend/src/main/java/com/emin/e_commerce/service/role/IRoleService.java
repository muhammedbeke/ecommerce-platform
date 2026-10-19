package com.emin.e_commerce.service.role;

import com.emin.e_commerce.dto.RoleDto;
import com.emin.e_commerce.model.Role;
import com.emin.e_commerce.request.RoleRequest;

import java.util.List;

public interface IRoleService {
    RoleDto createRole(RoleRequest request);
    RoleDto updateRole(Long id, RoleRequest request);
    RoleDto getRoleById(Long id);
    Role getRoleEntityById(Long id);
    Role getRoleEntityByName(String name);
    List<RoleDto> getAllRoles();
    void deleteRole(Long id);
}
