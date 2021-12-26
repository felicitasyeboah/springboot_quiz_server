package de.semesterprojekt.quiz.security;

import lombok.Data;

/**
 * The class represents a user authentication request
 */
@Data
public class AuthRequest {

    private String userName;

    private String password;
}
