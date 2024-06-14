package org.teresa.Task_Management_System.dto;

import lombok.Data;

/**
 * SignupRequest is a Data Transfer Object (DTO) that encapsulates the
 * data required for user registration in the task management system.
 * This class is used to transfer signup data from the client to the server.
 */
@Data
public class SignupRequest {

    /**
     * The name of the user signing up for an account.
     * This field captures the full name of the user.
     */
    private String name;

    /**
     * The email address of the user signing up for an account.
     * This will serve as the user's login identifier.
     * The email should be unique and properly formatted.
     */
    private String email;

    /**
     * The password for the new account.
     * This field should be securely handled and stored.
     * Password policies, such as minimum length and complexity, should be enforced.
     */
    private String password;
}
