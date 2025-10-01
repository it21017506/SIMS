package com.sims.repository;

import com.sims.model.ClassSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClassScheduleRepository extends MongoRepository<ClassSchedule, String> {
}