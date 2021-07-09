package com.example.student_management.controller;

import com.example.student_management.converter.UserConverter;
import com.example.student_management.domain.Role;
import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import com.example.student_management.service.RoleService;
import com.example.student_management.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, UserConverter userConverter, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
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
        User user = userService.findById(id);

        return new ResponseEntity<>(userConverter.toDto(user), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_user')")
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) {
        Role role = roleService.findByRoleName(userDto.getRole());

        User user = userConverter.toEntity(userDto);
        user.setId(null);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(role);
        User insertedUser = userService.save(user);
        return new ResponseEntity<>(userConverter.toDto(insertedUser), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_user')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        userService.findById(id);

        Role role = roleService.findByRoleName(userDto.getRole());

        User user = userConverter.toEntity(userDto);
        user.setId(id);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User updatedUser = userService.save(user);
        return new ResponseEntity<>(userConverter.toDto(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_user_by_id')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
