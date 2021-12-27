package de.semesterprojekt.quiz.game.model.message;

import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.database.model.SimpleUser;
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

    public ScoreMessage(User user, User opponent, int userScore, int opponentScore) {

        //construct the extended class
        super();

        //Set the type
        super.setType(MessageType.SCORE_MESSAGE);

        //Set the users
        this.user = user.getSimpleUser();
        this.opponent = opponent.getSimpleUser();

        //Set the scores
        this.userScore = userScore;
        this.opponentScore = opponentScore;
    }
}
