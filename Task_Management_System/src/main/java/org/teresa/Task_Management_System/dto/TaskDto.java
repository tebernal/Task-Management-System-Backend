package org.teresa.Task_Management_System.dto;

import lombok.Data;
import org.teresa.Task_Management_System.enums.TaskStatus;

import java.util.Date;

/**
 * TaskDto is a Data Transfer Object (DTO) that encapsulates the data
 * related to tasks within the task management system.
 * This class is used to transfer task data between the client and server.
 */
@Data
public class TaskDto {

    /**
     * The unique identifier of the task.
     * This ID is used to reference the task within the system.
     */
    private Long id;

    /**
     * The title or name of the task.
     * This field provides a brief description of what the task entails.
     */
    private String title;

    /**
     * The detailed description of the task.
     * This field may contain additional information or instructions related to the task.
     */
    private String description;

    /**
     * The due date or deadline for completing the task.
     * This date indicates when the task is expected to be finished.
     */
    private Date dueDate;

    /**
     * The priority level of the task.
     * This field defines the urgency or importance of the task relative to other tasks.
     */
    private String priority;

    /**
     * The status of the task.
     * This field represents the current state or progress of the task, such as "Pending", "In Progress", or "Completed".
     */
    private TaskStatus taskStatus;

    /**
     * The unique identifier of the employee assigned to the task.
     * This ID links the task to the user who is responsible for completing it.
     */
    private Long employeeId;

    /**
     * The name of the employee assigned to the task.
     * This provides a human-readable reference to the employee who is responsible for the task.
     */
    private String employeeName;
}
