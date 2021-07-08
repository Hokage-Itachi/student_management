package com.example.student_management.controller;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.User;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.service.PermissionService;
import com.example.student_management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-authorization")
public class UserAuthorizationController {
    private final UserService userService;
    private final PermissionService permissionService;

    public UserAuthorizationController(UserService userService, PermissionService permissionService) {
        this.userService = userService;
        this.permissionService = permissionService;
    }

    @PostMapping("/{userId}/{permissionId}")
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

    @DeleteMapping("/{userId}/{permissionId}")
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
