package org.teresa.Task_Management_System.dto;

import lombok.Data;

import java.util.Date;

/**
 * CommentDto is a Data Transfer Object (DTO) that encapsulates the data
 * related to comments within the task management system.
 * This class is used to transfer comment data between the client and server.
 */
@Data
public class CommentDto {

    /**
     * The unique identifier of the comment.
     * This ID is used to reference the comment in the system.
     */
    private Long id;

    /**
     * The content or text of the comment.
     * This field holds the actual comment made by the user.
     */
    private String content;

    /**
     * The date and time when the comment was created.
     * This timestamp indicates when the comment was posted.
     */
    private Date createdAt;

    /**
     * The ID of the task to which the comment is related.
     * This links the comment to a specific task in the system.
     */
    private Long taskId;

    /**
     * The ID of the user who posted the comment.
     * This references the user who made the comment.
     */
    private Long userId;

    /**
     * The name of the user who posted the comment.
     * This provides a human-readable reference to the commenter.
     */
    private String postedBy;
}
