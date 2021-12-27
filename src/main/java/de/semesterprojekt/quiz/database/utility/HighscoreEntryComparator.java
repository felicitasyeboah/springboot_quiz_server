package de.semesterprojekt.quiz.database.utility;

import de.semesterprojekt.quiz.database.entity.PlayedGame;
import de.semesterprojekt.quiz.database.model.HighscoreEntry;

import java.util.Comparator;

/**
 * The class sorts PlayedGame objects descending by the max score achieved
 */
public class HighscoreEntryComparator implements Comparator<HighscoreEntry> {

    @Override
    public int compare(HighscoreEntry o1, HighscoreEntry o2) {

        //Compare the scores
        if (o1.getUserScore() > o2.getUserScore()) {
            return -1;
        } else if (o1.getUserScore() < o2.getUserScore()) {
            return 1;
        }

        //TODO: SORTIERUNG NACH TIMESTAMP TESTEN
        //Compare the timestamp at equal scores - check for null values first
        if(o1.getTimeStamp() == null && o2.getTimeStamp() == null) {
            return 0;
        }

        if(o1.getTimeStamp() == null) {
            return 1;
        }

        if(o2.getTimeStamp() == null) {
            return -1;
        }

        if (o1.getTimeStamp().before(o2.getTimeStamp())) {
            return -1;
        } else if (o2.getTimeStamp().before(o1.getTimeStamp())) {
            return 1;
        }


        return 0;
    }
}
