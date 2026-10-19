package com.emin.e_commerce.service.role;

import com.emin.e_commerce.dto.DtoMapper;
import com.emin.e_commerce.dto.RoleDto;
import com.emin.e_commerce.exception.AlreadyExistsException;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.Role;
import com.emin.e_commerce.repository.RoleRepository;
import com.emin.e_commerce.request.RoleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;
    private final DtoMapper dtoMapper;

    @Override
    @Transactional
    public RoleDto createRole(RoleRequest request) {
        String roleName = request.getName().toUpperCase();
        if (roleRepository.existsByName(roleName)) {
            throw new AlreadyExistsException("Role already exists: " + roleName);
        }
        Role role = new Role();
        role.setName(roleName);
        Role savedRole = roleRepository.save(role);
        return dtoMapper.toRoleDto(savedRole);
    }

    @Override
    @Transactional
    public RoleDto updateRole(Long id, RoleRequest request) {
        Role role = getRoleEntityById(id);
        String newName = request.getName().toUpperCase();
        if (!role.getName().equals(newName) && roleRepository.existsByName(newName)) {
            throw new AlreadyExistsException("Role already exists: " + newName);
        }
        role.setName(newName);
        Role updatedRole = roleRepository.save(role);
        return dtoMapper.toRoleDto(updatedRole);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDto getRoleById(Long id) {
        return dtoMapper.toRoleDto(getRoleEntityById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Role getRoleEntityById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role not found with ID: " + id));
    }

    @Override
    @Transactional
    public Role getRoleEntityByName(String name) {
        String roleName = name.toUpperCase();
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(roleName);
                    return roleRepository.save(role);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(dtoMapper::toRoleDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = getRoleEntityById(id);
        roleRepository.delete(role);
    }
}
