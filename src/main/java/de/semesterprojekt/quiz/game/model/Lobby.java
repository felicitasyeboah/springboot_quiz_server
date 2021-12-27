package de.semesterprojekt.quiz.game.model;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.utility.QuestionRandomizer;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;
import de.semesterprojekt.quiz.websocket.model.IncomingWebSocketMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {

    //TODO: CUT LOGIC FROM LobbyController.class AND IMPLEMENT IT HERE

    //Map to store username and token
    private Map<String, String> userTokenMap;

    //Store the users
    private List<User> userList;

    //Store the games of the lobby
    private List<Game> gameList;

    //Stores an instance of the WebsocketMessageSender
    WebsocketMessageSender messageSender;

    //Stores an instance of the QuestionRandomizer
    QuestionRandomizer questionRandomizer;

    public Lobby(WebsocketMessageSender messageSender, QuestionRandomizer questionRandomizer) {
        this.messageSender = messageSender;
        this.questionRandomizer = questionRandomizer;
        this.userTokenMap = new HashMap<>();
        this.userList = new ArrayList<>();
        this.gameList = new ArrayList<>();
    }

    /**
     * Method takes a IncomingWebsocketMessage and forwards it to the correct game
     */
    public void forwardMessage(IncomingWebSocketMessage message) {

    }

    /**
     * Method prints all connected users
     */
    public void printConnectedUsers() {

    }

    /**
     * Method adds a user to the userlist and add a key-value-pair to the userTokenMap
     */
    public void addUser(User user, String token) {

        matchAndStartGame();
    }

    /**
     * Method removes a user from the userlist and deletes a key-value-pair in the userTokenMap
     */
    public void removeUser(User user) {

    }

    /**
     * The method checks if there are two new ready users and starts a new game instance with them
     */
    private void matchAndStartGame() {

    }
}