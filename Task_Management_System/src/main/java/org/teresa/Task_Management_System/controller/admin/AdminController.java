package org.teresa.Task_Management_System.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.services.AdminService;

import java.util.List;

/**
 * AdminController handles HTTP requests related to administrative tasks.
 * It provides endpoints for managing users, tasks, and comments.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService adminService;

    /**
     * Endpoint to retrieve all users with the role of EMPLOYEE.
     * @return ResponseEntity containing a list of UserDto objects
     */
    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(adminService.getUsers());
    }

    /**
     * Endpoint to create a new task.
     * @param taskDto TaskDto object containing task details
     * @return ResponseEntity containing the created TaskDto object
     */
    @PostMapping("/task")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        TaskDto createdTaskDto = adminService.createTask(taskDto);
        if (createdTaskDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDto);
    }

    /**
     * Endpoint to retrieve all tasks.
     * @return ResponseEntity containing a list of TaskDto objects
     */
    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(adminService.getAllTasks());
    }

    /**
     * Endpoint to delete a task by its ID.
     * @param id ID of the task to be deleted
     * @return ResponseEntity indicating success or failure of the operation
     */
    @DeleteMapping("/task/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        adminService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint to retrieve a task by its ID.
     * @param id ID of the task to retrieve
     * @return ResponseEntity containing the TaskDto object
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = adminService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    /**
     * Endpoint to update a task.
     * @param id ID of the task to update
     * @param taskDto TaskDto object containing updated task details
     * @return ResponseEntity containing the updated TaskDto object
     */
    @PutMapping("/task/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        TaskDto updatedTask = adminService.updateTask(id, taskDto);
        if (updatedTask == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Endpoint to search tasks by their title.
     * @param title Title to search for
     * @return ResponseEntity containing a list of TaskDto objects matching the search criteria
     */
    @GetMapping("/tasks/search/{title}")
    public ResponseEntity<List<TaskDto>> searchTask(@PathVariable String title) {
        return ResponseEntity.ok(adminService.searchTaskByTitle(title));
    }

    /**
     * Endpoint to create a comment for a task.
     * @param taskId ID of the task to add the comment to
     * @param content Content of the comment
     * @return ResponseEntity containing the created CommentDto object
     */
    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content) {
        CommentDto createdCommentDto = adminService.createComment(taskId, content);
        if (createdCommentDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCommentDto);
    }

    /**
     * Endpoint to retrieve comments for a specific task.
     * @param taskId ID of the task
     * @return ResponseEntity containing a list of CommentDto objects for the task
     */
    @GetMapping("/comments/{taskId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(adminService.getCommentsByTaskId(taskId));
    }
}
