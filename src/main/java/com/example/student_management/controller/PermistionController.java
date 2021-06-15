package com.example.student_management.controller;

import com.example.student_management.converter.PermistionConverter;
import com.example.student_management.domain.Permistion;
import com.example.student_management.dto.PermistionDto;
import com.example.student_management.service.PermistionService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permistions")
public class PermistionController {
    private final PermistionService permistionService;
    private final PermistionConverter permistionConverter;

    public PermistionController(PermistionService permistionService, PermistionConverter permistionConverter) {
        this.permistionService = permistionService;
        this.permistionConverter = permistionConverter;
    }

    @GetMapping
    public ResponseEntity<Object> getAllPermistion() {
        List<Permistion> permistions = permistionService.findAll();
        List<PermistionDto> permistionDtoList = permistions.stream().map(permistionConverter::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(permistionDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getPermistionById(@PathVariable("id") Long id) {
        Optional<Permistion> permistionOptional = permistionService.findById(id);
        if (permistionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(permistionConverter.toDto(permistionOptional.get()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addPermistion(@RequestBody PermistionDto permistionDto) {
        Permistion permistion = permistionConverter.toEntity(permistionDto);
        Permistion insertedPermistion = permistionService.save(permistion);
        return new ResponseEntity<>(permistionConverter.toDto(insertedPermistion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePermistion(@PathVariable("id") Long id, @RequestBody PermistionDto permistionDto) {
        Optional<Permistion> permistionOptional = permistionService.findById(id);
        if (permistionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Permistion permistion = permistionConverter.toEntity(permistionDto);
        permistion.setId(id);
        Permistion updatedPermistion = permistionService.save(permistion);
        return new ResponseEntity<>(permistionConverter.toDto(updatedPermistion), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePermistion(@PathVariable("id") Long id) {
        try {
            permistionService.deleteById(id);
            ;
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
