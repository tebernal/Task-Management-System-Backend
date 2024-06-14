package org.teresa.Task_Management_System.controller.employee;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.services.EmployeeService;

import java.util.List;

/**
 * EmployeeController handles HTTP requests related to employee-specific tasks.
 * It provides endpoints to retrieve tasks assigned to the logged-in employee and update task status.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employee")
@CrossOrigin("*")
public class EmployeeController {
    private final EmployeeService employeeService;

    /**
     * Endpoint to get tasks assigned to the logged-in employee.
     * @return ResponseEntity containing a list of TaskDto objects representing tasks assigned to the employee
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getTasksByUserId(){
        // Retrieve tasks assigned to the logged-in employee
        return ResponseEntity.ok(employeeService.getTasksByUserId());
    }

    /**
     * Endpoint to update the status of a task assigned to the logged-in employee.
     * @param id Task ID
     * @param status New status of the task
     * @return ResponseEntity containing the updated TaskDto object if successful, or an error response if failed
     */
    @GetMapping("/task/{id}/{status}")
    public ResponseEntity<TaskDto> updateTask(@PathVariable Long id, @PathVariable String status) {
        // Update the status of the task with the given ID
        TaskDto updatedTaskDTO = employeeService.updateTask(id, status);

        // If task update fails, return error response
        if (updatedTaskDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Return success response with the updated task details
        return ResponseEntity.ok(updatedTaskDTO);
    }

    /**
     * Endpoint to retrieve a task by its ID.
     * @param id ID of the task to retrieve
     * @return ResponseEntity containing the TaskDto object
     */
    @GetMapping("/task/{id}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        TaskDto taskDto = employeeService.getTaskById(id);
        return ResponseEntity.ok(taskDto);
    }

    @PostMapping("/task/comment/{taskId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long taskId, @RequestParam String content) {
        CommentDto createdCommentDto = employeeService.createComment(taskId, content);
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
        return ResponseEntity.ok(employeeService.getCommentsByTaskId(taskId));
    }
}
