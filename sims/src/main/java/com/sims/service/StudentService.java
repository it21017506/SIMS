package com.sims.service;

import com.sims.model.Student;
import com.sims.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository studentRepository;

    public void enrollStudent(Student student) {
        logger.info("Attempting to save student: {}", student);
        try {
            studentRepository.save(student);
            logger.info("Student saved successfully with ID: {}", student.getId());
        } catch (Exception e) {
            logger.error("Failed to save student: {}", e.getMessage(), e);
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        logger.info("Retrieved {} students", students.size());
        return students;
    }
}