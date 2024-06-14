package org.teresa.Task_Management_System.exceptions;

/**
 * Exception thrown when an entity is not found.
 */
public class EntityNotFoundException extends RuntimeException {
    /**
     * Constructs a new EntityNotFoundException with the specified detail message.
     * @param message the detail message.
     */
    public EntityNotFoundException(String message) {
        super(message);
    }
}
