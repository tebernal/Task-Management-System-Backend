package org.teresa.Task_Management_System.dto;

import lombok.Data;

/**
 * AuthenticationRequest is a Data Transfer Object (DTO) that encapsulates
 * the data required for user authentication.
 * This class is typically used when a user attempts to log in to the system.
 */
@Data
public class AuthenticationRequest {

    /**
     * The email of the user attempting to authenticate.
     * This serves as the username in the login process.
     */
    private String email;

    /**
     * The password of the user attempting to authenticate.
     * This should be provided along with the email to validate the user's identity.
     */
    private String password;
}
