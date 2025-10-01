package com.sims.service;

import com.sims.model.ClassSchedule;
import com.sims.repository.ClassScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ClassScheduleService.class);
    @Autowired
    private ClassScheduleRepository classScheduleRepository;
public void addClassSchedule(ClassSchedule classSchedule) {
    logger.info("Attempting to save class schedule: {}", classSchedule);
    try {
        ClassSchedule savedSchedule = classScheduleRepository.save(classSchedule);
        logger.info("Class schedule saved successfully with ID: {}", savedSchedule.getId());
    } catch (Exception e) {
        logger.error("Failed to save class schedule: {}", e.getMessage(), e);
    }
}

    public List<ClassSchedule> getAllClassSchedules() {
        List<ClassSchedule> schedules = classScheduleRepository.findAll();
        logger.info("Retrieved {} class schedules", schedules.size());
        return schedules;
    }
}