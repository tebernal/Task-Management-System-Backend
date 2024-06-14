package org.teresa.Task_Management_System.enums;

/**
 * TaskStatus is an enumeration that defines the possible states a task can have
 * within the task management system.
 */
public enum TaskStatus {

    /**
     * Indicates that the task has not been started yet.
     */
    PENDING,

    /**
     * Indicates that the task is currently in progress.
     */
    IN_PROGRESS,

    /**
     * Indicates that the task has been completed successfully.
     */
    COMPLETED,

    /**
     * Indicates that the task has been deferred and will be addressed later.
     */
    DEFERRED,

    /**
     * Indicates that the task has been cancelled and will not be completed.
     */
    CANCELLED
}
