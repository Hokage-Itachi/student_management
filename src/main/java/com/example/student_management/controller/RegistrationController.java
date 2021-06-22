package com.example.student_management.controller;

import com.example.student_management.converter.RegistrationConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.RegistrationDto;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.RegistrationService;
import com.example.student_management.service.StudentService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final RegistrationConverter registrationConverter;
    private final StudentService studentService;
    private final ClassService classService;

    public RegistrationController(RegistrationService registrationService, RegistrationConverter registrationConverter, StudentService studentService, ClassService classService) {
        this.registrationService = registrationService;
        this.registrationConverter = registrationConverter;
        this.studentService = studentService;
        this.classService = classService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('can_view_all_registrations')")
    public ResponseEntity<Object> getAllRegistration() {
        List<Registration> registrations = registrationService.findAll();
        List<RegistrationDto> registrationDtoList = registrations.stream().map(registrationConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(registrationDtoList, HttpStatus.OK);
    }

    @GetMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_view_registration_by_id')")
    public ResponseEntity<Object> getRegistrationById(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        Optional<Registration> registrationOptional = registrationService.findById(id);
        if (registrationOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(registrationConverter.toDto(registrationOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_registration')")
    public ResponseEntity<Object> addRegistration(@RequestBody RegistrationDto registrationDto) {
        Optional<Student> studentOptional = studentService.findById(registrationDto.getId().getStudentId());
        Optional<Class> classOptional = classService.findById(registrationDto.getId().getClassId());
        if (studentOptional.isEmpty() || classOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Registration registration = registrationConverter.toEntity(registrationDto);
        registration.setClazz(classOptional.get());
        registration.setStudent(studentOptional.get());
        Registration insertedRegistration = registrationService.save(registration);
        return new ResponseEntity<>(registrationConverter.toDto(insertedRegistration), HttpStatus.CREATED);
    }

    @DeleteMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_update_registration')")
    public ResponseEntity<Object> deleteRegistration(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        try {
            registrationService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
