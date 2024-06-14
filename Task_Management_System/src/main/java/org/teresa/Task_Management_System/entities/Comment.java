package org.teresa.Task_Management_System.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.teresa.Task_Management_System.dto.CommentDto;

import java.util.Date;

/**
 * Comment Entity represents a comment on a task in the system.
 */
@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for the comment

    private String content; // Content of the comment

    private Date createdAt; // Date and time when the comment was created

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user; // User who posted the comment

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Task task; // Task to which the comment belongs

    /**
     * Retrieves a Data Transfer Object (DTO) representing the comment.
     *
     * @return a CommentDto object containing comment data
     */
    public CommentDto getCommentDto() {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(id);
        commentDto.setContent(content);
        commentDto.setCreatedAt(createdAt);
        commentDto.setTaskId(task.getId());
        commentDto.setPostedBy(user.getName());
        return commentDto;
    }

}
