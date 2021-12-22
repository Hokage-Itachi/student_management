package com.example.student_management.controller;

import com.example.student_management.converter.RegistrationConverter;
import com.example.student_management.domain.Class;
import com.example.student_management.domain.Registration;
import com.example.student_management.domain.RegistrationId;
import com.example.student_management.domain.Student;
import com.example.student_management.dto.RegistrationDto;
import com.example.student_management.exception.DataInvalidException;
import com.example.student_management.service.ClassService;
import com.example.student_management.service.RegistrationService;
import com.example.student_management.service.StudentService;
import com.example.student_management.specification.CustomSpecificationBuilder;
import com.example.student_management.utils.ServiceUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/registrations")
@Slf4j
@ApiResponses(value = {
        @ApiResponse(responseCode = "401", ref = "unauthorized"),
        @ApiResponse(responseCode = "405", ref = "methodNotAllowed"),
        @ApiResponse(responseCode = "404", ref = "resourceNotFound"),
        @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
})
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
    @Operation(summary = "Get list registration")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> getAllRegistration(
            @RequestParam(name = "filter", required = false) String[] filter,
            @RequestParam(name = "sort", required = false, defaultValue = "id:asc") String[] sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "5") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size, ServiceUtils.getSortParam(sort));
        Specification<Registration> specification = new CustomSpecificationBuilder<Registration>(ServiceUtils.getFilterParam(filter, Registration.class)).build();
        List<Registration> registrations = registrationService.findAll(specification, pageable);
        List<RegistrationDto> registrationDtoList = registrations.stream().map(registrationConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(registrationDtoList, HttpStatus.OK);
    }

    @GetMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_view_registration_by_id', 'can_view_all_registrations')")
    @Operation(summary = "Get registration by class id and student id")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationDto.class)))
    public ResponseEntity<Object> getRegistrationById(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        Registration registration = registrationService.findById(id);

        return new ResponseEntity<>(registrationConverter.toDto(registration), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('can_add_registration')")
    @Operation(summary = "Create registration")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationDto.class)))
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> addRegistration(@RequestBody RegistrationDto registrationDto) {
        if (registrationDto.getId() == null) {
            throw new DataInvalidException("Registration id must not be null");
        }
        Registration registration = registrationConverter.toEntity(registrationDto);
        Student student = studentService.findById(registrationDto.getId().getStudentId());
        Class clazz = classService.findById(registrationDto.getId().getClassId());
        registration.setStudent(student);
        registration.setClazz(clazz);
        Registration insertedRegistration = registrationService.add(registration);
        return new ResponseEntity<>(registrationConverter.toDto(insertedRegistration), HttpStatus.CREATED);
    }

    @PutMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_update_registration')")
    @Operation(summary = "Update registration")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrationDto.class)))
    @ApiResponse(responseCode = "409", description = "Resource conflict", content = @Content)
    public ResponseEntity<Object> updateRegistration(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId, @RequestBody RegistrationDto registrationDto) {
        RegistrationId id = new RegistrationId(studentId, classId);
        Registration updatedTarget = registrationService.findById(id);
        Registration updatedSource = registrationConverter.toEntity(registrationDto);
        updatedSource.setId(id);
        Student student = studentService.findById(studentId);
        Class clazz = classService.findById(classId);
        updatedSource.setClazz(clazz);
        updatedSource.setStudent(student);
        // Copy non-null properties from source to target
        BeanUtils.copyProperties(updatedSource, updatedTarget, ServiceUtils.getNullPropertyNames(updatedSource));
        Registration updatedRegistration = registrationService.update(updatedTarget);
        return new ResponseEntity<>(registrationConverter.toDto(updatedRegistration), HttpStatus.OK);
    }

    @DeleteMapping("/{classId}/{studentId}")
    @PreAuthorize("hasAnyAuthority('can_update_registration')")
    @Operation(summary = "Delete registration")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    public ResponseEntity<Object> deleteRegistration(@PathVariable("classId") Long classId, @PathVariable("studentId") Long studentId) {
        RegistrationId id = new RegistrationId(studentId, classId);
        registrationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
