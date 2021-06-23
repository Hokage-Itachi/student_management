package com.example.student_management.service;

import com.example.student_management.domain.Permission;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.PermistionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermistionService {
    private final PermistionRepository permistionRepository;

    public PermistionService(PermistionRepository permistionRepository) {
        this.permistionRepository = permistionRepository;
    }

    public List<Permission> findAll() {
        return permistionRepository.findAll();
    }

    public Permission findById(Long id) {

        Optional<Permission> permissionOptional = permistionRepository.findById(id);
        if (permissionOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        }
        return permissionOptional.get();
    }

    public Permission save(Permission permission) {
        return permistionRepository.save(permission);
    }

    public void deleteById(Long id) {

        try {
            permistionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.PERMISSION_NOT_FOUND.toString(), id));
        }
    }


}
