package de.semesterprojekt.quiz.game.model;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.database.controller.QuestionController;
import de.semesterprojekt.quiz.database.entity.Question;
import de.semesterprojekt.quiz.database.entity.User;
import de.semesterprojekt.quiz.game.model.message.GameMessage;
import de.semesterprojekt.quiz.websocket.model.UserAnswerMessage;

import java.util.*;

/**
 * The class represents a game session
 */
public class Game extends Observable {

    private static int gameIdGenerator = 0;
    private int gameId;
    private User user1;
    private User user2;
    private String uuidUser1;
    private String uuidUser2;
    private int scoreUser1;
    private int scoreUser2;
    private List<Question> question;
    private Queue<UserAnswerMessage> messageInputQueue;

    /**
     * The contructor generates a new game session
     * @param user1 User 1
     * @param user2 User 2
     * @param questionController Instance of the QuestionController
     */
    public Game(User user1, String uuidUser1, User user2, String uuidUser2, QuestionController questionController) {

        //generates a new gameId
        this.gameId = gameIdGenerator++;
        this.user1 = user1;
        this.user2 = user2;
        this.uuidUser1 = uuidUser1;
        this.uuidUser2 = uuidUser2;
        this.scoreUser1 = 0;
        this.scoreUser2 = 0;
        this.question = questionController.getQuestions();
        this.messageInputQueue = new LinkedList<>();
    }

    /**
     * Returns the game ID
     * @return gameId
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Returns user1
     * @return user1
     */
    public User getUser1(){
        return this.user1;
    }

    /**
     * Returns user2
     * @return user2
     */
    public User getUser2(){
        return this.user2;
    }

    /**
     * Returns uuid of user1
     * @return uuid of user1
     */
    public String getUuidUser1(){
        return this.uuidUser1;
    }

    /**
     * Returns uuid of user2
     * @return uuid of user2
     */
    public String getUuidUser2(){
        return this.uuidUser2;
    }

    /**
     * Returns score of user 1
     * @return score of user1
     */
    public int getScoreUser1() {
        return scoreUser1;
    }

    /**
     * Returns score of user 2
     * @return score of user2
     */
    public int getScoreUser2() {
        return scoreUser2;
    }

    /**
     * Adds a value to the current score of user 1 and returns the new score
     * @return score of user1
     */
    public int addScoreUser1(int addScore) {
        this.scoreUser1 += addScore;
        return getScoreUser1();
    }

    /**
     * Adds a value to the current score of user 2 and returns the new score
     * @return score of user2
     */
    public int addScoreUser2(int addScore) {
        this.scoreUser2 += addScore;
        return getScoreUser2();
    }

    /**
     * Returns a question by its index
     * If the index is out of bounds, it is set to 0
     * @param index
     * @return question
     */
    public Question getQuestion(int index) {
        if(index < 0 || index >= GameConfig.COUNT_QUESTION) {
            index = 0;
        }
        return question.get(index);
    }

    /**
     * The method returns a GameMessage object for the specified user
     * @param round game round. First round is 0
     * @param user the requested user
     * @return GameMessage Object
     */
    public GameMessage getGameMessage(User user, int round) {

        //Return null if the requested index is out of bound
        if(round >= GameConfig.COUNT_QUESTION) {
            return null;
        }

        //Return the GameMessage object for the specified user
        if(user == this.user1) {

            return new GameMessage(this.user1, this.user2, this.scoreUser1, this.scoreUser2, this.question.get(round));

        } else if (user == this.user2) {

            return new GameMessage(this.user2, this.user1, this.scoreUser2, this.scoreUser1, this.question.get(round));

        }

        //Return null if the user is wrong
        return null;
    }

    /**
     * The method adds a queue for incoming messages during a game
     * It notifies the observer (GameThread instance)
     */
    public synchronized void addMessage(UserAnswerMessage newMessage) {

        //Add the message if its from one of the users
        if(newMessage.getUser().equals(this.user1) || newMessage.getUser().equals(this.user2)) {
            this.messageInputQueue.add(newMessage);
            setChanged();
            notifyObservers("NEW_MESSAGE");
        }
    }

    /**
     * Return the first message from the queue
     * @return
     */
    public UserAnswerMessage getNextMessage() {

        //Get the first entry of the queue
        return messageInputQueue.poll();
    }

    /**
     * Check for a new message
     * @return
     */
    public boolean isNextMessage() {

        //Is the queue not empty?
        return !messageInputQueue.isEmpty();
    }

    /**
     * Delete the messageInputQueue
     */
    public void clearMessages() {

        //Clear the queue
        this.messageInputQueue.clear();
    }

    /**
     * Checks the answer and add the calculated points
     */
    public void submitAnswer(User user, int round, String answer, long answerTime) {

        //Check the answer
        if(answer.equals(this.question.get(round).getAnswerCorrect())) {

            //Check which user answered and calculate the points
            if(user.equals(this.getUser1())) {
                this.addScoreUser1(calculatePoints(answerTime));
            } else if (user.equals(this.getUser2())) {
                this.addScoreUser2(calculatePoints(answerTime));
            }
        }
    }

    /**
     * Calculate the points of a round
     *
     * @param answerTime needed time to answer
     * @return
     */
    private int calculatePoints (long answerTime) {
        if (answerTime < GameConfig.DURATION_MAX_POINTS) {

            //Return max points
            return GameConfig.POINTS_MAX;

        } else if (answerTime >= GameConfig.DURATION_MAX_POINTS && answerTime <= GameConfig.DURATION_QUESTION * 1000) {

            //Calculate the points
            return (int) (GameConfig.POINTS_MIN + ((GameConfig.POINTS_MAX - GameConfig.POINTS_MIN) * (1 - ((float) (answerTime - GameConfig.DURATION_MAX_POINTS) / (GameConfig.DURATION_QUESTION * 1000 - GameConfig.DURATION_MAX_POINTS)))));
        }

        //Return 0 points
        return 0;
    }

    /**
     * Method calls the observer to delete the game from the lobby
     */
    public synchronized void setGameOver() {

        setChanged();
        notifyObservers("GAME_OVER");
    }

    /**
     * Method calls the observer to sends a message to the still connected user and deletes the game from the lobby
     */
    public synchronized void setDisconnected() {

        setChanged();
        notifyObservers("USER_DISCONNECTED");
    }
}