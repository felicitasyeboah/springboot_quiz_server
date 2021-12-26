package de.semesterprojekt.quiz.game.message;

import lombok.Getter;

/**
 * The class represents a timer message that will be sent to the user
 */
@Getter
public class TimerMessage {

    private int timeLeft;
    private MessageType type;

    public TimerMessage(int timeLeft) {

        this.type = MessageType.TIMER_MESSAGE;

        this.timeLeft = timeLeft;
    }

}
