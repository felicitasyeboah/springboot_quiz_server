package de.semesterprojekt.quiz.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class HighscoreGlobal {

    @Id
    private int highscoreId;

    private int gameId;

    private int userId;
}
