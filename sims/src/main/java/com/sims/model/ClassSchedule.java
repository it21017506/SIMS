package com.sims.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class ClassSchedule {
    @Id
    private String id;
    private String className;
    private String instructor;
    private String time;
    private String room;
}