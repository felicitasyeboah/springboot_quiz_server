package de.semesterprojekt.quiz.game.message;

public class StartTimerMessage extends TimerMessage{
    public StartTimerMessage(int timeLeft) {
        super(timeLeft);
        super.setType(MessageType.START_TIMER_MESSAGE);
    }
}
