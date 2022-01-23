package de.semesterprojekt.quiz.database.model;

import de.semesterprojekt.quiz.database.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class PlayedGameResult {

    User user;
    List<UserScoreEntry> playedGames;
    int wonGames = 0;
    int lostGames = 0;
    int averageScore = 0;

    public PlayedGameResult(User user, List<UserScoreEntry> playedGames) {

        //Set the attributes
        this.user = user;
        this.playedGames = playedGames;

        //Calculate the values
        calculateValues();
    }

    /**
     * The method calculates the won/lost games and the average scores
     */
    private void calculateValues() {
        if(user != null && playedGames != null) {
            for(UserScoreEntry entry : playedGames) {

                //Add won/lost games
                if(entry.getUserScore() > entry.getOpponentScore()) {
                    wonGames++;
                } else {
                    lostGames++;
                }

                //Add score
                averageScore = averageScore + entry.getUserScore();
            }

            //Calculate average score
            averageScore = Math.round(averageScore/playedGames.size());
        }
    }
}
