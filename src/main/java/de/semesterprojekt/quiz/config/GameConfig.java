package de.semesterprojekt.quiz.config;

public class GameConfig {

    //The count of the questions
    public final static int COUNT_QUESTION = 3;

    //The question duration in seconds
    public final static int DURATION_QUESTION = 10;

    //The time before each question in seconds
    public final static int DURATION_BREAK = 2;

    //The question duration in milliseconds (Must be smaller than QUESTION_DURATION)
    public final static long DURATION_MAX_POINTS = 1000;

    //The minimum points for a correct question
    public final static int POINTS_MIN = 500;

    //The maximum points for a correct question
    public final static int POINTS_MAX = 1000;

    //The length of the highscore-list
    public final static int LENGTH_HIGHSCORE_LIST = 3;
}
