package de.semesterprojekt.quiz.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The class represents a token message from a user
 */
@Data
@AllArgsConstructor
public class TokenMessage {

    private String token;
}