package org.teresa.Task_Management_System;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.teresa.Task_Management_System.dto.SignupRequest;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.repositories.UserRepository;
import org.teresa.Task_Management_System.services.servicesImpl.AuthServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignupUser() {
        // Given
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setName("Test User");
        request.setPassword("password");

        User user = new User();
        user.setEmail(request.getEmail());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        var result = authService.signupUser(request);

        // Then
        assertNotNull(result);
        assertEquals("test@test.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testHasUserWithEmail() {
        // Given
        when(userRepository.findFirstByEmail("test@test.com")).thenReturn(Optional.of(new User()));

        // When
        boolean exists = authService.hasUserWithEmail("test@test.com");

        // Then
        assertTrue(exists);
        verify(userRepository, times(1)).findFirstByEmail("test@test.com");
    }
}
