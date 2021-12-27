package de.semesterprojekt.quiz.game.message;

import lombok.Getter;

/**
 * The class represents a timer message that will be sent to the user
 */
@Getter
public class TimerMessage extends GenericMessage {

    //Add a value that represents a timer value
    private int timeLeft;

    public TimerMessage(int timeLeft) {

        //construct the extended class
        super();

        //Set the type
        super.setType(MessageType.TIMER_MESSAGE);

        //Set the value
        this.timeLeft = timeLeft;
    }

}
