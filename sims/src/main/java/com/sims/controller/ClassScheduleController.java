package com.sims.controller;

import com.sims.model.ClassSchedule;
import com.sims.service.ClassScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/schedules")
public class ClassScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleController.class);

    @Autowired
    private ClassScheduleService service;

    @GetMapping("/add")
    public String showAddScheduleForm(Model model) {
        model.addAttribute("classSchedule", new ClassSchedule());
        model.addAttribute("message", null);
        return "schedule";
    }

    @PostMapping("/add")
    public String addSchedule(@ModelAttribute ClassSchedule classSchedule, Model model) {
        logger.info("Received class schedule for addition: {}", classSchedule);
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
    public String updateSchedule(@PathVariable String id, @ModelAttribute ClassSchedule classSchedule, Model model) {
        logger.info("Received class schedule for update: {}", classSchedule);
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
}