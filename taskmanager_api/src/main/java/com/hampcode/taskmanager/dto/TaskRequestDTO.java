package com.hampcode.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotBlank(message = "El nombre no debe estar en blanco")
    @Size(max= 70, message = "El nombre no debe exceder de 70 caracteres")
    private String name;
    @NotBlank(message = "La descripcion no debe estar en blanco")
    @Size(max= 150, message = "La descripcion no debe exceder de 150 caracteres")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignmentDate;

    private TaskStatus status;

    @PositiveOrZero(message = "El tiempo no debe ser cero ni negativo")
    @NotNull(message = "El tiempo no debe ser null")
    private Integer time;

    @NotNull(message = "El id del proyecto no debe ser null")
    private Long projectId;
}
