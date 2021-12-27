package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.repository.PlayedGameRepository;
import de.semesterprojekt.quiz.game.model.message.*;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.model.IncomingWebSocketMessage;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;

import java.util.Observable;
import java.util.Observer;

/**
 * The class implements the game logic
 */
public class GameThread extends Thread implements Observer {

    private Game game;
    private WebsocketMessageSender messageSender;
    private PlayedGameRepository playedGameRepository;
    private Gson gson = new Gson();

    public GameThread(Game game, WebsocketMessageSender messageSender, PlayedGameRepository playedGameRepository){
        this.game = game;
        this.messageSender = messageSender;
        this.playedGameRepository = playedGameRepository;
    }

    /**
     * The method implements the game logic
     */
    @Override
    public void run() {

        //Print the usernames
        System.out.println("Start game: " + game.getUser1().getUserName() + " vs. " + game.getUser2().getUserName());

        //Start the blocking start timer
        GameTimer startTimer = new GameTimer(this.game, this.messageSender, MessageType.START_TIMER_MESSAGE, GameConfig.DURATION_START);
        startTimer.startBlocking();

        for(int currentRound = 0; currentRound < GameConfig.COUNT_QUESTION; currentRound++)
        {

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

            //Start the non-blocking question timer
            GameTimer questionTimer = new GameTimer(this.game, this.messageSender, MessageType.QUESTION_TIMER_MESSAGE, GameConfig.DURATION_QUESTION);
            questionTimer.start();

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

            //Interrupt the question timer
            questionTimer.interrupt();

            //Remove messageQueue-observer
            game.deleteObserver(this);

            //Send the score-message to each user
            messageSender.sendMessage(game.getTokenUser1(),new ScoreMessage(game.getUser1(), game.getUser2(), game.getScoreUser1(), game.getScoreUser2()));
            messageSender.sendMessage(game.getTokenUser2(),new ScoreMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1()));

            //Start the blocking score timer
            GameTimer scoreTimer = new GameTimer(this.game, this.messageSender, MessageType.SCORE_TIMER_MESSAGE, GameConfig.DURATION_SCORE);
            scoreTimer.startBlocking();
        }

        //TODO: STORE IN PLAYED GAME AND CHECK HIGHSCORES

        //Send a result-message to each user
        messageSender.sendMessage(game.getTokenUser1(),new ResultMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1(),false));
        messageSender.sendMessage(game.getTokenUser1(),new ResultMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1(),false));

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
