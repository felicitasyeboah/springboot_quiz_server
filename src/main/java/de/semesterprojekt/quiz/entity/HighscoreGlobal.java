package de.semesterprojekt.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HighscoreGlobal {

    @Id
    private int highscoreID;

    private int gameID;

    private int userID;

    public int getHighscoreID() {
        return highscoreID;
    }

    public void setHighscoreID(int highscoreID) {
        this.highscoreID = highscoreID;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
