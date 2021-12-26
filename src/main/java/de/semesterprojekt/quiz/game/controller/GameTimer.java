package de.semesterprojekt.quiz.game.controller;

import com.google.gson.Gson;
import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.message.WebsocketMessageSender;

public class GameTimer extends Thread {

    private Game game;
    private WebsocketMessageSender messageSender;

    public GameTimer(Game game, WebsocketMessageSender messageSender){
        this.game = game;
        this.messageSender = messageSender;
    }

    @Override
    public void run() {

        try {

            //Send a message every second until the timer gets interrupted
            for(int i = GameConfig.DURATION_QUESTION; i > 0; i--) {
                System.out.println("Game timer: " + i + " seconds left.");

                //TODO: SEND TIMER MESSAGE

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

        }
    }
}
