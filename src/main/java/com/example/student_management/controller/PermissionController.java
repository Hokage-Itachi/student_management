package com.example.student_management.controller;

import com.example.student_management.converter.PermissionConverter;
import com.example.student_management.domain.Permission;
import com.example.student_management.dto.ClassDto;
import com.example.student_management.dto.PermissionDto;
import com.example.student_management.service.PermissionService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permissions")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
public class PermissionController {
    private final PermissionService permissionService;
    private final PermissionConverter permissionConverter;

    public PermissionController(PermissionService permissionService, PermissionConverter permissionConverter) {
        this.permissionService = permissionService;
        this.permissionConverter = permissionConverter;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_permissions')")
    @Operation(summary = "Get list permission")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllPermission(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Permission> specification = new CustomSpecificationBuilder<Permission>(ServiceUtils.getFilterParam(filter, Permission.class)).build();
        List<Permission> permissions = permissionService.findAll(specification, pageable);
        List<PermissionDto> permissionDtoList = permissions.stream().map(permissionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(permissionDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_view_permission_by_id', 'can_view_all_permissions')")
    @Operation(summary = "Get permission by id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDto.class)))
    public ResponseEntity<Object> getPermissionById(@PathVariable("id") Long id) {
        Permission permission = permissionService.findById(id);

        return new ResponseEntity<>(permissionConverter.toDto(permission), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_permission')")
    @Operation(summary = "Create permission")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDto.class)))
    public ResponseEntity<Object> addPermission(@RequestBody PermissionDto permissionDto) {
        Permission permission = permissionConverter.toEntity(permissionDto);
        permission.setId(null);
        Permission insertedPermission = permissionService.save(permission);
        return new ResponseEntity<>(permissionConverter.toDto(insertedPermission), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_update_permission')")
    @Operation(summary = "Update permission")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionDto.class)))
    public ResponseEntity<Object> updatePermission(@PathVariable("id") Long id, @RequestBody PermissionDto permissionDto) {
        Permission updatedTarget = permissionService.findById(id);
        Permission updatedSource = permissionConverter.toEntity(permissionDto);
        updatedSource.setId(id);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Permission updatedPermission = permissionService.save(updatedTarget);
        return new ResponseEntity<>(permissionConverter.toDto(updatedPermission), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('can_delete_permission_by_id')")
    @Operation(summary = "Delete permission")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deletePermission(@PathVariable("id") Long id) {
        permissionService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
