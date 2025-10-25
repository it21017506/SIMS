package com.sims.service;

import com.sims.model.ClassSchedule;
import com.sims.model.Student;
import com.sims.repository.ClassScheduleRepository;
import com.sims.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassScheduleRepository classScheduleRepository; // For relations

    @Transactional
    public void enrollStudent(Student student) {
        logger.info("Attempting to save student: {}", student);
        try {
            // Auto-set enrollment date if not provided
            if (student.getEnrollmentDate() == null) {
                student.setEnrollmentDate(LocalDate.now());
            }
            // Check for duplicate email
            if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
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

    @Transactional
    public void updateStudent(Student student) {
        logger.info("Attempting to update student: {}", student);
        try {
            if (studentRepository.existsById(student.getId())) {
                // Check for email uniqueness if changed
                Optional<Student> existing = studentRepository.findByEmail(student.getEmail());
                if (existing.isPresent() && !existing.get().getId().equals(student.getId())) {
                    throw new IllegalArgumentException("Email already exists");
                }
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

    @Transactional
    public boolean deleteStudent(String id) {
        logger.info("Attempting to delete student with ID: {}", id);
        try {
            Optional<Student> studentOpt = studentRepository.findById(id);
            if (studentOpt.isPresent()) {
                Student student = studentOpt.get();
                // Remove from schedules
                for (String scheduleId : student.getScheduleIds()) {
                    Optional<ClassSchedule> scheduleOpt = classScheduleRepository.findById(scheduleId);
                    scheduleOpt.ifPresent(schedule -> {
                        schedule.getStudentIds().remove(id);
                        classScheduleRepository.save(schedule);
                    });
                }
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

    @Transactional
    public void enrollInSchedule(String studentId, String scheduleId) {
        logger.info("Attempting to enroll student {} in schedule {}", studentId, scheduleId);
        try {
            Optional<Student> studentOpt = studentRepository.findById(studentId);
            Optional<ClassSchedule> scheduleOpt = classScheduleRepository.findById(scheduleId);
            if (studentOpt.isPresent() && scheduleOpt.isPresent()) {
                Student student = studentOpt.get();
                ClassSchedule schedule = scheduleOpt.get();
                if (!student.getScheduleIds().contains(scheduleId)) {
                    student.getScheduleIds().add(scheduleId);
                    schedule.getStudentIds().add(studentId);
                    studentRepository.save(student);
                    classScheduleRepository.save(schedule);
                    logger.info("Enrollment successful");
                } else {
                    throw new IllegalArgumentException("Student already enrolled in this schedule");
                }
            } else {
                throw new IllegalArgumentException("Student or schedule not found");
            }
        } catch (Exception e) {
            logger.error("Failed to enroll in schedule: {}", e.getMessage(), e);
            throw e;
        }
    }
}