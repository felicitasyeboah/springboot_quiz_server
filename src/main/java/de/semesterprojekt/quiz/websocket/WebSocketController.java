package de.semesterprojekt.quiz.websocket;

import de.semesterprojekt.quiz.controller.QuestionController;
import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.model.GameMessage;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.utility.GameFactory;
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

    @Autowired
    private GameFactory gamefactory;

    @MessageMapping("/game")
    @SendTo("/topic/game")
    public void getGameMessage(){


        //template.convertAndSend("/topic/game", questionController.getRandom());
        System.out.println("Send GameMessage to user.");

        //TEST DATA
        User user1 = new User();
        user1.setUserName("Bernd");

        User user2 = new User();
        user2.setUserName("Beate");

        Game newGame = gamefactory.createGame(user1, user2);

        //Get the gameMessage for user 1 for the first round
        GameMessage newGameMessage = newGame.getGameMessage(user1, 0);

        template.convertAndSend("/topic/game", newGameMessage);

    }
}
