package org.teresa.Task_Management_System.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.teresa.Task_Management_System.enums.UserRole;
import org.teresa.Task_Management_System.services.UserServices;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * WebSecurityConfiguration class configures Spring Security for the application.
 * It defines security rules, authentication providers, password encryption, and JWT authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserServices userServices;

    /**
     * Configures the security filter chain for HTTP requests.
     * @param http HttpSecurity instance to configure security settings
     * @return SecurityFilterChain configured with the specified rules
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/auth/**").permitAll() // Permit access to authentication endpoints
                        .requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name()) // Require ADMIN role for admin endpoints
                        .requestMatchers("/api/employee/**").hasAnyAuthority(UserRole.EMPLOYEE.name()) // Require EMPLOYEE role for employee endpoints
                        .anyRequest().authenticated()) // Require authentication for all other requests
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS)) // Configure session management as STATELESS
                .authenticationProvider(authenticationProvider()) // Set custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
        return http.build();
    }

    /**
     * Configures the password encoder used for encoding and decoding passwords.
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the authentication provider with custom user details service and password encoder.
     * @return AuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userServices.userDetailService()); // Set custom user details service
        authProvider.setPasswordEncoder(passwordEncoder()); // Set password encoder
        return authProvider;
    }

    /**
     * Retrieves the AuthenticationManager bean from the AuthenticationConfiguration.
     * @param config AuthenticationConfiguration instance
     * @return AuthenticationManager instance
     * @throws Exception if an error occurs while retrieving the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
