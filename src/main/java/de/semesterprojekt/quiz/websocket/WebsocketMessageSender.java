package de.semesterprojekt.quiz.websocket;

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

    /**
     * The method sends a websocket message to a specific user (by token)
     * @param token token of the user
     * @param message message
     */
    public void sendMessage(String token, String message, MessageType messageType) {

        Map<String, List<String>> nativeHeaders = new HashMap<>();
        nativeHeaders.put("type", Collections.singletonList(messageType.toString()));

        Map<String, Object> headers = new HashMap<>();
        headers.put(NativeMessageHeaderAccessor.NATIVE_HEADERS, nativeHeaders);

        //Send message to user
        template.convertAndSendToUser(token , "/topic/game", message, headers);
    }
}
