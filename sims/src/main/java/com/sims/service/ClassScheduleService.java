package com.sims.service;

import com.sims.model.ClassSchedule;
import com.sims.repository.ClassScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
            throw e;
        }
    }

    public List<ClassSchedule> getAllClassSchedules() {
        List<ClassSchedule> schedules = classScheduleRepository.findAll();
        logger.info("Retrieved {} class schedules", schedules.size());
        return schedules;
    }

    public ClassSchedule getClassScheduleById(String id) {
        logger.info("Retrieving class schedule with ID: {}", id);
        Optional<ClassSchedule> schedule = classScheduleRepository.findById(id);
        if (schedule.isPresent()) {
            logger.info("Class schedule found with ID: {}", id);
            return schedule.get();
        } else {
            logger.warn("Class schedule not found with ID: {}", id);
            return null;
        }
    }

    public void updateClassSchedule(ClassSchedule classSchedule) {
        logger.info("Attempting to update class schedule: {}", classSchedule);
        try {
            if (classScheduleRepository.existsById(classSchedule.getId())) {
                classScheduleRepository.save(classSchedule);
                logger.info("Class schedule updated successfully with ID: {}", classSchedule.getId());
            } else {
                logger.error("Class schedule with ID {} does not exist", classSchedule.getId());
                throw new IllegalArgumentException("Schedule not found");
            }
        } catch (Exception e) {
            logger.error("Failed to update class schedule: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void deleteClassSchedule(String id) {
        logger.info("Attempting to delete class schedule with ID: {}", id);
        try {
            if (classScheduleRepository.existsById(id)) {
                classScheduleRepository.deleteById(id);
                logger.info("Class schedule deleted successfully with ID: {}", id);
            } else {
                logger.error("Class schedule with ID {} does not exist", id);
                throw new IllegalArgumentException("Schedule not found");
            }
        } catch (Exception e) {
            logger.error("Failed to delete class schedule: {}", e.getMessage(), e);
            throw e;
        }
    }
}