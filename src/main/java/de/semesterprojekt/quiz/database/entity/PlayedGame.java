package de.semesterprojekt.quiz.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;

/**
 * The class represents a played game for two users
 */
@Data
@Entity
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
     * The method returns the biggest userscore of the game
     * @return
     */
    @JsonIgnore
    public int getMaxScore() {
        if (this.getUserScore1() > this.getUserScore2()) {
            return this.getUserScore1();
        } else {
            return this.getUserScore2();
        }
    }
}
