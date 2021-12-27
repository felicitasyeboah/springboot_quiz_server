package de.semesterprojekt.quiz.websocket.model;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.data.entity.User;
import lombok.Data;

/**
 * The class represents an incoming websocket message from a user
 */
@Data
public class IncomingWebSocketMessage {


    private User user;
    private String message;

    public IncomingWebSocketMessage(User user, String message)
    {
        this.user = user;

        //Get the Data with key "answer" -> if the key is no present, the field will be an empty string
        try {
            this.message = new Gson().fromJson(message, AnswerMessage.class).getAnswer();
        } catch (Exception e) {
            this.message = "";
        }
    }
}
