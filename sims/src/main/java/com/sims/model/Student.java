package com.sims.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Student {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gradeLevel;
    private String enrollmentDate;
}