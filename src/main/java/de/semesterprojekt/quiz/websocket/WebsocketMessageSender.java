package de.semesterprojekt.quiz.websocket;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.game.message.GenericMessage;
import de.semesterprojekt.quiz.game.message.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebsocketMessageSender {

    @Autowired
    SimpMessagingTemplate template;

    Gson gson = new Gson();

    /**
     * The method sends a websocket message to a specific user (by token)
     * @param token token of the user
     * @param message message
     */
    public void sendMessage(String token, GenericMessage message) {

        //Create a map and add the message-type header
        Map<String, List<String>> nativeHeaders = new HashMap<>();
        nativeHeaders.put("type", Collections.singletonList(message.getType().toString()));

        //Create a header
        Map<String, Object> headers = new HashMap<>();
        headers.put(NativeMessageHeaderAccessor.NATIVE_HEADERS, nativeHeaders);

        //Print send
        System.out.println("type: \"" + message.getType().toString() + "\", message: " + gson.toJson(message));

        //Send message to user
        template.convertAndSendToUser(token , "/topic/game", gson.toJson(message), headers);
    }
}
