package com.example.student_management.service;

import com.example.student_management.domain.Role;
import com.example.student_management.repository.RoleRepository;
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

    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void deleteByRoleName(String roleName) {
        roleRepository.deleteById(roleName);
    }
}
