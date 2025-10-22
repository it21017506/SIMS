package com.sims.service;

import com.sims.model.Student;
import com.sims.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            throw e;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        logger.info("Retrieved {} students", students.size());
        return students;
    }

    public Student getStudentById(String id) {
        logger.info("Retrieving student with ID: {}", id);
        Optional<Student> student = studentRepository.findById(id);
        if (student.isPresent()) {
            logger.info("Student found with ID: {}", id);
            return student.get();
        } else {
            logger.warn("Student not found with ID: {}", id);
            return null;
        }
    }

    public void updateStudent(Student student) {
        logger.info("Attempting to update student: {}", student);
        try {
            if (studentRepository.existsById(student.getId())) {
                studentRepository.save(student);
                logger.info("Student updated successfully with ID: {}", student.getId());
            } else {
                logger.error("Student with ID {} does not exist", student.getId());
                throw new IllegalArgumentException("Student not found");
            }
        } catch (Exception e) {
            logger.error("Failed to update student: {}", e.getMessage(), e);
            throw e;
        }
    }

    public boolean deleteStudent(String id) {
        logger.info("Attempting to delete student with ID: {}", id);
        try {
            if (studentRepository.existsById(id)) {
                studentRepository.deleteById(id);
                logger.info("Student deleted successfully with ID: {}", id);
                return true;
            } else {
                logger.warn("Student with ID {} does not exist", id);
                return false;
            }
        } catch (Exception e) {
            logger.error("Failed to delete student: {}", e.getMessage(), e);
            throw e;
        }
    }
}