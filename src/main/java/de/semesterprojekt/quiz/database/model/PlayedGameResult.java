package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayedGameResult {

    List<UserScoreEntry> playedGames;
    int wonGames = 0;
    int lostGames = 0;
    int drawGames = 0;
    int averageScore = 0;
    int gameCount;

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
