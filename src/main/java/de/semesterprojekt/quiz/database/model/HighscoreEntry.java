package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * The class represents a highscore entry, it adds a simple user to a score entry
 */
@Getter
@Setter
public class HighscoreEntry extends ScoreEntry {

    private SimpleUser user;

    public HighscoreEntry(Date timeStamp, User user, int userScore) {

        super(timeStamp,userScore);
        this.user = user.getSimpleUser();
    }
}
