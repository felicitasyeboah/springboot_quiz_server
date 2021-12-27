package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.message.*;
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

        //Start timer
        try {
            for (int timeLeft = GameConfig.DURATION_START; timeLeft > 0; timeLeft--) {

                //Create a timer-message and send it to the user
                StartTimerMessage newStartTimerMessage = new StartTimerMessage(timeLeft);
                messageSender.sendMessage(game.getTokenUser1(), newStartTimerMessage);
                messageSender.sendMessage(game.getTokenUser2(), newStartTimerMessage);

                Thread.sleep(1000);
            }
        } catch (Exception e) {

        }
        for(int currentRound = 0; currentRound < GameConfig.COUNT_QUESTION; currentRound++)
        {
            //Print the usernames
            System.out.println("Start game: " + game.getUser1().getUserName() + " vs. " + game.getUser2().getUserName());



            //Send the game-message to both users
            messageSender.sendMessage(game.getTokenUser1(), game.getGameMessage(game.getUser1(), currentRound));
            messageSender.sendMessage(game.getTokenUser2(), game.getGameMessage(game.getUser2(), currentRound));

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

            //Send the score-message to each user
            messageSender.sendMessage(game.getTokenUser1(),new ScoreMessage(game.getUser1(), game.getUser2(), game.getScoreUser1(), game.getScoreUser2()));
            messageSender.sendMessage(game.getTokenUser2(),new ScoreMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1()));

            //Score timer
            try {
                for (int timeLeft = GameConfig.DURATION_SCORE; timeLeft > 0; timeLeft--) {

                    //Create a timer-message and send it to the user
                    ScoreTimerMessage newScoreTimerMessage = new ScoreTimerMessage(timeLeft);
                    messageSender.sendMessage(game.getTokenUser1(), newScoreTimerMessage);
                    messageSender.sendMessage(game.getTokenUser2(), newScoreTimerMessage);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {

            }

        }

        //TODO: CHECK HIGHSCORES

        //Send a result-message to each user
        messageSender.sendMessage(game.getTokenUser1(),new ResultMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1(),false));
        messageSender.sendMessage(game.getTokenUser1(),new ResultMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1(),false));

        //Result timer
        try {
            for (int timeLeft = GameConfig.DURATION_RESULT; timeLeft > 0; timeLeft--) {

                //Create a timer-message and send it to the user
                ResultTimerMessage newResultTimerMessage = new ResultTimerMessage(timeLeft);
                messageSender.sendMessage(game.getTokenUser1(), newResultTimerMessage);
                messageSender.sendMessage(game.getTokenUser2(), newResultTimerMessage);

                Thread.sleep(1000);
            }
        } catch (Exception e) {

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
