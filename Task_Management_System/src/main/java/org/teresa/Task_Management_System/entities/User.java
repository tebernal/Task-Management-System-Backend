package org.teresa.Task_Management_System.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.teresa.Task_Management_System.dto.UserDto;
import org.teresa.Task_Management_System.enums.UserRole;

import java.util.Collection;
import java.util.List;

/**
 * User Entity represents a user in the system.
 */
@Data
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the user

    private String email; // Email address of the user

    private String password; // Password of the user (hashed or encrypted)

    private UserRole userRole; // Role of the user in the system

    /**
     * Retrieves the authorities (roles) granted to the user.
     *
     * @return a collection of GrantedAuthority objects representing the user's roles.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
    }

    /**
     * Retrieves the username used to authenticate the user.
     *
     * @return the username
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the user's account is valid, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the user's credentials are valid, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Retrieves a Data Transfer Object (DTO) representing the user.
     *
     * @return a UserDto object containing user data
     */
    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);
        userDto.setUserRole(userRole);
        return userDto;
    }
}
