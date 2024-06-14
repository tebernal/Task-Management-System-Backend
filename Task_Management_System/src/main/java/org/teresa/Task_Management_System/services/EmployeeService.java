package org.teresa.Task_Management_System.services;

import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;

import java.util.List;

/**
 * EmployeeService interface defines the operations available for employees
 * to manage their tasks within the task management system.
 */
public interface EmployeeService {
    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the TaskDto object representing the task.
     */
    TaskDto getTaskById(Long id);

    /**
     * Retrieves all tasks assigned to the currently logged-in user.
     *
     * @return a list of TaskDto objects representing the tasks assigned to the user.
     */
    List<TaskDto> getTasksByUserId();

    /**
     * Updates the status of a specified task.
     *
     * @param id the ID of the task to be updated.
     * @param status the new status to be assigned to the task.
     * @return the updated TaskDto object representing the task with the new status.
     */
    TaskDto updateTask(Long id, String status);

    /**
     * Creates a new comment for a specific task.
     *
     * @param taskId the ID of the task to which the comment belongs.
     * @param content the content of the comment.
     * @return the created CommentDto object.
     */
    CommentDto createComment(Long taskId, String content);

    /**
     * Retrieves all comments associated with a specific task.
     *
     * @param taskId the ID of the task whose comments are to be retrieved.
     * @return a list of CommentDto objects representing the comments for the task.
     */
    List<CommentDto> getCommentsByTaskId(Long taskId);
}
