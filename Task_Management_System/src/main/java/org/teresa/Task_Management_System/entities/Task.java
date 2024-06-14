package org.teresa.Task_Management_System.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.enums.TaskStatus;

import java.util.Date;

/**
 * Task Entity represents a task in the system.
 */
@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the task

    private String title; // Title of the task

    private String description; // Description of the task

    private Date dueDate; // Due date of the task

    private String priority; // Priority level of the task

    private TaskStatus taskStatus; // Status of the task

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user; // User assigned to the task

    /**
     * Retrieves a Data Transfer Object (DTO) representing the task.
     *
     * @return a TaskDto object containing task data
     */
    public TaskDto getTaskDTO() {
        TaskDto taskDTO = new TaskDto();
        taskDTO.setId(id);
        taskDTO.setTitle(title);
        taskDTO.setDescription(description);
        taskDTO.setEmployeeName(user.getName());
        taskDTO.setEmployeeId(user.getId());
        taskDTO.setTaskStatus(taskStatus);
        taskDTO.setDueDate(dueDate);
        taskDTO.setPriority(priority);
        return taskDTO;
    }
}
