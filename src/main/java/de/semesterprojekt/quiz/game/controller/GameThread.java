package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.message.IncomingWebSocketMessage;
import de.semesterprojekt.quiz.websocket.message.WebsocketMessageSender;

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
    public void run() {

        //Add messageQueue-observer
        game.addObserver(this);

        //Print the usernames
        System.out.println("Start game: " + game.getUser1().getUserName() + " vs. " + game.getUser2().getUserName());

        //BREAK_DURATION
        try {
            System.out.println("Wait " + GameConfig.DURATION_BREAK + "millis");
            Thread.sleep(GameConfig.DURATION_BREAK);
        } catch (Exception e) {

        }





        int currentRound = 0;

        //Send the first GameMessage to both users
        System.out.println("First question: " + game.getQuestion(0).getQuestionText());
        messageSender.sendMessage(game.getTokenUser1(),gson.toJson(game.getGameMessage(game.getUser1(),currentRound)));
        messageSender.sendMessage(game.getTokenUser2(),gson.toJson(game.getGameMessage(game.getUser2(),currentRound)));

        //Delete messages from the queue
        game.clearMessages();

        //Start a timer
        long startTimeMillis = System.currentTimeMillis();
        long remainingTimeMillis = GameConfig.DURATION_QUESTION;

        //Has a user answered the question?
        boolean user1Answered = false;
        boolean user2Answered = false;

        //How much time has passed to answer
        long user1Time = 10000;
        long user2Time = 10000;

        //answers of the users
        String user1Answer = "";
        String user2Answer = "";

        //Start the timer loop
        do {
            try {

                //Wait the remaining time
                Thread.sleep(remainingTimeMillis);

                //Set it to 0 afterwards
                remainingTimeMillis = 0;

            } catch (InterruptedException e) {

                //Check if theres a message
                if(game.isNextMessage()) {
                    IncomingWebSocketMessage message = game.getNextMessage();

                    //Set the used time and answer for the first user
                    if(game.getUser1().equals(message.getUser()) && !user1Answered) {
                        user1Time = System.currentTimeMillis() - startTimeMillis;
                        user1Answer = message.getMessage();
                        user1Answered = true;
                        System.out.println(game.getUser1().getUserName() + " has chosen an answer.");
                    }

                    //Set the used time and answer for the second user
                    if(game.getUser2().equals(message.getUser()) && !user2Answered) {
                        user2Time = System.currentTimeMillis() - startTimeMillis;
                        user2Answer = message.getMessage();
                        user2Answered = true;
                        System.out.println(game.getUser2().getUserName() + " has chosen an answer.");
                    }
                }

                //Calculate the new remaining time
                remainingTimeMillis = GameConfig.DURATION_QUESTION - (System.currentTimeMillis() - startTimeMillis);

                //When both players answered, the remaining time will be set to 0
                if(user1Answered && user2Answered)
                {
                    remainingTimeMillis = 0;
                }
            }
        } while (remainingTimeMillis > 0);

        //Print timer-is-over message
        System.out.println("First round is over");

        //Print the correct answer
        System.out.println("The correct answer is: " + game.getQuestion(currentRound).getAnswerCorrect());

        //Analysis of the result
        System.out.println(game.getUser1().getUserName() + " answered: " + user1Answer + " in " + user1Time + " millis. Points: " + calculatePoints(user1Time));
        System.out.println(game.getUser2().getUserName() + " answered: " + user2Answer + " in " + user2Time + " millis. Points: " + calculatePoints(user2Time));

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

    /**
     * Calculate the points for the user
     * timeMillis = 0 - GameConfig.MAXPOINTS_DURATION -> GameConfig.POINTS_MAX
     * timeMillis = GameConfig.MAXPOINTS_DURATION - GameConfig.QUESTION_DURATION -> GameConfig.POINTS_MIN - GameConfig.POINTS_MAX
     * timeMillis > GameConfig.MAXPOINTS_DURATION -> 0 points
     *
     * @param timeMillis needed time to answer
     * @return
     */
    private int calculatePoints (long timeMillis) {
        if (timeMillis < GameConfig.DURATION_MAX_POINTS) {

            //Return max points
            return GameConfig.POINTS_MAX;

        } else if (timeMillis >= GameConfig.DURATION_MAX_POINTS && timeMillis < GameConfig.DURATION_QUESTION) {

            long deltaTimeNeededMillis = timeMillis - GameConfig.DURATION_MAX_POINTS;
            int deltaPoints = GameConfig.POINTS_MAX - GameConfig.POINTS_MIN;
            long deltaTimeMillis = GameConfig.DURATION_QUESTION - GameConfig.DURATION_MAX_POINTS;

            return GameConfig.POINTS_MIN;
            //return (int) GameConfig.POINTS_MIN + GameConfig.POINTS_MAX - ((deltaTimeNeededMillis / deltaTimeMillis) * deltaPoints));
        }

        //Return 0 points
        return 0;
    }
}
