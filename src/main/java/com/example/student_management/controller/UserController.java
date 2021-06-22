package com.example.student_management.controller;

import com.example.student_management.converter.UserConverter;
import com.example.student_management.domain.Role;
import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import com.example.student_management.service.RoleService;
import com.example.student_management.service.UserService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final RoleService roleService;


    public UserController(UserService userService, UserConverter userConverter, RoleService roleService) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_users')")
    public ResponseEntity<Object> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDto> userDtoList = users.stream().map(userConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(userDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_all_users', 'can_view_user_by_id')")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userConverter.toDto(userOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_user')")
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) {
        Optional<Role> roleOptional = roleService.findByRoleName(userDto.getRole());
        if (roleOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userConverter.toEntity(userDto);
        user.setRole(roleOptional.get());
        User insertedUser = userService.save(user);
        return new ResponseEntity<>(userConverter.toDto(insertedUser), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_user')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<Role> roleOptional = roleService.findByRoleName(userDto.getRole());
        if (roleOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userConverter.toEntity(userDto);

        if (!userOptional.get().getEmail().equals(user.getEmail()) && userService.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        user.setId(id);
        user.setRole(roleOptional.get());
        User updatedUser = userService.save(user);
        return new ResponseEntity<>(userConverter.toDto(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_user_by_id')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        try {
            userService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
