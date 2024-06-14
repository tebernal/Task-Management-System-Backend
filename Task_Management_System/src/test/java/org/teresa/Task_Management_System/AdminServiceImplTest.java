package org.teresa.Task_Management_System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.teresa.Task_Management_System.dto.TaskDto;
import org.teresa.Task_Management_System.entities.Task;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.enums.UserRole;
import org.teresa.Task_Management_System.repositories.CommentRepository;
import org.teresa.Task_Management_System.repositories.TaskRepository;
import org.teresa.Task_Management_System.repositories.UserRepository;
import org.teresa.Task_Management_System.services.servicesImpl.AdminServiceImpl;
import org.teresa.Task_Management_System.utils.JwtUtil;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUsers() {
        // Given
        User user = new User();
        user.setUserRole(UserRole.EMPLOYEE);
        when(userRepository.findAll()).thenReturn(List.of(user));

        // When
        var users = adminService.getUsers();

        // Then
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testCreateTask() {
        // Given
        User user = new User();
        user.setId(1L);
        TaskDto taskDto = new TaskDto();
        taskDto.setEmployeeId(1L);

        Task task = new Task();
        task.setId(1L);
        task.setUser(user);
        task.setTitle("Task 1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // When
        var result = adminService.createTask(taskDto);

        // Then
        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetAllTasks() {
        // Given
        User user = new User();
        Task task = new Task();
        task.setTitle("Task 1");
        task.setUser(user);
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When
        var tasks = adminService.getAllTasks();

        // Then
        assertEquals(1, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteTask() {
        // Given
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        // When
        adminService.deleteTask(1L);

        // Then
        verify(taskRepository, times(1)).existsById(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTaskById() {
        // Given
        Task task = new Task();
        User user = new User();
        task.setId(1L);
        task.setUser(user);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        var taskDto = adminService.getTaskById(1L);

        // Then
        assertNotNull(taskDto);
        assertEquals(1L, taskDto.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @ParameterizedTest
    @ValueSource(strings = {"task1", "task2"})
    public void testSearchTaskByTitle(String title) {
        // Given
        Task task = new Task();
        User user = new User();
        task.setUser(user);
        task.setTitle(title);
        when(taskRepository.findAllByTitleContaining(title)).thenReturn(List.of(task));

        // When
        var tasks = adminService.searchTaskByTitle(title);

        // Then
        assertEquals(1, tasks.size());
        assertEquals(title, tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAllByTitleContaining(title);
    }
}
