package org.teresa.Task_Management_System.services;

import org.teresa.Task_Management_System.dto.SignupRequest;
import org.teresa.Task_Management_System.dto.UserDto;

/**
 * AuthService interface defines the operations related to authentication and user registration
 * within the task management system.
 */
public interface AuthService {

    /**
     * Registers a new user in the system.
     *
     * @param signupRequest the SignupRequest object containing the user's registration details.
     * @return the UserDto object representing the newly registered user.
     */
    UserDto signupUser(SignupRequest signupRequest);

    /**
     * Checks if a user with the given email address already exists in the system.
     *
     * @param email the email address to check for existence.
     * @return true if a user with the specified email exists, false otherwise.
     */
    boolean hasUserWithEmail(String email);
}
