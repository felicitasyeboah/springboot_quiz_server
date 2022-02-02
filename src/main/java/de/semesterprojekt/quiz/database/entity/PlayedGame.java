package de.semesterprojekt.quiz.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * The class represents a played game for two users
 */
@Data
@Entity
@NoArgsConstructor
public class PlayedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
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

    /**
     * All args Constructor
     */
    public PlayedGame (User user1, User user2, int userScore1, int userScore2) {

        //Set the date and time
        this.timeStamp = new Date();

        //Set the users
        this.user1 = user1;
        this.user2 = user2;

        //Set the scores
        this.userScore1 = userScore1;
        this.userScore2 = userScore2;
    }
}