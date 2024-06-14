package org.teresa.Task_Management_System.services;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService interface provides a method to access the UserDetailsService,
 * which is used for retrieving user-related data in the context of Spring Security.
 */
public interface UserServices {

    /**
     * Provides the UserDetailsService implementation to be used for authentication and user retrieval.
     *
     * @return an instance of UserDetailsService that is used to load user-specific data.
     */
    UserDetailsService userDetailService();
}
