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
}