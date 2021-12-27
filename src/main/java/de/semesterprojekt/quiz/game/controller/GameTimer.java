package de.semesterprojekt.quiz.game.controller;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.game.message.MessageType;
import de.semesterprojekt.quiz.game.message.TimerMessage;
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
                TimerMessage newTimerMessage = new TimerMessage(MessageType.QUESTION_TIMER_MESSAGE, timeLeft);
                messageSender.sendMessage(game.getTokenUser1(), newTimerMessage);
                messageSender.sendMessage(game.getTokenUser2(), newTimerMessage);

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

        }
    }
}
