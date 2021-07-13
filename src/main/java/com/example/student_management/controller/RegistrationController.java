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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    @PreAuthorize("hasAnyAuthority('can_view_registration_by_id', 'can_view_all_registrations')")
    public ResponseEntity<Object> getRegistrationById(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        Registration registration = registrationService.findById(id);

        return new ResponseEntity<>(registrationConverter.toDto(registration), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_registration')")
    public ResponseEntity<Object> addRegistration(@RequestBody RegistrationDto registrationDto) {
        Registration registration = registrationConverter.toEntity(registrationDto);
        Registration insertedRegistration = registrationService.add(registration);
        return new ResponseEntity<>(registrationConverter.toDto(insertedRegistration), HttpStatus.CREATED);
    }

    @PutMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_update_registration')")
    public ResponseEntity<Object> updateRegistration(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId, @RequestBody RegistrationDto registrationDto) {

        Registration registration = registrationConverter.toEntity(registrationDto);
        registration.setId(new RegistrationId(studentId, classId));

        Registration updatedRegistration = registrationService.update(registration);
        return new ResponseEntity<>(registrationConverter.toDto(updatedRegistration), HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_update_registration')")
    public ResponseEntity<Object> deleteRegistration(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        registrationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
