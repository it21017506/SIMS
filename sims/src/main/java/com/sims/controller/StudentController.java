package com.sims.controller;

import com.sims.model.Student;
import com.sims.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String enroll(@ModelAttribute Student student, Model model) {
        logger.info("Received student for enrollment: {}", student);
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
}