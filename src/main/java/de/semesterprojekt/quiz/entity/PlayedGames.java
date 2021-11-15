package de.semesterprojekt.quiz.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity

public class PlayedGames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameID;

    private Date timeStamp;

    private int user1ID;

    private int user2ID;

    private int user1Score;

    private int user2Score;

}
