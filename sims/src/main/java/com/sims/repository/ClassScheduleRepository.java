package com.sims.repository;

import com.sims.model.ClassSchedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ClassScheduleRepository extends MongoRepository<ClassSchedule, String> {
    @Query("{'$or': [{'className': {$regex: ?0, $options: 'i'}}, {'instructor': {$regex: ?0, $options: 'i'}}]}")
    List<ClassSchedule> findByClassNameOrInstructorContainingIgnoreCase(String searchTerm);
}