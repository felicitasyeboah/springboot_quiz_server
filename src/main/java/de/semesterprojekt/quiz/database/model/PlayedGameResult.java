package de.semesterprojekt.quiz.database.model;

import lombok.Getter;
import java.util.List;

/**
 * The class represents a result for the last played games
 * if offers a list of all games, an average score, the won, lost and drawed games and the total count of the games
 */
@Getter
public class PlayedGameResult {

    List<UserScoreEntry> playedGames;
    int wonGames = 0;
    int lostGames = 0;
    int drawGames = 0;
    int averageScore = 0;
    int gameCount;

    /**
     * Contructor
     * @param playedGames list of played games
     */
    public PlayedGameResult(List<UserScoreEntry> playedGames) {

        //Set the attributes
        this.playedGames = playedGames;
        this.gameCount = playedGames.size();

        //Calculate the values
        calculateValues();
    }

    /**
     * The method calculates the won/lost games and the average scores
     */
    private void calculateValues() {
        if(playedGames != null) {
            for(UserScoreEntry entry : playedGames) {

                //Add won/lost games
                if(entry.getUserScore() > entry.getOpponentScore()) {
                    wonGames++;
                } else if (entry.getUserScore() < entry.getOpponentScore()) {
                    lostGames++;
                } else {
                    drawGames++;
                }

                //Add score
                averageScore = averageScore + entry.getUserScore();
            }

            //Calculate average score
            averageScore = Math.round(averageScore/playedGames.size());
        }
    }
}