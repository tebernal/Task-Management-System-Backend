package org.teresa.Task_Management_System.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teresa.Task_Management_System.entities.Comment;

import java.util.List;

/**
 * Repository interface for managing Comment entities.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Custom query
    /**
     * Retrieves a list of comments associated with a specific task.
     * @param taskId The ID of the task
     * @return List of comments associated with the task
     */
    List<Comment> findAllByTaskId(Long taskId);
}
