package de.semesterprojekt.quiz.database.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * The class represents a highscore-list for the played games of the users
 */
@Data
@Entity
public class HighscoreGlobal {

    @Id
    private int highscoreId;

    @ManyToOne
    @JoinColumn(name = "game_id",nullable = false)
    PlayedGames playedGame;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    User user;
}
