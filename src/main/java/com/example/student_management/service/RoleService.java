package com.example.student_management.service;

import com.example.student_management.domain.Role;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.RoleRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findByRoleName(String roleName) {
        if (roleName == null){
            throw new DataInvalidException(String.format(ExceptionMessage.ID_INVALID.message, "Role"));
        }
        Optional<Role> roleOptional = roleRepository.findByRoleName(roleName);
        if (roleOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.ROLE_NOT_FOUND.toString(), roleName));
        }
        return roleOptional.get();
    }

    public Role add(Role role) {
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.ROLE_NAME_INVALID.message);
        }
        if (roleRepository.findByRoleName(role.getRoleName()).isPresent()) {
            throw new ResourceConflictException(String.format(ExceptionMessage.ROLE_NAME_CONFLICT.message, role.getRoleName()));
        }
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        if (role.getRoleName() == null || role.getRoleName().isBlank()) {
            throw new DataInvalidException(ExceptionMessage.ROLE_NAME_INVALID.message);
        }
        if (roleRepository.findByRoleName(role.getRoleName()).isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.ROLE_NOT_FOUND.message, role.getRoleName()));
        }
        return roleRepository.save(role);
    }

    public void deleteByRoleName(String roleName) {
        try {
            roleRepository.deleteById(roleName);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.ROLE_NOT_FOUND.toString(), roleName));
        }
    }
}
