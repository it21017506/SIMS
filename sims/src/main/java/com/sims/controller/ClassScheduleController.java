package com.sims.controller;

import com.sims.model.ClassSchedule;
import com.sims.model.Student;
import com.sims.service.ClassScheduleService;
import com.sims.service.StudentService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ClassScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

    @Autowired
    private ClassScheduleService service;

    @Autowired
    private StudentService studentService;

    @GetMapping("/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("classSchedule", new ClassSchedule());
        model.addAttribute("message", null);
        return "schedule";
    }

    @PostMapping("/add")
    public String addSchedule(@Valid @ModelAttribute ClassSchedule classSchedule, BindingResult bindingResult, Model model) {
        logger.info("Received class schedule for addition: {}", classSchedule);
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation errors occurred.");
            return "schedule";
        }
        try {
            service.addClassSchedule(classSchedule);
            model.addAttribute("message", "Class schedule added successfully!");
            model.addAttribute("classSchedule", new ClassSchedule());
        } catch (Exception e) {
            model.addAttribute("message", "Error during addition: " + e.getMessage());
            logger.error("Addition failed: {}", e.getMessage(), e);
        }
        return "schedule";
    }

    @GetMapping("/list")
    public String showAllSchedules(Model model) {
        model.addAttribute("schedules", service.getAllClassSchedules());
        return "schedules";
    }

    @GetMapping("/edit/{id}")
    public String showEditScheduleForm(@PathVariable String id, Model model) {
        try {
            ClassSchedule schedule = service.getClassScheduleById(id);
            if (schedule == null) {
                model.addAttribute("message", "Schedule not found!");
                model.addAttribute("schedules", service.getAllClassSchedules());
                return "schedules";
            }
            model.addAttribute("classSchedule", schedule);
            model.addAttribute("message", null);
            return "edit-schedule";
        } catch (Exception e) {
            model.addAttribute("message", "Error retrieving schedule: " + e.getMessage());
            logger.error("Error retrieving schedule with ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("schedules", service.getAllClassSchedules());
            return "schedules";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSchedule(@PathVariable String id, @Valid @ModelAttribute ClassSchedule classSchedule, BindingResult bindingResult, Model model) {
        logger.info("Received class schedule for update: {}", classSchedule);
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Validation errors occurred.");
            return "edit-schedule";
        }
        try {
            classSchedule.setId(id);
            service.updateClassSchedule(classSchedule);
            model.addAttribute("message", "Class schedule updated successfully!");
            model.addAttribute("schedules", service.getAllClassSchedules());
            return "schedules";
        } catch (Exception e) {
            model.addAttribute("message", "Error during update: " + e.getMessage());
            logger.error("Update failed for schedule ID {}: {}", id, e.getMessage(), e);
            model.addAttribute("classSchedule", classSchedule);
            return "edit-schedule";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable String id, Model model) {
        logger.info("Attempting to delete schedule with ID: {}", id);
        try {
            service.deleteClassSchedule(id);
            model.addAttribute("message", "Class schedule deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("message", "Error during deletion: " + e.getMessage());
            logger.error("Deletion failed for schedule ID {}: {}", id, e.getMessage(), e);
        }
        model.addAttribute("schedules", service.getAllClassSchedules());
        return "schedules";
    }

    @GetMapping("/search")
    public String searchSchedules(@RequestParam String query, Model model) {
        List<ClassSchedule> schedules = service.searchSchedules(query);
        model.addAttribute("schedules", schedules);
        model.addAttribute("searchQuery", query);
        return "schedules";
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateScheduleReport() {
        byte[] report = service.generateScheduleReport();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "schedules-report.pdf");
        return ResponseEntity.ok().headers(headers).body(report);
    }

    @GetMapping("/{id}/enroll-students")
    public String showEnrollStudentsForm(@PathVariable String id, Model model) {
        ClassSchedule schedule = service.getClassScheduleById(id);
        if (schedule == null) {
            return "redirect:/schedules/list";
        }
        List<Student> allStudents = studentService.getAllStudents();
        List<String> enrolledStudentIds = schedule.getStudentIds();
        List<Student> availableStudents = allStudents.stream()
            .filter(s -> !enrolledStudentIds.contains(s.getId()))
            .toList();
        model.addAttribute("schedule", schedule);
        model.addAttribute("availableStudents", availableStudents);
        return "enroll-student-to-schedule";
    }

    @PostMapping("/{id}/enroll-students")
    public String enrollStudentsToSchedule(@PathVariable String id, @RequestParam String studentId, Model model) {
        try {
            studentService.enrollInSchedule(studentId, id); // Note: Calling studentService.enrollInSchedule
            return "redirect:/schedules/" + id + "/enroll-students?success=true";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return showEnrollStudentsForm(id, model);
        }
    }
}