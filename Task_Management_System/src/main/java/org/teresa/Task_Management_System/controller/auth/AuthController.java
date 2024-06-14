package org.teresa.Task_Management_System.controller.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.teresa.Task_Management_System.dto.AuthenticationRequest;
import org.teresa.Task_Management_System.dto.AuthenticationResponse;
import org.teresa.Task_Management_System.dto.SignupRequest;
import org.teresa.Task_Management_System.dto.UserDto;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.repositories.UserRepository;
import org.teresa.Task_Management_System.services.AuthService;
import org.teresa.Task_Management_System.services.UserServices;
import org.teresa.Task_Management_System.utils.JwtUtil;

import java.util.Optional;

/**
 * AuthController handles user authentication-related HTTP requests.
 * It provides endpoints for user signup and login.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserServices userServices;
    private final AuthenticationManager authenticationManager;

    /**
     * Endpoint for user signup.
     * @param signupRequest SignupRequest object containing user details
     * @return ResponseEntity containing the created UserDto object if successful, or an error message if failed
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){
        // Check if user with the given email already exists
        if(authService.hasUserWithEmail(signupRequest.getEmail()))
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exists with this email");

        // Call the signup service to create a new user
        UserDto createdUserDto = authService.signupUser(signupRequest);

        // If user creation fails, return error response
        if(createdUserDto == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not created");

        // Return success response with the created user details
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    /**
     * Endpoint for user login.
     * @param authenticationRequest AuthenticationRequest object containing user credentials
     * @return AuthenticationResponse object containing JWT token and user details upon successful login
     */
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest){
        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getEmail(),
                    authenticationRequest.getPassword()));
        }catch (BadCredentialsException e){
            // If authentication fails, throw exception with appropriate message
            throw new BadCredentialsException("Incorrect username or password");
        }

        // Load user details from the user service
        final UserDetails userDetails = userServices.userDetailService().loadUserByUsername(authenticationRequest.getEmail());

        // Find the user by email in the repository
        Optional<User> optionalUser =  userRepository.findFirstByEmail(authenticationRequest.getEmail());

        // Generate JWT token
        final String jwtToken = jwtUtil.generateToken(userDetails);

        // Create authentication response
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        // If user is found, set JWT token and user details in the response
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }
}
