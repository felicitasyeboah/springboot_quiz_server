package de.semesterprojekt.quiz.game.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.message.QuestionTimerMessage;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.WebsocketMessageSender;

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
            for(int timeLeft = GameConfig.DURATION_QUESTION; timeLeft > 0; timeLeft--) {

                //Create a timer-message and send it to the user
                QuestionTimerMessage newQuestionTimerMessage = new QuestionTimerMessage(timeLeft);
                messageSender.sendMessage(game.getTokenUser1(), newQuestionTimerMessage);
                messageSender.sendMessage(game.getTokenUser2(), newQuestionTimerMessage);

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

        }
    }
}
