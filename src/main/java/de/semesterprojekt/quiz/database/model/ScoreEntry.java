package de.semesterprojekt.quiz.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * The class represents a score entry with a time stamp and a score
 */
@Data
@AllArgsConstructor
public class ScoreEntry {

    private Date timeStamp;
    private int userScore;
}
