package de.semesterprojekt.quiz.game.message;

import lombok.Getter;

/**
 * The class represents a timer message that will be sent to the user
 */
@Getter
public class TimerMessage extends GenericMessage {

    private int timeLeft;

    public TimerMessage(int timeLeft) {

        super.setType(MessageType.TIMER_MESSAGE);

        this.timeLeft = timeLeft;
    }

}
