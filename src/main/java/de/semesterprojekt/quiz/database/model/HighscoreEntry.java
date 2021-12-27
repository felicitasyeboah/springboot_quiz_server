package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Data;

import java.util.Date;

/**
 * The class represents a highscore entry
 */
@Data
public class HighscoreEntry {

    public HighscoreEntry(Date timeStamp, User user, int userScore) {
        this.timeStamp = timeStamp;
        this.user = user.getSimpleUser();
        this.userScore = userScore;

    }

    private Date timeStamp;
    private SimpleUser user;
    private int userScore;

}
