package de.semesterprojekt.quiz.websocket;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.controller.QuestionController;
import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.model.GameMessage;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.model.ResponseMessage;
import de.semesterprojekt.quiz.security.CustomUserDetailsService;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import de.semesterprojekt.quiz.utility.GameFactory;
import nonapi.io.github.classgraph.json.JSONSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    QuestionController questionController;

    @Autowired
    GameFactory gamefactory;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @MessageMapping("/public-game")
    @SendTo("/topic/game")
    public void getGameMessage(@Header("token") String token, @Header("simpSessionId") String sessionId, @RequestParam String message){
        if(tokenProvider.validateToken((token))) {
            System.out.println("Valid token");
            System.out.println("Message from: " + tokenProvider.getUserNameFromToken(token));
            System.out.println("sessionID: " + sessionId);
            System.out.println("Message: " + message);
        } else {
            System.out.println("Invalid token. Please log in again.");
        }
        //template.convertAndSend("/topic/game", questionController.getRandom());

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

    @MessageMapping("/game")
    //@SendToUser("/topic/game")
    public void getPrivateGameMessage(SimpMessageHeaderAccessor headerAccessor, @Header("token") String token, @RequestParam String message, Principal principal){

        //Get sessionId from the request
        String sessionId = principal.getName();

        if(tokenProvider.validateToken(token)) {
            System.out.println("Message from: " + tokenProvider.getUserNameFromToken(token));
            System.out.println("JWT: valid");
            System.out.println("sessionID: " + sessionId);
            System.out.println("Message: " + message);
        } else {
            System.out.println("User has an invalid token.");
        }

        //TEST DATA
        User user1 = new User();
        user1.setUserName("Bernd");

        User user2 = new User();
        user2.setUserName("Beate");

        Game newGame = gamefactory.createGame(user1, user2);

        //Get the gameMessage for user 1 for the first round
        GameMessage newGameMessage = newGame.getGameMessage(user1, 0);

        Gson gson = new Gson();
        String object = gson.toJson(newGameMessage);

        template.convertAndSendToUser(sessionId, "/topic/game", object);
    }

    /**
     * The Eventlistener is called when a client connects
     * @param event connect event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {

        System.out.println("User \"" + getUserFromHeaderString(event.getMessage().getHeaders().toString())+ "\" connected. Session ID: " + event.getUser());

    }

    /**
     * The Eventlistener is called when a client disconnects
     * @param event disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        System.out.println("User \"" + getUserFromHeaderString(event.getMessage().getHeaders().toString())+ "\" disconnected. Session ID: " + event.getUser());

    }

    /**
     * The method extracts the token out of the event header
     * @param headerString header String
     * @return token
     */
    private String getTokenFromHeaderString(String headerString) {

        String token = headerString.substring(headerString.indexOf("token=") + 7,headerString.indexOf("token=") + 186);

        return token;
    }

    /**
     * The method extracts the username out of the event header
     * @param headerString header String
     * @return username
     */
    private String getUserFromHeaderString(String headerString) {

        String token = getTokenFromHeaderString(headerString);
        if(tokenProvider.validateToken(token)) {
            return tokenProvider.getUserNameFromToken(token);
        } else {
            return null;
        }
    }
}
