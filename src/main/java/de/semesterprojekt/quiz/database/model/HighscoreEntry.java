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

    /**
     * Override equals method, because the database offers another date format
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {

        //Is object a HighscoreEntry?
        if(o instanceof HighscoreEntry) {

            //cast object to HighscoreEntry
            HighscoreEntry object = (HighscoreEntry) o;

            //Check date with getTime because the database has another format
            if(super.getTimeStamp().getTime() == object.getTimeStamp().getTime()
                    && super.getUserScore() == object.getUserScore()
                    && this.user.equals(object.getUser())){
                return true;
            }
        }

        //Objects are not equal
        return false;
    }
}
