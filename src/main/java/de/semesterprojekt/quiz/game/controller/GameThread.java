package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.controller.PlayedGameController;
import de.semesterprojekt.quiz.database.entity.PlayedGame;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.game.model.message.*;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.model.UserAnswerMessage;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * The class implements the game logic
 */
public class GameThread extends Thread implements Observer {

    private Game game;
    private WebsocketMessageSender messageSender;
    private PlayedGameController playedGameController;
    private Gson gson = new Gson();
    private boolean receiveMessages = false;

    //Create GameTimers that can be stopped from update()
    private GameTimer questionTimer;
    private GameTimer startTimer;
    private GameTimer scoreTimer;

    public GameThread(Game game, WebsocketMessageSender messageSender, PlayedGameController playedGameController){
        this.game = game;
        this.messageSender = messageSender;
        this.playedGameController = playedGameController;

        //Add an observer for new messages and disconnects
        this.game.addObserver(this);
    }

    /**
     * The method implements the game logic
     */
    @Override
    public void run() {

        //Print the usernames
        System.out.println("Start game: '" + game.getUser1().getUserName() + "' vs. '" + game.getUser2().getUserName() + "'");

        //Start the blocking start timer
        this.startTimer = new GameTimer(this.game, this.messageSender, MessageType.START_TIMER_MESSAGE, GameConfig.DURATION_START);
        this.startTimer.startBlocking();

        for(int currentRound = 0; currentRound < GameConfig.COUNT_QUESTION; currentRound++)
        {

            //Send the game-message to both users
            messageSender.sendMessage(game.getUuidUser1(), game.getGameMessage(game.getUser1(), currentRound));
            messageSender.sendMessage(game.getUuidUser2(), game.getGameMessage(game.getUser2(), currentRound));

            //Delete messages from the queue
            game.clearMessages();

            //Let new messages interrupt the game
            this.receiveMessages = true;

            //Start a timer
            long startTimeMillis = System.currentTimeMillis();
            long remainingTimeMillis = GameConfig.DURATION_QUESTION * 1000;

            //Has a user answered the question?
            boolean hasAnsweredUser1 = false;
            boolean hasAnsweredUser2 = false;

            //Start the non-blocking question timer
            this.questionTimer = new GameTimer(this.game, this.messageSender, MessageType.QUESTION_TIMER_MESSAGE, GameConfig.DURATION_QUESTION);
            this.questionTimer.start();

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
                        UserAnswerMessage message = game.getNextMessage();

                        //Submit the answer of user 1
                        if (game.getUser1().equals(message.getUser()) && !hasAnsweredUser1) {

                            game.submitAnswer(game.getUser1(), currentRound, message.getAnswer(), System.currentTimeMillis() - startTimeMillis);
                            hasAnsweredUser1 = true;
                            System.out.println("'" + game.getUser1().getUserName() + "' has chosen an answer.");
                        }

                        //Submit the answer of user 2
                        if (game.getUser2().equals(message.getUser()) && !hasAnsweredUser2) {

                            game.submitAnswer(game.getUser2(), currentRound, message.getAnswer(), System.currentTimeMillis() - startTimeMillis);
                            hasAnsweredUser2 = true;
                            System.out.println("'" + game.getUser2().getUserName() + "' has chosen an answer.");
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
            this.questionTimer.interrupt();

            //Don't let new messages interrupt the game
            this.receiveMessages = false;

            //Send the score-message to each user
            messageSender.sendMessage(game.getUuidUser1(),new ScoreMessage(game.getUser1(), game.getUser2(), game.getScoreUser1(), game.getScoreUser2()));
            messageSender.sendMessage(game.getUuidUser2(),new ScoreMessage(game.getUser2(), game.getUser1(), game.getScoreUser2(), game.getScoreUser1()));

            //Start the blocking score timer
            this.scoreTimer = new GameTimer(this.game, this.messageSender, MessageType.SCORE_TIMER_MESSAGE, GameConfig.DURATION_SCORE);
            this.scoreTimer.startBlocking();
        }

        //Store the played game and store the isHighscore values
        Map<User,Boolean> isHighscore = playedGameController.submitPlayedGame(new PlayedGame(game.getUser1(),game.getUser2(),game.getScoreUser1(),game.getScoreUser2()));

        //Get the winner (same scores -> null-object)
        User winner = null;
        if(game.getScoreUser1() > game.getScoreUser2()) {
            winner = game.getUser1();
        } else if(game.getScoreUser2() > game.getScoreUser1()) {
            winner = game.getUser2();
        }

        //Send a result-message to each user
        messageSender.sendMessage(game.getUuidUser1(),new ResultMessage(game.getUser1(), game.getUser2(), winner, game.getScoreUser1(), game.getScoreUser2(),isHighscore.get(game.getUser1())));
        messageSender.sendMessage(game.getUuidUser2(),new ResultMessage(game.getUser2(), game.getUser1(), winner, game.getScoreUser2(), game.getScoreUser1(),isHighscore.get(game.getUser2())));

        //Set Game over to notify the lobby
        game.setGameOver();
    }

    /**
     * The method stops the current thread and all timers
     */
    private void stopAll(){

        //Stop the current startTimer
        if(this.startTimer != null) {
            this.startTimer.stop();
        }

        //Stop the current questionTimer
        if(this.questionTimer != null) {
            this.questionTimer.stop();
        }

        //Stop the current scoreTimer
        if(this.scoreTimer != null) {
            this.scoreTimer.stop();
        }

        //Stop the current GameThread
        this.stop();
    }

    /**
     * The method implements the update method of Observer
     * @param o Observable
     * @param arg Arguments
     */
    @Override
    public void update(Observable o, Object arg) {

        if(arg instanceof String) {

            //If the message is NEW_MESSAGE
            if(arg.equals("NEW_MESSAGE") && this.receiveMessages) {

                //Interrupt the sleep() for handling the incoming messages)
                this.interrupt();
            }

            //If the message is USER_DISCONNECTED
            if(arg.equals("USER_DISCONNECTED")) {

                System.out.println("User disconnected during game.");

                //Send a disconnect message to both users (only one is required)
                messageSender.sendMessage(game.getUuidUser1(),new DisconnectMessage());
                messageSender.sendMessage(game.getUuidUser2(),new DisconnectMessage());

                //Set Game over to notify the lobby
                game.setGameOver();

                //Stop the current game thread and all timers
                stopAll();
            }
        }
    }
}