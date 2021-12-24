package de.semesterprojekt.quiz.gamelogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketMessageSender {

    @Autowired
    SimpMessagingTemplate template;

    /**
     * The method sends a websocket message to a specific user (by token)
     * @param token token of the user
     * @param message message
     */
    public void sendMessage(String token, String message) {

        //Send message to user
        template.convertAndSendToUser(token , "/topic/game", message);
    }
}
