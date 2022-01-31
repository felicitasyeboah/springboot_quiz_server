package de.semesterprojekt.quiz.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The class represents an answer message from a user
 */
@Data
@AllArgsConstructor
public class AnswerMessage {

    private String answer;
}
