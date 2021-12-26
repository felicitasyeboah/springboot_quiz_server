package de.semesterprojekt.quiz.websocket.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

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
        Map<String,String> newMap = new HashMap<>();
        newMap.put("type","gameMessage");

        //Send message to user
        template.convertAndSendToUser(token , "/topic/game", message);
    }
}
