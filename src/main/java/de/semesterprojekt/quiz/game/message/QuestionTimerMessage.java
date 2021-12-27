package de.semesterprojekt.quiz.game.message;

import lombok.Getter;

@Getter
public class QuestionTimerMessage extends TimerMessage {
    public QuestionTimerMessage(int timeLeft) {
        super(timeLeft);
        super.setType(MessageType.QUESTION_TIMER_MESSAGE);

    }
}
