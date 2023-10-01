package com.hampcode.taskmanager.controller;

import com.hampcode.taskmanager.dto.TaskRequestDTO;
import com.hampcode.taskmanager.dto.TaskResponseDTO;
import com.hampcode.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskResponseDTO> getAllTask(){
        return taskService.getAllTask();
    }

    @PostMapping
    public TaskResponseDTO createTask(@Valid @RequestBody TaskRequestDTO taskRequest){
        return taskService.createTask(taskRequest);
    }

    @PutMapping("/{taskId}/complete")
    public TaskResponseDTO completeTask(@PathVariable Long taskId){
        return taskService.completeTask(taskId);
    }

    @GetMapping("/status")
    public List<TaskResponseDTO> getAllTaskByStatus(
            @RequestParam("status") String status
    ){
        return taskService.getTasksByProjectName(status);
    }

    @GetMapping("/date-range")
    public List<TaskResponseDTO> getAllTaskByDateRange(
            @RequestParam("startDate")LocalDate startDate,
            @RequestParam("endDate")LocalDate endDate
            ){
        return taskService.getTasksByDateRange(startDate,endDate);
    }

    @GetMapping("/project")
    public List<TaskResponseDTO> getAllTaskByProject(
            @RequestParam("projectName")String projectName
    ){
        return taskService.getTasksByProjectName(projectName);
    }

}
