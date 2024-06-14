package org.teresa.Task_Management_System.dto;

import lombok.Data;
import org.teresa.Task_Management_System.enums.UserRole;

/**
 * UserDto is a Data Transfer Object (DTO) that encapsulates the data
 * related to users within the task management system.
 * This class is used to transfer user data between the client and server.
 */
@Data
public class UserDto {

    /**
     * The unique identifier of the user.
     * This ID is used to reference the user within the system.
     */
    private Long id;

    /**
     * The name of the user.
     * This field contains the full name of the user.
     */
    private String name;

    /**
     * The email address of the user.
     * This serves as the login identifier for the user.
     * It should be unique and properly formatted.
     */
    private String email;

    /**
     * The role of the user within the system.
     * This field defines the user's permissions and access level.
     */
    private UserRole userRole;
}
