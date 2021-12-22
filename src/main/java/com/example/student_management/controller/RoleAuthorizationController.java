package com.example.student_management.controller;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.Role;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.service.PermissionService;
import com.example.student_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role-authorization")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class RoleAuthorizationController {
    private final RoleService roleService;
    private final PermissionService permissionService;

    public RoleAuthorizationController(RoleService roleService, PermissionService permissionService) {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAnyAuthority('can_authorize_role')")
    @PostMapping("/{roleName}/{permissionId}")
    @Operation(summary = "Authorized role")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content)
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
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
    @Operation(summary = "Unauthorized role")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
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
