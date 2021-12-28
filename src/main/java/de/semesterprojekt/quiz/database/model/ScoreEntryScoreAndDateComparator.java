package de.semesterprojekt.quiz.database.model;

import java.util.Comparator;

/**
 * A comparator to sort ScoreEntry objects by score and then by date
 */
public class ScoreEntryScoreAndDateComparator implements Comparator<ScoreEntry> {

    @Override
    public int compare(ScoreEntry o1, ScoreEntry o2) {

        //Compare the scores -> higher score equals a lower position after sorting
        if (o1.getUserScore() > o2.getUserScore()) {
            return -1;
        } else if (o1.getUserScore() < o2.getUserScore()) {
            return 1;
        }

        //Compare the dates -> lower date equals a lower position after sorting
        if (o1.getTimeStamp().before(o2.getTimeStamp())) {
            return -1;
        } else if (o2.getTimeStamp().before(o1.getTimeStamp())) {
            return 1;
        }

        return 0;
    }
}
