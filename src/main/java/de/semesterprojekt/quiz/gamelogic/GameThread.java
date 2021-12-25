package de.semesterprojekt.quiz.gamelogic;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.model.Game;

/**
 * The class implements the game logic
 */
public class GameThread implements Runnable{

    private Game game;
    private WebsocketMessageSender messageSender;
    Gson gson = new Gson();

    public GameThread(Game game, WebsocketMessageSender messageSender){
        this.game = game;
        this.messageSender = messageSender;
    }

    /**
     * The method implements the game logic
     */
    public void run() {

        //Print the usernames
        System.out.println("Start game: " + game.getUser1().getUserName() + " vs. " + game.getUser2().getUserName());

        //2 seconds timeout
        try {
            System.out.println("Wait two seconds...");
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        //Send the first GameMessage to both users, print the correct answer for both users
        System.out.println("First question: " + game.getQuestion(0).getQuestionText());
        System.out.println("Correct answer for \"" + game.getUser1().getUserName() + "\": answer" + game.getGameMessage(game.getUser1(),0).getCorrectAnswer());
        System.out.println("Correct answer for \"" + game.getUser2().getUserName() + "\": answer" + game.getGameMessage(game.getUser2(),0).getCorrectAnswer());
        messageSender.sendMessage(game.getTokenUser1(),gson.toJson(game.getGameMessage(game.getUser1(),0)));
        messageSender.sendMessage(game.getTokenUser2(),gson.toJson(game.getGameMessage(game.getUser2(),0)));
    }
}
