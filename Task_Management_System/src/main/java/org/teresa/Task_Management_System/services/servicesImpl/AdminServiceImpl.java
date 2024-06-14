package org.teresa.Task_Management_System.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.teresa.Task_Management_System.dto.CommentDto;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.dto.UserDto;
import org.teresa.Task_Management_System.entities.Comment;
import org.teresa.Task_Management_System.entities.Task;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.enums.TaskStatus;
import org.teresa.Task_Management_System.enums.UserRole;
import org.teresa.Task_Management_System.exceptions.EntityNotFoundException;
import org.teresa.Task_Management_System.exceptions.TaskNotFoundException;
import org.teresa.Task_Management_System.repositories.CommentRepository;
import org.teresa.Task_Management_System.repositories.TaskRepository;
import org.teresa.Task_Management_System.repositories.UserRepository;
import org.teresa.Task_Management_System.services.AdminService;
import org.teresa.Task_Management_System.utils.JwtUtil;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j // Add this annotation to enable logging
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<UserDto> getUsers() {
        log.info("Fetching all users with role EMPLOYEE");
        List<UserDto> users = userRepository.findAll()
                .stream()
                .filter(user -> user.getUserRole() == UserRole.EMPLOYEE)
                .map(User::getUserDto)
                .collect(Collectors.toList());
        log.debug("Fetched users: {}", users);
        return users;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        log.info("Creating task for user ID: {}", taskDto.getEmployeeId());
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());

        if (optionalUser.isPresent()) {
            Task task = new Task();
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setPriority(taskDto.getPriority());
            task.setDueDate(taskDto.getDueDate());
            task.setTaskStatus(TaskStatus.IN_PROGRESS);
            task.setUser(optionalUser.get());

            Task savedTask = taskRepository.save(task);
            log.info("Task created successfully with ID: {}", savedTask.getId());
            log.debug("Created task details: {}", savedTask.getTaskDTO());
            return savedTask.getTaskDTO();
        } else {
            log.warn("User with ID {} not found. Task creation aborted.", taskDto.getEmployeeId());
            return null;
        }
    }

    @Override
    public List<TaskDto> getAllTasks() {
        log.info("Fetching all tasks");
        List<TaskDto> tasks = taskRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
        log.debug("Fetched tasks: {}", tasks);
        return tasks;
    }

    @Override
    public void deleteTask(Long id) {
        log.info("Attempting to delete task with ID: {}", id);

        // Check if the task exists
        if (!taskRepository.existsById(id)) {
            log.warn("Task with ID {} not found, cannot delete", id);
            throw new TaskNotFoundException("Task with ID " + id + " not found.");
        }

        // Delete the task if it exists
        taskRepository.deleteById(id);
        log.info("Task with ID {} deleted successfully", id);
    }

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
//    public TaskDto getTaskById(Long id) {
//        Optional<Task> optionalTask = taskRepository.findById(id);
//        return optionalTask.map(Task::getTaskDTO).orElse(null);
//    }

//    @Override
//    public TaskDto updateTask(Long id, TaskDto taskDto) {
//        log.info("Attempting to update task with ID: {}", id);
//
//        // Retrieve the task by ID
//        Task existingTask = taskRepository.findById(id)
//                .orElseThrow(() -> {
//                    log.warn("Task with ID {} not found, cannot update", id);
//                    return new TaskNotFoundException("Task with ID " + id + " not found.");
//                });
//
//        // Update the task details
//        existingTask.setTitle(taskDto.getTitle());
//        existingTask.setDescription(taskDto.getDescription());
//        existingTask.setDueDate(taskDto.getDueDate());
//        existingTask.setPriority(taskDto.getPriority());
//        existingTask.setTaskStatus(mapStringToTaskStatus(taskDto.getTaskStatus().toString()));
//
//        // Save the updated task
//        Task updatedTask = taskRepository.save(existingTask);
//        log.info("Task with ID {} updated successfully", id);
//
//        // Return the updated task DTO
//        return updatedTask.getTaskDTO();
//    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        Optional<User> optionalUser = userRepository.findById(taskDto.getEmployeeId());
        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task existingTask = optionalTask.get();
            existingTask.setTitle(taskDto.getTitle());
            existingTask.setDescription(taskDto.getDescription());
            existingTask.setDueDate(taskDto.getDueDate());
            existingTask.setPriority(taskDto.getPriority());
            existingTask.setTaskStatus(mapStringToTaskStatus(String.valueOf(taskDto.getTaskStatus())));
            existingTask.setUser(optionalUser.get());
            return taskRepository.save(existingTask).getTaskDTO();
        }
        return null;
    }

    @Override
    public List<TaskDto> searchTaskByTitle(String title) {
        return taskRepository.findAllByTitleContaining(title)
                .stream()
                .sorted(Comparator.comparing(Task::getDueDate).reversed())
                .map(Task::getTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(Long taskId, String content) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        User user = jwtUtil.getLoggedInUser();
        if (optionalTask.isPresent() && user != null) {
            Comment comment = new Comment();
            comment.setCreatedAt(new Date());
            comment.setContent(content);
            comment.setTask(optionalTask.get());
            comment.setUser(user);
            return commentRepository.save(comment).getCommentDto();
        }
        throw new EntityNotFoundException("User or Task not found");
    }

    @Override
    public List<CommentDto> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId).stream()
                .map(Comment::getCommentDto)
                .collect(Collectors.toList());
    }


    private TaskStatus mapStringToTaskStatus(String status) {
        return switch (status) {
            case "PENDING" -> TaskStatus.PENDING;
            case "IN_PROGRESS" -> TaskStatus.IN_PROGRESS;
            case "COMPLETED" -> TaskStatus.COMPLETED;
            case "DEFERRED" -> TaskStatus.DEFERRED;
            default -> TaskStatus.CANCELLED;
        };
    }

}


