package org.teresa.Task_Management_System.services.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.entities.Comment;
import org.teresa.Task_Management_System.entities.Task;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.enums.TaskStatus;
import org.teresa.Task_Management_System.exceptions.TaskNotFoundException;
import org.teresa.Task_Management_System.repositories.CommentRepository;
import org.teresa.Task_Management_System.repositories.TaskRepository;
import org.teresa.Task_Management_System.services.EmployeeService;
import org.teresa.Task_Management_System.utils.JwtUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public TaskDto getTaskById(Long id) {
        log.info("Fetching task with ID: {}", id);
        return taskRepository.findById(id)
                .map(task -> {
                    TaskDto taskDto = task.getTaskDTO();
                    log.info("Task found with ID: {}", id);
                    return taskDto;
                })
                .orElseThrow(() -> {
                    log.error("Task with ID {} not found", id);
                    return new TaskNotFoundException("Task with ID " + id + " not found.");
                });
    }

    @Override
    public List<TaskDto> getTasksByUserId() {
        User user = jwtUtil.getLoggedInUser();
        if (user != null) {
            log.info("Fetching tasks for user ID: {}", user.getId());
            List<TaskDto> tasks = taskRepository.findAllByUserId(user.getId())
                    .stream()
                    .sorted(Comparator.comparing(Task::getDueDate).reversed())
                    .map(Task::getTaskDTO)
                    .collect(Collectors.toList());
            log.debug("Fetched tasks: {}", tasks);
            return tasks;
        } else {
            log.warn("Logged in user not found");
            throw new EntityNotFoundException("User not found");
        }
    }

    @Override
    public TaskDto updateTask(Long id, String status) {
        log.info("Attempting to update task status with ID: {}", id);
        Optional<Task> optionalTask = taskRepository.findById(id);

        if (optionalTask.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTaskStatus(mapStringToTaskStatus(status));

            Task updatedTask = taskRepository.save(existingTask);
            log.info("Task with ID {} status updated to {}", id, status);
            return updatedTask.getTaskDTO();
        } else {
            log.warn("Task with ID {} not found, cannot update status", id);
            throw new EntityNotFoundException("Task not found");
        }
    }

    @Override
    public CommentDto createComment(Long taskId, String content) {
        log.info("Creating comment for task ID: {}", taskId);
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();

        if (optionalTask.isPresent() && user != null) {
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);

            Comment savedComment = commentRepository.save(comment);
            log.info("Comment created successfully with ID: {}", savedComment.getId());
            return savedComment.getCommentDto();
        } else {
            if (optionalTask.isEmpty()) {
                log.warn("Task with ID {} not found", taskId);
            }
            if (user == null) {
                log.warn("No logged in user found");
            }
            throw new org.teresa.Task_Management_System.exceptions.EntityNotFoundException("User or Task not found");
        }
    }

    @Override
    public List<CommentDto> getCommentsByTaskId(Long taskId) {
        log.info("Fetching comments for task ID: {}", taskId);
        List<CommentDto> comments = commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(Comment::getCommentDto)
                .collect(Collectors.toList());
        log.debug("Fetched comments: {}", comments);
        return comments;
    }

    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "PENDING" -> TaskStatus.PENDING;
            case "INPROGRESS" -> TaskStatus.IN_PROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }
}
