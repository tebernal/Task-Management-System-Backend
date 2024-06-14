package org.teresa.Task_Management_System.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.enums.UserRole;

import java.util.Optional;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    // Custom query
    /**
     * Retrieves the first user with the specified email.
     * @param email The email of the user
     * @return Optional containing the user with the specified email, if found
     */
    Optional<User> findFirstByEmail(String email);

    // Custom query


    /**
     * Retrieves a user by their role.
     * @param userRole The role of the user
     * @return Optional containing the user with the specified role, if found
     */
    Optional<User> findByUserRole(UserRole userRole);
}
