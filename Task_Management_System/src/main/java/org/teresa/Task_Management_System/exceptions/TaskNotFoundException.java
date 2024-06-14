package org.teresa.Task_Management_System.exceptions;

/**
 * Exception thrown when a task is not found in the system.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructs a new TaskNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}
