package org.teresa.Task_Management_System.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.teresa.Task_Management_System.entities.User;
import org.teresa.Task_Management_System.repositories.UserRepository;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

/**
 * This class provides utility methods for JWT (JSON Web Token) generation, validation,
 * and extraction of user details from the token. It handles the creation of JWT tokens,
 * verification of token validity, and extraction of user details from the SecurityContextHolder.
 */
@Component
@RequiredArgsConstructor
public class JwtUtil {
    /**
     * Secret key used for JWT token signing and verification.
     */
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    /**
     * Repository for accessing user data from the database.
     */
    private final UserRepository userRepository;
    /**
     * Generates a JWT token based on the UserDetails.
     * @param userDetails The UserDetails object representing the user.
     * @return The generated JWT token.
     */
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    private String generateToken(Map<String, Objects> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Retrieves the signing key used for JWT token generation.
     * @return The signing key.
     */
    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Validates if the given JWT token is valid for the specified UserDetails.
     * @param token The JWT token to validate.
     * @param userDetails The UserDetails object representing the user.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    /**
     * Extracts the username from the JWT token.
     * @param token The JWT token from which to extract the username.
     * @return The username extracted from the token.
     */
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if the JWT token has expired.
     * @param token The JWT token to check for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
       return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date extracted from the token.
     */

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolvers The function to resolve the claim.
     * @param <T> The type of the claim.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers){
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     * @param token The JWT token from which to extract all claims.
     * @return The extracted claims.
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the logged-in user from the SecurityContextHolder.
     * @return The logged-in user.
     */
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
             Optional<User> optionalUser = userRepository.findById(user.getId());
            return optionalUser.orElse(null);
        }
        return null;
    }

}
