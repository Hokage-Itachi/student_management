package com.example.student_management.service;

import com.example.student_management.domain.Teacher;
import com.example.student_management.repository.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Optional<Teacher> findById(Long id){
        return teacherRepository.findById(id);
    }
}
