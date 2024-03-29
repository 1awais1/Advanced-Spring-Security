package com.example.springsecurity.demo.Student;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static  final List<Student> STUDENTS= Arrays.asList(
            new Student(1,"JamesBond"),
            new Student(2,"Faizan"),
            new Student(3,"Jamila")

    );
    @GetMapping(path="{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return  STUDENTS.stream().filter(x->studentId.equals(x.getStudentId())).findFirst()
                .orElseThrow(()-> new IllegalStateException("Student"+studentId+"does not exist"));

    }
}
