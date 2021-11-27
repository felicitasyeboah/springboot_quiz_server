package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.*;

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
