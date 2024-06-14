package org.teresa.Task_Management_System.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teresa.Task_Management_System.entities.Task;

import java.util.List;

/**
 * Repository interface for managing Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Custom query
    /**
     * Retrieves a list of tasks with titles containing the specified keyword.
     * @param title The keyword to search for in task titles
     * @return List of tasks matching the search criteria
     */
    List<Task> findAllByTitleContaining(String title);



    // Custom query
    /**
     * Retrieves a list of tasks assigned to a specific user.
     * @param id The ID of the user
     * @return List of tasks assigned to the user
     */
    List<Task> findAllByUserId(Long id);

}
