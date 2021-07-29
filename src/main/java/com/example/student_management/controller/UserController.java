package com.example.student_management.controller;

import com.example.student_management.converter.UserConverter;
import com.example.student_management.domain.User;
import com.example.student_management.dto.UserDto;
import com.example.student_management.service.UserService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;


    public UserController(UserService userService, UserConverter userConverter, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_users')")
    public ResponseEntity<Object> getAllUsers(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<User> specification = new CustomSpecificationBuilder<User>(ServiceUtils.getFilterParam(filter, User.class)).build();
        List<User> users = userService.findAll(specification, pageable);
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
        User user = userConverter.toEntity(userDto);
        user.setId(null);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User insertedUser = userService.save(user);
        return new ResponseEntity<>(userConverter.toDto(insertedUser), HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_user')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        User updatedTarget = userService.findById(id);
        User updatedSource = userConverter.toEntity(userDto);
        updatedSource.setId(id);
        if (userDto.getPassword() != null) {
            updatedSource.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        User updatedUser = userService.save(updatedTarget);
        return new ResponseEntity<>(userConverter.toDto(updatedUser), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_user_by_id')")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
