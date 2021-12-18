package de.semesterprojekt.quiz.websocket;

import de.semesterprojekt.quiz.controller.QuestionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    QuestionController questionController;

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public void getRandomQuestion(){

        System.out.println("Send question to user.");
        template.convertAndSend("/topic/game", questionController.getRandom());
    }
}
