package org.teresa.Task_Management_System;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.teresa.Task_Management_System.entities.Comment;
import org.teresa.Task_Management_System.entities.Task;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.enums.TaskStatus;
import org.teresa.Task_Management_System.repositories.CommentRepository;
import org.teresa.Task_Management_System.repositories.TaskRepository;
import org.teresa.Task_Management_System.services.servicesImpl.EmployeeServiceImpl;
import org.teresa.Task_Management_System.utils.JwtUtil;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

    @SpringBootTest
    public class EmployeeServiceImplTest {

        @Mock
        private TaskRepository taskRepository;

        @Mock
        private CommentRepository commentRepository;

        @Mock
        private JwtUtil jwtUtil;

        @InjectMocks
        private EmployeeServiceImpl employeeService;

        @BeforeEach
        public void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        public void testGetTasksByUserId() {
            // Given
            User user = new User();
            user.setId(1L);
            when(jwtUtil.getLoggedInUser()).thenReturn(user);

            Task task = new Task();
            task.setUser(user);
            task.setTitle("Task 1");
            when(taskRepository.findAllByUserId(1L)).thenReturn(List.of(task));

            // When
            var tasks = employeeService.getTasksByUserId();

            // Then
            assertEquals(1, tasks.size());
            assertEquals("Task 1", tasks.get(0).getTitle());
            verify(taskRepository, times(1)).findAllByUserId(1L);
        }

        @Test
        public void testUpdateTaskStatus() {
            // Given
            Task task = new Task();
            User user = new User();
            task.setUser(user);
            task.setId(1L);
            when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
            when(taskRepository.save(any(Task.class))).thenReturn(task);

            // When
            var updatedTask = employeeService.updateTask(1L, "COMPLETED");

            // Then
            assertNotNull(updatedTask);
            assertEquals(TaskStatus.COMPLETED, updatedTask.getTaskStatus());
            verify(taskRepository, times(1)).findById(1L);
            verify(taskRepository, times(1)).save(any(Task.class));
        }

        @Test
        public void testCreateComment() {
            // Given
            User user = new User();
            user.setId(1L);
            when(jwtUtil.getLoggedInUser()).thenReturn(user);

            Task task = new Task();
            task.setId(1L);
            when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

            Comment comment = new Comment();
            comment.setContent("Nice");

        }
    }