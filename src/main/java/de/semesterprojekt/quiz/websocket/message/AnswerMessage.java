package de.semesterprojekt.quiz.websocket.message;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The class represents an answer object from a user
 */
@Data
@AllArgsConstructor
public class AnswerMessage {

    private String answer;
}
