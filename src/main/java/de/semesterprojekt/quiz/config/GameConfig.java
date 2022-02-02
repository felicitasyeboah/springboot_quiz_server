package de.semesterprojekt.quiz.config;

/**
 * The class configures the game settings
 */
public class GameConfig {

    //The count of the questions
    public final static int COUNT_QUESTION = 3;

    //The question duration in seconds
    public final static int DURATION_QUESTION = 10;

    //The time before each game will start in seconds
    public final static int DURATION_START = 3;

    //The time after each question to show score for this question in seconds
    public final static int DURATION_SCORE = 3;

    //The question duration in milliseconds (Must be smaller than QUESTION_DURATION)
    public final static long DURATION_MAX_POINTS = 1000;

    //The minimum points for a correct question
    public final static int POINTS_MIN = 500;

    //The maximum points for a correct question
    public final static int POINTS_MAX = 1000;

    //The length of the highscore-list
    public final static int LENGTH_HIGHSCORE_LIST = 5;

    //The length of the user-game-history-list
    public final static int LENGTH_USER_PLAYED_GAMES_LIST = 20;

    //The allowed file types for the uploaded profile images
    public final static String[] ALLOWED_FILE_TYPE = {"png", "jpg", "jpeg", "gif", "tiff", "bmp"};
}