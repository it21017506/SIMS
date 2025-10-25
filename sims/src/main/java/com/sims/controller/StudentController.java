package com.sims.controller;

import com.sims.model.Student;
import com.sims.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService service;

    @GetMapping("/enroll")
    public String showEnrollForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("message", null);
        return "enroll";
    }

    @PostMapping("/enroll")
    public String enroll(@Valid @ModelAttribute Student student, BindingResult bindingResult, Model model) {
        logger.info("Received student for enrollment: {}", student);
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation errors occurred.");
            return "enroll";
        }
        try {
            service.enrollStudent(student);
            model.addAttribute("message", "Student enrolled successfully!");
            model.addAttribute("student", new Student());
        } catch (Exception e) {
            model.addAttribute("message", "Error during enrollment: " + e.getMessage());
            logger.error("Enrollment failed: {}", e.getMessage(), e);
        }
        return "enroll";
    }

    @GetMapping("/list")
    public String showAllStudents(Model model) {
        model.addAttribute("students", service.getAllStudents());
        return "students";
    }

    @GetMapping("/edit/{id}")
    public String showEditStudentForm(@PathVariable String id, Model model) {
        try {
            Student student = service.getStudentById(id);
            if (student == null) {
                model.addAttribute("message", "Student not found!");
                model.addAttribute("students", service.getAllStudents());
                return "students";
            }
            model.addAttribute("student", student);
            model.addAttribute("message", null);
            return "edit-student";
        } catch (Exception e) {
            model.addAttribute("message", "Error retrieving student: " + e.getMessage());
            logger.error("Error retrieving student with ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("students", service.getAllStudents());
            return "students";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateStudent(@PathVariable String id, @Valid @ModelAttribute Student student, BindingResult bindingResult, Model model) {
        logger.info("Received student for update: {}", student);
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation errors occurred.");
            return "edit-student";
        }
        try {
            student.setId(id);
            service.updateStudent(student);
            model.addAttribute("message", "Student updated successfully!");
            model.addAttribute("students", service.getAllStudents());
            return "students";
        } catch (Exception e) {
            model.addAttribute("message", "Error during update: " + e.getMessage());
            logger.error("Update failed for student ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("student", student);
            return "edit-student";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteStudent(@PathVariable String id, Model model) {
        logger.info("Attempting to delete student with ID: {}", id);
        try {
            boolean deleted = service.deleteStudent(id);
            if (deleted) {
                model.addAttribute("message", "Student deleted successfully!");
            } else {
                model.addAttribute("message", "Student not found!");
            }
        } catch (Exception e) {
            model.addAttribute("message", "Error during deletion: " + e.getMessage());
            logger.error("Deletion failed for student ID {}: {}", id, e.getMessage(), e);
        }
        model.addAttribute("students", service.getAllStudents());
        return "students";
    }

    @PostMapping("/{studentId}/enroll-schedule/{scheduleId}")
    public String enrollInSchedule(@PathVariable String studentId, @PathVariable String scheduleId, Model model) {
        try {
            service.enrollInSchedule(studentId, scheduleId);
            model.addAttribute("message", "Student enrolled in schedule successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error during enrollment: " + e.getMessage());
            logger.error("Enrollment in schedule failed: {}", e.getMessage(), e);
        }
        model.addAttribute("students", service.getAllStudents());
        return "students";
    }
}