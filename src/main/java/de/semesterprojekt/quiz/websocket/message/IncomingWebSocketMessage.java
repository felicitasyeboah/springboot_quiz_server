package de.semesterprojekt.quiz.websocket.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.model.ResponseMessage;
import lombok.AllArgsConstructor;
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
        this.message = new Gson().fromJson(message, AnswerMessage.class).getAnswer();
    }

}
