package org.teresa.Task_Management_System.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.teresa.Task_Management_System.services.UserServices;
import org.teresa.Task_Management_System.utils.JwtUtil;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a custom filter responsible for validating JWT tokens
 * and setting up Spring Security authentication based on the token information.
 * It intercepts incoming requests, extracts the JWT token from the Authorization header,
 * validates it, and sets up the Spring Security context with the authenticated user details.
 */
@Component
@RequiredArgsConstructor
public class
JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserServices userServices;

    /**
     * Overrides the doFilterInternal method to implement JWT authentication logic.
     * This method intercepts incoming requests, extracts and validates JWT tokens,
     * and sets up Spring Security authentication based on the token information.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Extract the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // If the Authorization header is missing or doesn't start with "Bearer ", continue to the next filter
        if(StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        jwt = authHeader.substring(7);

        // Extract the user email from the JWT token
        userEmail = jwtUtil.extractUserName(jwt);

        // If the user email is not empty and there is no existing authentication in the SecurityContextHolder
        if(StringUtils.isNotEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
            // Load user details by email using the userDetailsService
            UserDetails userDetails = userServices.userDetailService().loadUserByUsername(userEmail);

            // If the JWT token is valid for the user details
            if(jwtUtil.isTokenValid(jwt, userDetails)){
                // Create an authentication token with the user details and set it in the SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request,response);
    }
}
