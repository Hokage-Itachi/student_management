package com.example.student_management.controller;

import com.example.student_management.converter.RoleConverter;
import com.example.student_management.domain.Role;
import com.example.student_management.dto.RoleDto;
import com.example.student_management.service.RoleService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: testing api
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;
    private final RoleConverter roleConverter;

    public RoleController(RoleService roleService, RoleConverter roleConverter) {
        this.roleService = roleService;
        this.roleConverter = roleConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_roles')")
    public ResponseEntity<Object> getAllRole() {
        List<Role> roles = roleService.findAll();
        List<RoleDto> roleDtoList = roles.stream().map(roleConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(roleDtoList, HttpStatus.OK);
    }

    @GetMapping("/{roleName}")
    @PreAuthorize("hasAnyAuthority('can_view_role_by_id')")
    public ResponseEntity<Object> getRoleByName(@PathVariable("roleName") String roleName) {
        Role role = roleService.findByRoleName(roleName);

        return new ResponseEntity<>(roleConverter.toDto(role), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_role')")
    public ResponseEntity<Object> addRole(@RequestBody RoleDto roleDto) {
        // TODO: handle duplicate role name
        Role role = roleConverter.toEntity(roleDto);
        Role insertedRole = roleService.save(role);
        return new ResponseEntity<>(roleConverter.toDto(insertedRole), HttpStatus.CREATED);
    }

    @PutMapping("/{roleName}")
    @PreAuthorize("hasAnyAuthority('can_update_role')")
    public ResponseEntity<Object> getRoleByName(@PathVariable("roleName") String roleName, @RequestBody RoleDto roleDto) {
        Role existRole = roleService.findByRoleName(roleName);
        // TODO: check role duplicate error
        Role role = roleConverter.toEntity(roleDto);
        Role updatedRole = roleService.save(role);
        return new ResponseEntity<>(roleConverter.toDto(updatedRole), HttpStatus.CREATED);
    }

    @DeleteMapping("{roleName}")
    @PreAuthorize("hasAnyAuthority('can_delet_role_by_id')")
    public ResponseEntity<Object> deleteRole(@PathVariable("roleName") String roleName) {
        roleService.deleteByRoleName(roleName);

        return new ResponseEntity<>(HttpStatus.OK);


    }

}
