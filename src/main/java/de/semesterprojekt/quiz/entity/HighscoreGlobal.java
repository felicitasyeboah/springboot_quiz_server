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
}
