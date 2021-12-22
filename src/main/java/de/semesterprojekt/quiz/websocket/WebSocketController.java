package de.semesterprojekt.quiz.websocket;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.model.GameMessage;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import de.semesterprojekt.quiz.utility.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.security.Principal;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    GameFactory gamefactory;

    @Autowired
    JwtTokenProvider tokenProvider;

    @MessageMapping("/game")
    public void getGameMessage(String message, Principal principal){

        //Get sessionId from the request
        String sessionToken = principal.getName().toString();
        String username = tokenProvider.getUserNameFromToken(sessionToken);

        System.out.println("Message from: " + username);
        System.out.println("Token: " + sessionToken);
        System.out.println("Message: " + message);
        System.out.println("Send the following data to " + username + "\n");
        System.out.println(getTestGameMessage());

        //Send Data to user
        template.convertAndSendToUser(sessionToken , "/topic/game", getTestGameMessage());
    }

    /**
     * Method for empty messages
     * @param principal
     */
    @MessageMapping("/getTestData")
    public void getTestData(Principal principal) {

        this.getGameMessage("", principal);
    }

    /**
     * The Eventlistener is called when a client connects
     * @param event connect event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {

        System.out.println("User \"" + tokenProvider.getUserNameFromToken(event.getUser().toString()) + "\" connected.");

    }

    /**
     * The Eventlistener is called when a client disconnects
     * @param event disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        System.out.println("User \"" + tokenProvider.getUserNameFromToken(event.getUser().toString()) + "\" disconnected.");

    }

    /**
     * The method returns a test object
     * @return test object
     */
    private String getTestGameMessage() {

        //TEST DATA
        User user1 = new User();
        user1.setUserName("Bernd");

        User user2 = new User();
        user2.setUserName("Beate");

        Game newGame = gamefactory.createGame(user1, user2);

        //Get the gameMessage for user 1 for the first round
        GameMessage newGameMessage = newGame.getGameMessage(user1, 0);

        Gson gson = new Gson();

        return gson.toJson(newGameMessage);
    }
}