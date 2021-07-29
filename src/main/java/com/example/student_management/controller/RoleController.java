package com.example.student_management.controller;

import com.example.student_management.converter.RoleConverter;
import com.example.student_management.domain.Role;
import com.example.student_management.dto.RoleDto;
import com.example.student_management.service.RoleService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RoleController {
    private final RoleService roleService;
    private final RoleConverter roleConverter;

    public RoleController(RoleService roleService, RoleConverter roleConverter) {
        this.roleService = roleService;
        this.roleConverter = roleConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_roles')")
    public ResponseEntity<Object> getAllRole(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Role> specification = new CustomSpecificationBuilder<Role>(ServiceUtils.getFilterParam(filter, Role.class)).build();
        List<Role> roles = roleService.findAll(specification, pageable);
        List<RoleDto> roleDtoList = roles.stream().map(roleConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(roleDtoList, HttpStatus.OK);
    }

    @GetMapping("/{roleName}")
    @PreAuthorize("hasAnyAuthority('can_view_role_by_id', 'can_view_all_roles')")
    public ResponseEntity<Object> getRoleByName(@PathVariable("roleName") String roleName) {
        Role role = roleService.findByRoleName(roleName);

        return new ResponseEntity<>(roleConverter.toDto(role), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_role')")
    public ResponseEntity<Object> addRole(@RequestBody RoleDto roleDto) {
        Role role = roleConverter.toEntity(roleDto);
        Role insertedRole = roleService.add(role);
        return new ResponseEntity<>(roleConverter.toDto(insertedRole), HttpStatus.CREATED);
    }

    @PutMapping("/{roleName}")
    @PreAuthorize("hasAnyAuthority('can_update_role')")
    public ResponseEntity<Object> getRoleByName(@PathVariable("roleName") String roleName, @RequestBody RoleDto roleDto) {
        Role updatedTarget = roleService.findByRoleName(roleName);
        Role updatedSource = roleConverter.toEntity(roleDto);
        updatedSource.setRoleName(roleName);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Role updatedRole = roleService.update(updatedTarget);
        return new ResponseEntity<>(roleConverter.toDto(updatedRole), HttpStatus.OK);
    }

    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasAnyAuthority('can_delet_role_by_id')")
    public ResponseEntity<Object> deleteRole(@PathVariable("roleName") String roleName) {
        roleService.deleteByRoleName(roleName);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
