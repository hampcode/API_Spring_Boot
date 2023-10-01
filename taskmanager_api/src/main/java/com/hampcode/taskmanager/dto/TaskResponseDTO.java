package com.hampcode.taskmanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate assignmentDate;
    private TaskStatus status;
    private Integer time;
    private ProjectResponseDTO project;
}
