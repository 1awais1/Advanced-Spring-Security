package com.example.springsecurity.demo.Student;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/management/api/v1/students")
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1, "JamesBond"),
            new Student(2, "Faizan"),
            new Student(3, "Jamila")

    );

    @GetMapping
  // @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMIN_TRAINEE')")
    public List<Student> getAllStudents() {
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        System.out.println(student);
        System.out.println("New student registered");

    }

    @DeleteMapping(path = "{studentId}")
    //@PreAuthorize("hasAuthority('student:write')")

    public void deleteStudent(@PathVariable("studentId") Integer studentId) {

        System.out.println(studentId);
    }

    @PutMapping(path = "{studentId}")
    //@PreAuthorize("hasAuthority('student:write')")

    public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student student) {
        System.out.println(String.format("%s %s", student, studentId));
    }
}
