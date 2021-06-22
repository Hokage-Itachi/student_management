package com.example.student_management.controller;

import com.example.student_management.converter.PermistionConverter;
import com.example.student_management.domain.Permission;
import com.example.student_management.dto.PermistionDto;
import com.example.student_management.service.PermistionService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permistions")
public class PermistionController {
    private final PermistionService permistionService;
    private final PermistionConverter permistionConverter;

    public PermistionController(PermistionService permistionService, PermistionConverter permistionConverter) {
        this.permistionService = permistionService;
        this.permistionConverter = permistionConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_permissions')")
    public ResponseEntity<Object> getAllPermistion() {
        List<Permission> permissions = permistionService.findAll();
        List<PermistionDto> permistionDtoList = permissions.stream().map(permistionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(permistionDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_permission_by_id')")
    public ResponseEntity<Object> getPermistionById(@PathVariable("id") Long id) {
        Optional<Permission> permistionOptional = permistionService.findById(id);
        if (permistionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(permistionConverter.toDto(permistionOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_permission')")
    public ResponseEntity<Object> addPermistion(@RequestBody PermistionDto permistionDto) {
        Permission permission = permistionConverter.toEntity(permistionDto);
        Permission insertedPermission = permistionService.save(permission);
        return new ResponseEntity<>(permistionConverter.toDto(insertedPermission), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_permission')")
    public ResponseEntity<Object> updatePermistion(@PathVariable("id") Long id, @RequestBody PermistionDto permistionDto) {
        Optional<Permission> permistionOptional = permistionService.findById(id);
        if (permistionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Permission permission = permistionConverter.toEntity(permistionDto);
        permission.setId(id);
        Permission updatedPermission = permistionService.save(permission);
        return new ResponseEntity<>(permistionConverter.toDto(updatedPermission), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_permission_by_id')")
    public ResponseEntity<Object> deletePermistion(@PathVariable("id") Long id) {
        try {
            permistionService.deleteById(id);
            ;
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
