package de.semesterprojekt.quiz.database.utility;

import de.semesterprojekt.quiz.database.entity.PlayedGame;

import java.util.Comparator;

/**
 * The class sorts PlayedGame objects by the date
 */
public class PlayedGameDateComparator implements Comparator<PlayedGame> {

    @Override
    public int compare(PlayedGame o1, PlayedGame o2) {

        //Compare the objects ascending by date
        if (o1.getTimeStamp().before(o2.getTimeStamp())) {
            return -1;
        } else if (o1.getTimeStamp().after(o2.getTimeStamp())) {
            return 1;
        }

        return 0;
    }
}
