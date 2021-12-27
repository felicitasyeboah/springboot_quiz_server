package de.semesterprojekt.quiz.game.message;

public class ResultTimerMessage extends TimerMessage {
    public ResultTimerMessage(int timeLeft) {
        super(timeLeft);
        super.setType(MessageType.RESULT_TIMER_MESSAGE);
    }
}
