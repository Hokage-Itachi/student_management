package com.example.student_management.service;

import com.example.student_management.domain.Teacher;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.exception.ForeignKeyException;
import com.example.student_management.exception.ResourceConflictException;
import com.example.student_management.exception.ResourceNotFoundException;
import com.example.student_management.message.ExceptionMessage;
import com.example.student_management.repository.TeacherRepository;
import com.example.student_management.utils.ServiceUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> findAll() {
        return teacherRepository.findAll();
    }

    public Teacher findById(Long id) {

        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.TEACHER_NOT_FOUND_BY_ID.toString(), id));
        }
        return teacherOptional.get();
    }

    public Teacher save(Teacher teacher) {
        if (!ServiceUtils.isStringValid(teacher.getFullName(), "[^0-9]+")) {
            throw new DataInvalidException(ExceptionMessage.TEACHER_NAME_INVALID.message);
        }
        if (!ServiceUtils.isStringValid(teacher.getEmail(), "^(.+)@(.+)$")) {
            throw new DataInvalidException(ExceptionMessage.TEACHER_EMAIL_INVALID.message);
        }
        try {
            return teacherRepository.save(teacher);
        } catch (DataIntegrityViolationException e) {
            SQLException ex = (SQLException) e.getRootCause();
            String message = ServiceUtils.messageFormat(ex.getMessage());
            throw new ResourceConflictException(message);
        }
    }

    public void deleteById(Long id) {

        try {
            teacherRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.TEACHER_NOT_FOUND_BY_ID.toString(), id));
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyException(String.format(ExceptionMessage.TEACHER_FOREIGN_KEY_EXCEPTION_MESSAGE.toString(), id));
        }
    }

    public Teacher findByEmail(String email) {
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);
        if (teacherOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(ExceptionMessage.TEACHER_NOT_FOUND_BY_EMAIL.toString(), email));
        }
        return teacherOptional.get();
    }
}
