package de.semesterprojekt.quiz.model;

import de.semesterprojekt.quiz.config.GameConfig;
import de.semesterprojekt.quiz.entity.Question;
import de.semesterprojekt.quiz.entity.User;
import de.semesterprojekt.quiz.utility.QuestionRandomizer;

import java.util.*;

/**
 * The class represents a game session
 */
public class Game extends Observable {

    private static int gameIdGenerator = 0;
    private int gameId;
    private User user1;
    private User user2;
    private String tokenUser1;
    private String tokenUser2;
    private int scoreUser1;
    private int scoreUser2;
    private List<Question> question;
    private Queue<IncomingWebSocketMessage> messageInputQueue;

    /**
     * The contructor generates a new game session
     * @param user1 User 1
     * @param user2 User 2
     * @param questionRandomizer Instance of the QuestionRandomizer
     */
    public Game(User user1, String tokenUser1, User user2, String tokenUser2, QuestionRandomizer questionRandomizer) {
        //generates a new gameId
        this.gameId = gameIdGenerator++;
        this.user1 = user1;
        this.user2 = user2;
        this.tokenUser1 = tokenUser1;
        this.tokenUser2 = tokenUser2;
        this.scoreUser1 = 0;
        this.scoreUser2 = 0;
        this.question = questionRandomizer.getQuestions();
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
     * Returns token of user1
     * @return token of user1
     */
    public String getTokenUser1(){
        return this.tokenUser1;
    }

    /**
     * Returns token of user2
     * @return token of user2
     */
    public String getTokenUser2(){
        return this.tokenUser2;
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
        if(index < 0 || index >= GameConfig.QUESTION_COUNT) {
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
        if(round >= GameConfig.QUESTION_COUNT) {
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
    public synchronized void addMessage(IncomingWebSocketMessage newMessage) {

        //Add the message if its from one of the users
        if(newMessage.getUser().equals(this.user1) || newMessage.getUser().equals(this.user2)) {
            this.messageInputQueue.add(newMessage);
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Return the first message from the queue
     * @return
     */
    public IncomingWebSocketMessage getNextMessage() {

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
}