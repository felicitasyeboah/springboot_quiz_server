package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.database.controller.PlayedGameController;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;
import de.semesterprojekt.quiz.database.repository.UserRepository;
import de.semesterprojekt.quiz.security.jwt.JwtTokenProvider;
import de.semesterprojekt.quiz.database.utility.QuestionRandomizer;
import de.semesterprojekt.quiz.websocket.model.AnswerMessage;
import de.semesterprojekt.quiz.websocket.model.TokenMessage;
import de.semesterprojekt.quiz.websocket.model.UserAnswerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
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

    Gson gson = new Gson();

    //Map to store username and websocket uuid
    private Map<String, String> userUuidMap = new HashMap<>();
    private List<User> userList = new ArrayList<>();
    private List<Game> gameList = new ArrayList<>();

    /**
     * The Methods sends a message to the calling user
     * @param message
     * @param principal
     */
    @MessageMapping("/game")
    public void handleIncomingMessage(String message, Principal principal){

        //Try to get the token
        String token = gson.fromJson(message,TokenMessage.class).getToken();
        String uuid = principal.getName();
        User user = null;

        //Check the token
        if(token != null) {

            if(tokenProvider.validateToken(token)) {

                //Get the user
                Optional<User> userOptional = userRepository.findByUserName(tokenProvider.getUserNameFromToken(token));
                user = (User) userOptional.get();

                //Adds a user to the lobby and checks for a match
                addUserToLobby(user, uuid);

            } else {

                System.out.println("The token is not valid.");
            }
        }
        else {

            //Try to get an answer message
            String answer = gson.fromJson(message, AnswerMessage.class).getAnswer();

            if(answer != null) {

                //is the user available in the list
                if(userUuidMap.containsValue(uuid)) {

                    //Get the user from uuid
                    user = getUserFromUuid(uuid);

                    if(user != null) {

                        //Create a new IncomingMessage object
                        UserAnswerMessage newMessage = new UserAnswerMessage(user, answer);

                        //Find the current game of the calling user and add the message
                        getGame(user).addMessage(newMessage);
                    }
                } else {

                    System.out.println("User is not authenticated.");
                }

            } else {

                System.out.println("The websocket message is not valid.");
            }
        }
    }

    /**
     * The method adds an user to the lobby
     * @param user
     */
    private void addUserToLobby(User user, String uuid) {
        if(user != null) {

            //is the user already in the list?
            if(!userList.contains(user)) {

                //Store username and token in userTokenMap
                userUuidMap.put(user.getUserName(), uuid);

                //Store user in userList
                userList.add(user);

                //Print the connected users
                printConnectedUsers();

                //Check for player match
                checkMatch();
            }
        }
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

        //Get the calling user
        User user = getUserFromUuid(event.getUser().getName());

        //Get the game of the calling user (or null)
        Game game = getGame(user);

        //Check if the user is in a game
        if(game == null && user != null) {

            //Remove the user from the lists
            removeUser(getUserFromUuid(event.getUser().getName()));

            //Print the connected users
            printConnectedUsers();

        } else if (user != null) {

            //Tell the game that a user disconnected
            getGame(user).setDisconnected();
        }
    }

    /**
     * returns the current running game of the user
     */
    private Game getGame(User user) {

        //Find the current game of a user
        for(Game game : gameList) {

            if(game.getUser1().equals(user) || game.getUser2().equals(user)) {

                //return the game
                return game;
            }
        }

        //No game found
        return null;
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
            Game newGame = new Game(user1, userUuidMap.get(user1.getUserName()), user2, userUuidMap.get(user2.getUserName()), questionRandomizer);

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
     * Method removes a user from the userlist and deletes a key-value-pair from the userUuidMap
     */
    public void removeUser(User user) {

        if(user != null) {

            //Delete username and token from userUuidMap
            if(userUuidMap.containsKey(user.getUserName())) {
                userUuidMap.remove(user.getUserName());
            }

            //Delete User from userList
            if(userList.contains(user)) {
                userList.remove(user);
            }
        }
    }

    /**
     * Method removes a game from the game list
     * @param game
     */
    public void removeGame(Game game) {

        if(game != null) {

            //Remove the user from the lists
            removeUser(game.getUser1());
            removeUser(game.getUser2());

            //Print the connected users
            printConnectedUsers();

            //Remove the game
            if(gameList.contains(game)) {
                gameList.remove(game);
            }
        }
    }

    /**
     * Implementation of the Observer method "update"
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof String) {

            //If the message is NEW_MESSAGE
            if(((String) arg).equals("GAME_OVER")) {

                System.out.println("The current game is over.");

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

    /**
     * returns a User based on the uuid and the userUuidList
     * @param uuid
     * @return
     */
    private User getUserFromUuid(String uuid) {

        //Get the username and user
        for(Map.Entry<String,String> entry : userUuidMap.entrySet()) {

            if(entry.getValue().equals(uuid)) {

                //Get the user
                Optional<User> userOptional = userRepository.findByUserName(entry.getKey());
                return (User) userOptional.get();
            }
        }

        //No user found
        return null;
    }
}