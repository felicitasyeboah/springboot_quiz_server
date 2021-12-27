package de.semesterprojekt.quiz.game.message;

/**
 * defines the types of the messages a game sends
 */
public enum MessageType {

    //Add values to the ENUM
    GENERIC_MESSAGE("GENERIC_MESSAGE"),
    GAME_MESSAGE("GAME_MESSAGE"),
    SCORE_MESSAGE("SCORE_MESSAGE"),
    RESULT_MESSAGE("RESULT_MESSAGE"),
    TIMER_MESSAGE("TIMER_MESSAGE");

    private final String value;

    private MessageType(String value) {

        this.value = value;
    }

    //Override toString method to get the value of the enum
    @Override
    public String toString() {

        return this.value;
    }
}
