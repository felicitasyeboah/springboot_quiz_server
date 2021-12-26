package de.semesterprojekt.quiz.websocket.message;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The class represents an incoming websocket message from a user
 */
@Data
@AllArgsConstructor
public class IncomingWebSocketMessage {

    private User user;
    private String message;

}
