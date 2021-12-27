package de.semesterprojekt.quiz.game.message;

public class ScoreTimerMessage extends TimerMessage{

    public ScoreTimerMessage(int timeLeft) {
        super(timeLeft);
        super.setType(MessageType.SCORE_TIMER_MESSAGE);
    }
}
