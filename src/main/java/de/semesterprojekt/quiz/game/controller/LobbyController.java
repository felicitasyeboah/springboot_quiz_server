package de.semesterprojekt.quiz.game.controller;

import de.semesterprojekt.quiz.database.controller.PlayedGameController;
import de.semesterprojekt.quiz.database.repository.PlayedGameRepository;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;
import de.semesterprojekt.quiz.websocket.model.IncomingWebSocketMessage;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.security.jwt.JwtTokenProvider;
import de.semesterprojekt.quiz.database.utility.QuestionRandomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import java.security.Principal;
import java.util.*;

@Controller
public class LobbyController implements Observer{

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebsocketMessageSender messageSender;

    @Autowired
    QuestionRandomizer questionRandomizer;

    @Autowired
    PlayedGameController playedGameController;

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

            //Add game over observer
            newGame.addObserver(this);

            //Add the Game to the gameList
            gameList.add(newGame);

            //Start the game
            GameThread newGameThread = new GameThread(newGame, messageSender, playedGameController);
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

        if(!userList.contains(user)) {

            //Store username and token in userTokenMap
            userTokenMap.put(username, token);

            //Store user in userList
            userList.add(user);

            //Print the connected users
            printConnectedUsers();

            //Check for player match
            checkMatch();

        }
        //TODO: SEND ERROR MESSAGE USER IS ALREADY LOGGED IN
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

        removeUser(user);

        //Print the connected users
        printConnectedUsers();
    }

    /**
     * Method removes a user from the userlist and deletes a key-value-pair in the userTokenMap
     */
    public void removeUser(User user) {
        //Delete username and token from userTokenMap
        if(userTokenMap.containsKey(user.getUserName())) {
            userTokenMap.remove(user.getUserName());
        }

        //Delete User from userList
        if(userList.contains(user)) {
            userList.remove(user);
        }
    }

    /**
     * Method removes a game from the game list
     * @param game
     */
    public void removeGame(Game game) {

        //Remove the user from the lists
        removeUser(game.getUser1());
        removeUser(game.getUser2());

        //Remove the game
        if(gameList.contains(game)) {
            gameList.remove(game);
        }
    }


    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof String) {

            //If the message is NEW_MESSAGE
            if(((String) arg).equals("GAME_OVER")) {

                //Get the game instance
                Game game = (Game) o;

                //Delete all observers
                game.deleteObservers();

                //Remove the game from the list
                removeGame((Game) o);

                //Set game = null
                game = null;
            }
        }
    }
}