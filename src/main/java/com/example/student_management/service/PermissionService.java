package com.example.student_management.service;

import com.example.student_management.domain.Permission;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.PermissionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {

        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        }
        return permissionOptional.get();
    }

    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    public void deleteById(Long id) {

        try {
            permissionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        }
    }


}
