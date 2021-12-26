package de.semesterprojekt.quiz.game.message;

import de.semesterprojekt.quiz.model.SimpleUser;
import lombok.Getter;

/**
 * The class represents a score message that will be sent once after each round
 */
@Getter
public class ScoreMessage extends GenericMessage {

    //Store the user and the opponent
    private SimpleUser user;
    private SimpleUser opponent;

    //Stores the score of the user and the opponent
    private int userScore;
    private int opponentScore;

    public ScoreMessage(SimpleUser user, SimpleUser opponent, int userScore, int opponentScore) {

        super.setType(MessageType.SCORE_MESSAGE);
        this.user = user;
        this.opponent = opponent;
        this.userScore = userScore;
        this.opponentScore = opponentScore;
    }
}
