package com.example.student_management.service;

import com.example.student_management.domain.Permission;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.PermissionRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Cacheable("permission")
    public Permission findById(Long id) {
        if (id == null){
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Permission"));
        }
        Optional<Permission> permissionOptional = permissionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        }
        return permissionOptional.get();
    }
    @CachePut(value = "permission")
    public Permission save(Permission permission) {
        if (permission.getPerName() == null || permission.getPerName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.PERMISSION_NAME_INVALID.message);
        }
        return permissionRepository.save(permission);
    }
    @CacheEvict(value = "permission")
    public void deleteById(Long id) {

        try {
            permissionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.PERMISSION_FOREIGN_KEY.toString(), id));
        }
    }


}
