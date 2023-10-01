package com.hampcode.taskmanager.service;

import com.hampcode.taskmanager.dto.*;
import com.hampcode.taskmanager.exception.ResourceNotFoundException;
import com.hampcode.taskmanager.exception.ResourceValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {
    private final List<TaskResponseDTO> tasks = new ArrayList<>();
    private final List<ProjectResponseDTO> projects = new ArrayList<>();

    private final Validator validator;

    private Long taskIdCounter = 1L;

    public TaskService(Validator validator){
        this.validator = validator;
        this.loadProjects();
    }

    public List<TaskResponseDTO> getAllTask(){
        return tasks;
    }

    public TaskResponseDTO createTask(TaskRequestDTO taskRequest){
        Set<ConstraintViolation<TaskRequestDTO>> violations = validator.validate(taskRequest);

        if(!violations.isEmpty()){
            throw new ResourceValidationException("Task", violations);
        }

        validateDuplicateTask(taskRequest);

        ProjectResponseDTO project = projects
                .stream()
                .filter(p-> p.getId().equals(taskRequest.getProjectId()))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("El proyecto con el ID " + taskRequest.getProjectId() + " no existe"));

        TaskResponseDTO newTask = new TaskResponseDTO();
        newTask.setId(taskIdCounter++);
        newTask.setName(taskRequest.getName());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setAssignmentDate(taskRequest.getAssignmentDate());
        newTask.setTime(taskRequest.getTime());
        newTask.setStatus(TaskStatus.PENDING);
        newTask.setProject(project);

        tasks.add(newTask);

        return newTask;

    }

    public TaskResponseDTO completeTask(Long taskId){
        TaskResponseDTO taskToUpdate = tasks.stream()
                .filter(task-> task.getId().equals(taskId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("La tarea con el ID " + taskId + " no existe"));

        taskToUpdate.setStatus(TaskStatus.COMPLETED);

        return taskToUpdate;
    }

    public List<TaskResponseDTO> getTasksByStatus(String nameStatus){
        return tasks.stream()
                .filter(task-> task.getStatus().name().equalsIgnoreCase(nameStatus))
                .toList();
    }

    public List<TaskResponseDTO> getTasksByDateRange(LocalDate startDate, LocalDate endDate){
        if(endDate.isBefore(startDate))
            throw  new ResourceValidationException("La fecha de fin no debe ser menor a la fecha de inicio");

        return tasks.stream()
                .filter(task->!task.getAssignmentDate().isBefore(startDate) && !task.getAssignmentDate().isAfter(endDate))
                .toList();
    }

    public List<TaskResponseDTO> getTasksByProjectName(String projectName){
        return tasks.stream()
                .filter(task-> task.getProject().getName().equalsIgnoreCase(projectName))
                .toList();
    }

    private void loadProjects(){
        ProjectResponseDTO project1 = new ProjectResponseDTO();
        project1.setId(1L);
        project1.setName("Proyecto A");

        ProjectResponseDTO project2 = new ProjectResponseDTO();
        project2.setId(2L);
        project2.setName("Proyecto B");

        projects.add(project1);
        projects.add(project2);
    }


    private void validateDuplicateTask(TaskRequestDTO taskRequest){
        boolean taskAlreadyExists = tasks
                .stream()
                .anyMatch(existingTask -> existingTask.getName().equals(taskRequest.getName()) &&
                        existingTask.getAssignmentDate().equals(taskRequest.getAssignmentDate()));

        if(taskAlreadyExists){
            throw  new ResourceValidationException("Ya existe una tarea con el mismo nombre y fecha de asignacion");
        }

    }
}
