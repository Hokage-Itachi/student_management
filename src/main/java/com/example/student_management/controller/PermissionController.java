package com.example.student_management.controller;

import com.example.student_management.converter.PermissionConverter;
import com.example.student_management.domain.Permission;
import com.example.student_management.dto.PermissionDto;
import com.example.student_management.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionService permissionService;
    private final PermissionConverter permissionConverter;

    public PermissionController(PermissionService permissionService, PermissionConverter permissionConverter) {
        this.permissionService = permissionService;
        this.permissionConverter = permissionConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_permissions')")
    public ResponseEntity<Object> getAllPermistion() {
        List<Permission> permissions = permissionService.findAll();
        List<PermissionDto> permissionDtoList = permissions.stream().map(permissionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(permissionDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_permission_by_id', 'can_view_all_permissions')")
    public ResponseEntity<Object> getPermistionById(@PathVariable("id") Long id) {
        Permission permission = permissionService.findById(id);

        return new ResponseEntity<>(permissionConverter.toDto(permission), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_permission')")
    public ResponseEntity<Object> addPermistion(@RequestBody PermissionDto permissionDto) {
        Permission permission = permissionConverter.toEntity(permissionDto);
        Permission insertedPermission = permissionService.save(permission);
        return new ResponseEntity<>(permissionConverter.toDto(insertedPermission), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_permission')")
    public ResponseEntity<Object> updatePermistion(@PathVariable("id") Long id, @RequestBody PermissionDto permissionDto) {
        Permission existPermission = permissionService.findById(id);

        Permission permission = permissionConverter.toEntity(permissionDto);
        permission.setId(existPermission.getId());
        Permission updatedPermission = permissionService.save(permission);
        return new ResponseEntity<>(permissionConverter.toDto(updatedPermission), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_permission_by_id')")
    public ResponseEntity<Object> deletePermistion(@PathVariable("id") Long id) {
        permissionService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
