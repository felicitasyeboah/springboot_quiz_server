package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.message.MessageType;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.message.IncomingWebSocketMessage;
import de.semesterprojekt.quiz.websocket.WebsocketMessageSender;

import java.util.Observable;
import java.util.Observer;

/**
 * The class implements the game logic
 */
public class GameThread extends Thread implements Observer {

    private Game game;
    private WebsocketMessageSender messageSender;
    private Gson gson = new Gson();

    public GameThread(Game game, WebsocketMessageSender messageSender){
        this.game = game;
        this.messageSender = messageSender;
    }

    /**
     * The method implements the game logic
     */
    @Override
    public void run() {

        for(int currentRound = 0; currentRound < GameConfig.COUNT_QUESTION; currentRound++)
        {
            //Print the usernames
            System.out.println("Start game: " + game.getUser1().getUserName() + " vs. " + game.getUser2().getUserName());

            //Break timer
            try {
                for (int i = GameConfig.DURATION_BREAK; i > 0; i--) {
                    System.out.println("Break timer: " + i + " seconds left.");

                    //TODO: SEND TIMER MESSAGE

                    Thread.sleep(1000);
                }
            } catch (Exception e) {

            }

            //Print the question of the current round
            System.out.println("Question " + (currentRound + 1) + ": " + game.getQuestion(currentRound).getQuestionText());

            //Send the question to both users
            messageSender.sendMessage(game.getTokenUser1(), gson.toJson(game.getGameMessage(game.getUser1(), currentRound)), MessageType.GAME_MESSAGE);
            messageSender.sendMessage(game.getTokenUser2(), gson.toJson(game.getGameMessage(game.getUser2(), currentRound)), MessageType.GAME_MESSAGE);

            //Delete messages from the queue
            game.clearMessages();

            //Add messageQueue-observer
            game.addObserver(this);

            //Start a timer
            long startTimeMillis = System.currentTimeMillis();
            long remainingTimeMillis = GameConfig.DURATION_QUESTION * 1000;

            //Has a user answered the question?
            boolean hasAnsweredUser1 = false;
            boolean hasAnsweredUser2 = false;

            //Start the client timer
            GameTimer clientTimer = new GameTimer(this.game, this.messageSender);
            clientTimer.start();

            //Start the timer loop
            do {
                try {

                    //Wait the remaining time
                    Thread.sleep(remainingTimeMillis);

                    //Set it to 0 afterwards
                    remainingTimeMillis = 0;

                } catch (InterruptedException e) {

                    //Check if there's a message
                    if (game.isNextMessage()) {
                        IncomingWebSocketMessage message = game.getNextMessage();

                        //Submit the answer of user 1
                        if (game.getUser1().equals(message.getUser()) && !hasAnsweredUser1) {

                            game.submitAnswer(game.getUser1(), currentRound, message.getMessage(), System.currentTimeMillis() - startTimeMillis);
                            hasAnsweredUser1 = true;
                            System.out.println(game.getUser1().getUserName() + " has chosen an answer.");
                        }

                        //Submit the answer of user 2
                        if (game.getUser2().equals(message.getUser()) && !hasAnsweredUser2) {

                            game.submitAnswer(game.getUser2(), currentRound, message.getMessage(), System.currentTimeMillis() - startTimeMillis);
                            hasAnsweredUser2 = true;
                            System.out.println(game.getUser2().getUserName() + " has chosen an answer.");
                        }
                    }

                    //Calculate the new remaining time
                    remainingTimeMillis = GameConfig.DURATION_QUESTION * 1000 - (System.currentTimeMillis() - startTimeMillis);

                    //When both players answered, the remaining time will be set to 0
                    if (hasAnsweredUser1 && hasAnsweredUser2) {
                        remainingTimeMillis = 0;
                    }
                }
            } while (remainingTimeMillis > 0);

            //Interrupt the Client Timer
            clientTimer.interrupt();

            //Remove messageQueue-observer
            game.deleteObserver(this);

            //TODO: Send a scoreMessage to the users

            //Print timer-is-over message
            System.out.println("First round is over.");

            //Print the points of the users
            System.out.println(game.getUser1().getUserName() + " got " + game.getScoreUser1() + " points.");
            System.out.println(game.getUser2().getUserName() + " got " + game.getScoreUser2() + " points.");
        }

        //Print message
        System.out.println("Please restart the server for further testing");

        //TODO: repeat the round, set scores, set the highscore at the end
        //TODO: delete game from gamelist, and users from userlist
    }

    /**
     * The method implements the update method of Observer
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

        //Interrupt the sleep() for handling the incoming messages
        this.interrupt();
    }


}
