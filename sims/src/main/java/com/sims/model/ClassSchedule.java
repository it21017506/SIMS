package com.sims.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassSchedule {
    @Id
    private String id;

    @NotBlank(message = "Class name is required")
    private String className;

    @NotBlank(message = "Instructor is required")
    private String instructor;

    @NotBlank(message = "Time is required")
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]-([0-1][0-9]|2[0-3]):[0-5][0-9]$", 
             message = "Time must be in 'HH:MM-HH:MM' format (e.g., 09:00-10:30)")
    private String time;

    @NotBlank(message = "Room is required")
    private String room;

    @NotBlank(message = "Duration is required")
    private String duration; // e.g., "1.5 hours"

    @NotBlank(message = "Max capacity is required")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "Max capacity must be a positive number")
    private String maxCapacity; // e.g., "30"

    @Builder.Default
    private List<String> studentIds = new ArrayList<>();
}