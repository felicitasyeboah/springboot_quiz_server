package de.semesterprojekt.quiz.utility;

import de.semesterprojekt.quiz.database.entity.PlayedGame;

import java.util.Comparator;

/**
 * The class sorts PlayedGame objects by the max score achieved
 */
public class PlayedGameComparator implements Comparator<PlayedGame> {

    @Override
    public int compare(PlayedGame o1, PlayedGame o2) {

        //Compare the scores
        if (o1.getMaxScore() > o2.getMaxScore()) {
            return 1;
        } else if (o1.getMaxScore() < o2.getMaxScore()) {
            return -1;
        }

        //TODO: BETTER SORT BY TIMESTAMP/DATE
        //Both games have the same score, next sort by id
        if(o1.getGameId() < o2.getGameId()) {
            return 1;
        } else if (o1.getGameId() > o2.getGameId()) {
            return -1;
        }

        return 0;
    }
}
