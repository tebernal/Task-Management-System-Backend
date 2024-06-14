package org.teresa.Task_Management_System.services;

import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.dto.UserDto;

import java.util.List;

/**
 * AdminService interface defines the operations available for managing users, tasks, and comments
 * within the task management system.
 */
public interface AdminService {

    /**
     * Retrieves a list of all users with the role of EMPLOYEE.
     *
     * @return a list of UserDto objects representing users with EMPLOYEE role.
     */
    List<UserDto> getUsers();

    /**
     * Creates a new task and assigns it to a user.
     *
     * @param taskDto the TaskDto object containing the details of the task to be created.
     * @return the created TaskDto object.
     */
    TaskDto createTask(TaskDto taskDto);

    /**
     * Retrieves a list of all tasks sorted by their due date in descending order.
     *
     * @return a list of TaskDto objects representing all tasks.
     */
    List<TaskDto> getAllTasks();

    /**
     * Deletes a task identified by its ID.
     *
     * @param id the ID of the task to be deleted.
     */
    void deleteTask(Long id);

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the TaskDto object representing the task.
     */
    TaskDto getTaskById(Long id);

    /**
     * Updates an existing task with new details.
     *
     * @param id the ID of the task to be updated.
     * @param taskDto the TaskDto object containing the new details of the task.
     * @return the updated TaskDto object.
     */
    TaskDto updateTask(Long id, TaskDto taskDto);

    /**
     * Searches for tasks based on their title.
     *
     * @param title the title to search for in tasks.
     * @return a list of TaskDto objects that match the search criteria.
     */
    List<TaskDto> searchTaskByTitle(String title);

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
