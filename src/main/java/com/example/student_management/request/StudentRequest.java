package com.example.student_management.request;

import com.example.student_management.dto.ClassId;
import com.example.student_management.dto.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {
    private StudentDto student;
    private List<ClassId> classes;
}
