package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PlayedGames {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    private Date timeStamp;

    @ManyToOne
    @JoinColumn(name = "user_id1",nullable = false)
    User user1;

    private int userScore1;

    @ManyToOne
    @JoinColumn(name = "user_id2",nullable = false)
    User user2;

    private int userScore2;
}
