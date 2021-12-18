package de.semesterprojekt.quiz.entity;

/**
 * The class represents a game session
 */
public class Game {

    private static int gameIdGenerator = 0;
    private int gameId;
    private int countQuestions;
    private User user1;
    private User user2;
    private int scoreUser1;
    private int scoreUser2;
    private Question[] question;

    /**
     * The contructor generates a new game session
     * @param user1 User 1
     * @param user2 User 2
     * @param countQuestions Count of the questions
     */
    public Game(User user1, User user2, int countQuestions) {
        //generates a new gameId
        this.gameId = gameIdGenerator++;
        this.user1 = user1;
        this.user2 = user2;
        this.scoreUser1 = 0;
        this.scoreUser2 = 0;
        this.countQuestions = countQuestions;
        this.question = new Question[countQuestions];
    }

    /**
     * Returns the game ID
     * @return gameId
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Returns user 1
     * @return user1
     */
    public User getUser1(){
        return this.user1;
    }

    /**
     * Returns user 2
     * @return user2
     */
    public User getUser2(){
        return this.user2;
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
        if(index < 0 || index >= countQuestions) {
            index = 0;
        }
        return question[index];
    }

    /**
     * Returns the count of the questions
     * @return questionCount
     */
    public int getQuestionCount() {
        return this.countQuestions;
    }

    /**
     * Sets the questions
     * @param questionList question array
     */
    public void setQuestions(Question[] questionList) {
        this.question = questionList;
    }

    /**
     * The method returns a GameMessage object for the specified user
     * @param round game round. First round is 0
     * @param user the requested user
     * @return GameMessage Object
     */
    public GameMessage getGameMessage(int round, User user) {

        //Return null if the requested index is out of bound
        if(round >= this. countQuestions) {
            return null;
        }

        //Return the GameMessage object for the specified user
        if(user == this.user1) {

            return new GameMessage(this.user1, this.user2, this.scoreUser1, this.scoreUser2, this.question[round]);

        } else if (user == this.user2) {

            return new GameMessage(this.user2, this.user1, this.scoreUser2, this.scoreUser1, this.question[round]);

        }

        //Return null if the user is wrong
        return null;
    }
}