package de.semesterprojekt.quiz.websocket.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Getter;
import lombok.Setter;

/**
 * The class represents an answer message with the user from a user
 */
@Getter
@Setter
public class UserAnswerMessage extends AnswerMessage {

    private User user;

    public UserAnswerMessage(User user, String answer)
    {
        super(answer);
        this.user = user;
    }
}
