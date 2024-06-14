package org.teresa.Task_Management_System.dto;

import lombok.Data;
import org.teresa.Task_Management_System.enums.UserRole;

/**
 * AuthenticationResponse is a Data Transfer Object (DTO) that encapsulates
 * the response data returned to the client after a successful authentication.
 * This class typically includes a JWT token and user details for the authenticated session.
 */
@Data
public class AuthenticationResponse {

    /**
     * The JSON Web Token (JWT) issued to the user after successful authentication.
     * This token is used to securely identify the user in subsequent requests.
     */
    private String jwt;

    /**
     * The unique identifier of the authenticated user.
     * This ID can be used to fetch or relate other user-specific data within the system.
     */
    private Long userId;

    /**
     * The role of the authenticated user.
     * This indicates the user's permissions and access level within the system.
     */
    private UserRole userRole;
}
