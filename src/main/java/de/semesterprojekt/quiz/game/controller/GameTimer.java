package de.semesterprojekt.quiz.game.controller;

import de.semesterprojekt.quiz.game.model.message.MessageType;
import de.semesterprojekt.quiz.game.model.message.TimerMessage;
import de.semesterprojekt.quiz.game.model.Game;
import de.semesterprojekt.quiz.websocket.controller.WebsocketMessageSender;

/**
 * Starts a timer as thread start() or blocking startBlocking(). Timer can be stopped with interrupt()
 * Sends one message per seconds to the users of a game object
 */
public class GameTimer extends Thread {

    private Game game;
    private WebsocketMessageSender messageSender;
    private MessageType timerType;
    private int timerDuration;

    public GameTimer(Game game, WebsocketMessageSender messageSender, MessageType timerType, int timerDuration){
        this.game = game;
        this.messageSender = messageSender;
        this.timerType = timerType;
        this.timerDuration = timerDuration;
    }

    @Override
    public void run() {
        startBlocking();
    }

    public void startBlocking() {
        try {

            //Send a message every second
            for(int timeLeft = timerDuration; timeLeft > 0; timeLeft--) {

                //Create a timer-message and send it to the user
                TimerMessage newTimerMessage = new TimerMessage(timerType, timeLeft);
                messageSender.sendMessage(game.getTokenUser1(), newTimerMessage);
                messageSender.sendMessage(game.getTokenUser2(), newTimerMessage);

                //Wait one second
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

        }
    }
}
