package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class PlayedGames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    private Date timeStamp;

    private int userId1;

    private int userId2;

    private int userScore1;

    private int userScore2;
}
