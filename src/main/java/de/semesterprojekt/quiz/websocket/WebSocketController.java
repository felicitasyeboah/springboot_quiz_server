package de.semesterprojekt.quiz.websocket;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.model.Game;
import de.semesterprojekt.quiz.model.GameMessage;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.repository.UserRepository;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import de.semesterprojekt.quiz.utility.GameFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.security.Principal;
import java.util.Optional;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    GameFactory gamefactory;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @MessageMapping("/game")
    public void getGameMessage(String message, Principal principal){

        //Get sessionId from the request
        String sessionToken = principal.getName().toString();
        String username = tokenProvider.getUserNameFromToken(sessionToken);

        System.out.println("Message from: " + username);
        System.out.println("Token: " + sessionToken);
        System.out.println("Message: " + message);

        //5s Timer
        int delay = 5;
        Thread sendMessage = new Thread() {
            @Override
            public void run(){
                try {

                    //Kannst du auskommentieren :)
                    for(int i = delay; i>0 ; i--) {
                        System.out.println("Wait for another player... " + i + "s left.");
                        sleep(1000);
                    }

                    System.out.println("Send the following data to " + username + "\n");

                    //Get the User and create the test GameMessage
                    Optional<User> userOptional = userRepository.findByUserName(username);
                    User user = (User) userOptional.get();
                    String testGameMessage = getTestGameMessage(user);

                    //Print the data
                    System.out.println(testGameMessage);

                    //Send Data to user
                    template.convertAndSendToUser(sessionToken , "/topic/game", testGameMessage);

                } catch (Exception e) {
                }
            }
        };
        sendMessage.run();
    }

    /**
     * The Eventlistener is called when a client connects
     * @param event connect event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {

        //Get the user
        User user = getUserFromUsername(tokenProvider.getUserNameFromToken(event.getUser().toString()));

        //Set isReady status to true
        user.setReady(true);

        //Store the object
        userRepository.save(user);

        //Print the message
        System.out.println("User \"" + user.getUserName() + "\" connected. Changed isReady to: " + user.isReady());
    }

    /**
     * The Eventlistener is called when a client disconnects
     * @param event disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        //Get the user
        User user = getUserFromUsername(tokenProvider.getUserNameFromToken(event.getUser().toString()));

        //Set isReady status to true
        user.setReady(false);

        //Store the object
        userRepository.save(user);

        //Print the message
        System.out.println("User \"" + user.getUserName() + "\" disconnected. Changed isReady to: " + user.isReady());

    }

    /**
     *
     */
    private User getUserFromUsername(String username) {
        return (User) userRepository.findByUserName(username).get();
    }

    /**
     * The method returns a test object
     * @return test object
     */
    private String getTestGameMessage(User user1) {

        //TEST DATA
        //User user1 = new User();
        //user1.setUserName("Bernd");

        User user2 = new User();
        user2.setUserName("Beate");

        Game newGame = gamefactory.createGame(user1, user2);

        //Get the gameMessage for user 1 for the first round
        GameMessage newGameMessage = newGame.getGameMessage(user1, 0);

        Gson gson = new Gson();

        return gson.toJson(newGameMessage);
    }
}