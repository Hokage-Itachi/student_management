package com.example.student_management.controller;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.Role;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.service.PermissionService;
import com.example.student_management.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role-authorization")
@Slf4j
public class RoleAuthorizationController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleAuthorizationController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }
    @PreAuthorize("hasAnyAuthority('can_authorize_role')")
    @PostMapping("/{roleName}/{permissionId}")
    public ResponseEntity<Object> authorizeRole(@PathVariable("roleName") String roleName, @PathVariable("permissionId") Long permissionId) {
        Role role = roleService.findByRoleName(roleName);
        Permission permission = permissionService.findById(permissionId);
        if (role.getPermissions().contains(permission)) {
            throw new ResourceConflictException(String.format(ExceptionMessage.ROLE_AUTHORIZATION_CONFLICT.message, roleName, permissionId));
        }
        role.getPermissions().add(permission);
        roleService.update(role);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('can_unauthorize_role')")
    @DeleteMapping("/{roleName}/{permissionId}")
    public ResponseEntity<Object> unAuthorizeRole(@PathVariable("roleName") String roleName, @PathVariable("permissionId") Long permissionId) {
        Role role = roleService.findByRoleName(roleName);
        Permission permission = permissionService.findById(permissionId);
        if (!role.getPermissions().contains(permission)) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.ROLE_PERMISSION_NOT_FOUND.message, roleName, permissionId));
        }
        role.getPermissions().remove(permission);
        roleService.update(role);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
