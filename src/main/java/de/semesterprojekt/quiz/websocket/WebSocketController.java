package de.semesterprojekt.quiz.websocket;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.game.controller.GameThread;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.game.message.GameMessage;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.websocket.message.IncomingWebSocketMessage;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.security.JwtTokenProvider;
import de.semesterprojekt.quiz.utility.QuestionRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.security.Principal;
import java.util.*;

@Controller
public class WebSocketController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebsocketMessageSender messageSender;

    @Autowired
    QuestionRandomizer questionRandomizer;

    //Map to store username and token
    private Map<String, String> userTokenMap = new HashMap<>();
    private List<User> userList = new ArrayList<>();
    private List<Game> gameList = new ArrayList<>();

    /**
     * The Methods sends a message to the calling user
     * @param message
     * @param principal
     */
    @MessageMapping("/game")
    public void handleIncomingMessage(String message, Principal principal){

        //Get the user from the message
        String token = principal.getName().toString();
        String userName = tokenProvider.getUserNameFromToken(token);
        Optional<User> userOptional = userRepository.findByUserName(userName);
        User user = (User) userOptional.get();

        //Create a new IncomingMessage object
        IncomingWebSocketMessage newMessage = new IncomingWebSocketMessage(user, message);

        //Find the current game of the calling user
        for(Game game : gameList) {

            if(game.getUser1().getUserName().equals(userName) || game.getUser2().getUserName().equals(userName)) {

                //Add the message to the queue
                game.addMessage(newMessage);
                break;
            }
        }

        //System.out.println("Message from: " + username);
        //System.out.println("Message: " + message);

        //System.out.println("Send the following data to \"" + username + "\"");

        //Get the User and create the test GameMessage
        //Optional<User> userOptional = userRepository.findByUserName(username);
        //User user = (User) userOptional.get();
        //String testGameMessage = getTestGameMessage(user, token);

        //Print the data
        //System.out.println(testGameMessage);

        //Send Data to user
        //template.convertAndSendToUser(token , "/topic/game", testGameMessage);
    }

    /**
     * The method prints the connected users that are ready to play
     */
    private void printConnectedUsers() {

        //Set the userListSize
        int userListSize = userList.size();

        //Print the connected usernames
        System.out.print("Connected Users (" + userListSize + "): ");
        for(int i = userListSize; i > 0; i--) {

            //Print a username
            System.out.print("\"" + userList.get(userListSize - i).getUserName() + "\"");
            if(i > 1) {
                System.out.print(", ");
            } else {
                System.out.println("");
            }
        }
        if(userListSize == 0) {
            System.out.println("");
        }
    }

    /**
     * The method checks if two new players are ready to play and starts a game with them
     */
    private void checkMatch() {

        //Set the userListSize
        int userListSize = userList.size();

        //Try to match players, when there are sets of two ready to play
        if(userListSize % 2 == 0 && userListSize > 0) {

            //Get the users
            User user1 = userList.get(userListSize - 2);
            User user2 = userList.get(userListSize - 1);

            //Create the game
            Game newGame = new Game(user1, userTokenMap.get(user1.getUserName()), user2, userTokenMap.get(user2.getUserName()), questionRandomizer);

            //Add the Game to the gameList
            gameList.add(newGame);

            //Start the game
            GameThread newGameThread = new GameThread(newGame, messageSender);
            newGameThread.start();
        }
    }

    /**
     * The Eventlistener is called when a client connects
     * - puts the username and token on the userTokenMap
     * - adds the User to the userList
     * - print a connect message
     * - call checkMatch()
     * @param event connect event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionSubscribeEvent event) {

        //TODO: Block a double login

        //Get the token
        String token = event.getUser().toString();

        //Get the username
        String username = tokenProvider.getUserNameFromToken(token);

        //Get the user
        User user = (User) userRepository.findByUserName(username).get();

        //Store username and token in userTokenMap
        userTokenMap.put(username, token);

        //Store user in userList
        userList.add(user);

        //Print the connected users
        printConnectedUsers();

        //Check for player match
        checkMatch();
    }

    /**
     * The Eventlistener is called when a client disconnects
     * - removes the username and token from the userTokenMap
     * - removes the User from the userList
     * - print a disconnect message
     * @param event disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        //Get the token
        String token = event.getUser().toString();

        //Get the username
        String username = tokenProvider.getUserNameFromToken(token);

        //Get the user
        User user = (User) userRepository.findByUserName(username).get();

        //Delete username and token from userTokenMap
        if(userTokenMap.containsKey(username)) {
            userTokenMap.remove(username);
        }

        //Delete User from userList
        if(userList.contains(user)) {
            userList.remove(user);
        }

        //Print the connected users
        printConnectedUsers();
    }

    /**
     * The method returns a stringified test object of the type GameMessage
     * @return test object
     */
    private String getTestGameMessage(User user1, String tokenUser1) {

        //TEST DATA
        User user2 = new User();
        user2.setUserName("Beate");
        String tokenUser2 = "testToken";

        //Create test data with user1 (real user) and user2 (test user)
        Game newGame = new Game(user1, tokenUser1, user2, tokenUser2, questionRandomizer);

        //Get the gameMessage for user 1 for the first round
        GameMessage newGameMessage = newGame.getGameMessage(user1, 0);

        //Create a new Gson object
        Gson gson = new Gson();

        //format the object and return it
        return gson.toJson(newGameMessage);
    }
}