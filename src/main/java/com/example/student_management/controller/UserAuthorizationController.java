package com.example.student_management.controller;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.User;
import com.example.student_management.enums.ExceptionMessage;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.service.PermissionService;
import com.example.student_management.service.UserService;
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
@RequestMapping("/api/user-authorization")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class UserAuthorizationController {
    private final UserService userService;
    private final PermissionService permissionService;

    public UserAuthorizationController(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAnyAuthority('can_authorize_user')")
    @PostMapping("/{userId}/{permissionId}")
    @Operation(summary = "Authorized user")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content)
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> authorizeUser(@PathVariable("userId") Long userId, @PathVariable("permissionId") Long permissionId) {
        User user = userService.findById(userId);
        Permission permission = permissionService.findById(permissionId);
        if (user.getPermissions().contains(permission)) {
            throw new ResourceConflictException(String.format(ExceptionMessage.USER_AUTHORIZATION_CONFLICT.message, userId, permissionId));
        }
        user.getPermissions().add(permission);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PreAuthorize("hasAnyAuthority('can_unauthorize_user')")
    @DeleteMapping("/{userId}/{permissionId}")
    @Operation(summary = "Unauthorized user")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> unAuthorizeUser(@PathVariable("userId") Long userId, @PathVariable("permissionId") Long permissionId) {
        User user = userService.findById(userId);
        Permission permission = permissionService.findById(permissionId);
        if (!user.getPermissions().contains(permission)) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.USER_PERMISSION_NOT_FOUND.message, userId, permissionId));
        }
        user.getPermissions().remove(permission);
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
